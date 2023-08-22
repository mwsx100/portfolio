/*  
 *   simpleRShellClient1.c	example program for CS 468
 */


// OpenSSL Imports
#include "sha1.h"
#include <time.h>
#include <stdbool.h>

// Other Imports
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <ifaddrs.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/errno.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>
#include <sys/errno.h>

// Definitions for message type
#define RSHELL_REQ 0x01
#define AUTH_REQ 0x02
#define AUTH_RESP 0x03
#define AUTH_SUCCESS 0x04
#define AUTH_FAIL 0x05
#define RSHELL_RESULT 0x06

// Size in bytes of Message type
#define TYPESIZE 1

 // Size in bytes of Message payload length
#define LENSIZE 2

// Max ID size: 16 - 1 = 15 bytes for id, 1 for null term
#define IDSIZE 16

// Password size (in Hex)--> 20 bytes, 2 chars rep 1 byte, so 40 chars
#define PASSWDSIZE 40

// Max length of payload (2^16) = 65536
#define MAXPLSIZE 65536

// Max potential message size (2^1) + (2^2) + (2^16)
#define MAXMSGSIZE 65542

// Command size
#define MAXBUFSIZE ((MAXPLSIZE - IDSIZE) - 1)

// provided code definitions
#define LINELEN     MAXBUFSIZE
#define BUFSZ       MAXBUFSIZE
#define resultSz    (MAXPLSIZE - 1)


// Typedef for the message format
typedef struct Message{
	// Message type
	char msgtype;
	// payload length in bytes
	short paylen;
	// id for the first 16 bytes of the payload
	char id[IDSIZE];
	// the payload
    char *payload;
}Message;


// Method to determine the message type.
int decode_type(Message *msg){
    switch(msg -> msgtype){
        case RSHELL_REQ :
            printf("Received RSHELL_REQ message.\n");
            return 1;
            break;
        case AUTH_REQ :
            printf("Received AUTH_REQ message.\n");
            return 2;
            break;
        case AUTH_RESP :
            printf("Received AUTH_RESP message.\n");
            return 3;
            break;
        case AUTH_SUCCESS :
            printf("Received AUTH_SUCCESS message.\n");
            return 4;
            break;
        case AUTH_FAIL :
            printf("Received AUTH_FAIL message.\n");
            return 5;
            break;
        case RSHELL_RESULT :
            printf("Received RSHELL_RESULT message.\n");
            return 6;
            break;
        default :
            printf("ERROR: Received Invalid message.\n");
            return -1;
            break;
    }
}

// Debug method to print a Message
void print_message(Message *msg){
	printf("MESSAGE--> TYPE:0x0%d   PAYLEN:%d  ID:%s   PAYLOAD:%s\n\n", msg->msgtype, msg->paylen, msg->id, msg->payload);
}

int
clientsock(int UDPorTCP, const char *destination, int portN)
{
	struct hostent	*phe;		/* pointer to host information entry	*/
	struct sockaddr_in dest_addr;	/* destination endpoint address		*/
	int    sock;			/* socket descriptor to be allocated	*/


	bzero((char *)&dest_addr, sizeof(dest_addr));
	dest_addr.sin_family = AF_INET;

    /* Set destination port number */
	dest_addr.sin_port = htons(portN);

    /* Map host name to IPv4 address, does not work well for IPv6 */
	if ( (phe = gethostbyname(destination)) != 0 )
		bcopy(phe->h_addr, (char *)&dest_addr.sin_addr, phe->h_length);
	else if (inet_aton(destination, &(dest_addr.sin_addr))==0) /* invalid destination address */
		return -2;

/* version that support IPv6
	else if (inet_pton(AF_INET, destination, &(dest_addr.sin_addr)) != 1)
*/

    /* Allocate a socket */
	sock = socket(PF_INET, UDPorTCP, 0);
	if (sock < 0)
		return -3;

    /* Connect the socket */
	if (connect(sock, (struct sockaddr *)&dest_addr, sizeof(dest_addr)) < 0)
		return -4;

	return sock;
}

inline int clientTCPsock(const char *destination, int portN)
{
  return clientsock(SOCK_STREAM, destination, portN);
}


inline int clientUDPsock(const char *destination, int portN)
{
  return clientsock(SOCK_DGRAM, destination, portN);
}


void usage(char *self)
{
	// Useage message when bad # of arguments
	fprintf(stderr, "Usage: %s <server IP> <server port number> <ID> <password> \n", self);
	exit(1);
}

void errmesg(char *msg)
{
	fprintf(stderr, "**** %s\n", msg);
	exit(1);

}

/*------------------------------------------------------------------------------
 * TCPrecv - read TCP socket sock w/ flag for up to buflen bytes into buf

 * return:
	>=0: number of bytes read
	<0: error
 *------------------------------------------------------------------------------
 */
int
TCPrecv(int sock, char *buf, int buflen, int flag)
{
	int inbytes, n;

	if (buflen <= 0) return 0;

  /* first recv could be blocking */
	inbytes = 0;
	n=recv(sock, &buf[inbytes], buflen - inbytes, flag);
	if (n<=0 && n != EINTR)
		return n;

	buf[n] = 0;


	printf("\tTCPrecv(sock=%d, buflen=%d, flag=%d): first read %d bytes : `%s`\n",
			   sock, buflen, flag, n, buf);

  /* subsequent tries for for anything left available */

	for (inbytes += n; inbytes < buflen; inbytes += n)
	{
	 	if (recv(sock, &buf[inbytes], buflen - inbytes, MSG_PEEK|MSG_DONTWAIT)<=0) /* no more to recv */
			break;
	 	n=recv(sock, &buf[inbytes], buflen - inbytes, MSG_DONTWAIT);
		buf[n] = 0;


		printf("\tTCPrecv(sock=%d, buflen=%d, flag=%d): subsequent read %d bytes : `%s`\n",
			   sock, buflen, flag, n, &buf[inbytes]);


	  if (n<=0) /* no more bytes to receive */
		break;
	};


		printf("\tTCPrecv(sock=%d, buflen=%d): read totally %d bytes : `%s`\n",
			   sock, buflen, inbytes, buf);


	return inbytes;
}

int
RemoteShell(char *destination, int portN)
{
	char	buf[LINELEN+1];		/* buffer for one line of text	*/
	char	result[resultSz+1];
	int	sock;				/* socket descriptor, read count*/


	int	outchars, inchars;	/* characters sent and received	*/
	int n;

	if ((sock = clientsock(SOCK_STREAM, destination, portN)) < 0)
		errmesg("fail to obtain TCP socket");

	while (fgets(buf, sizeof(buf), stdin))
	{
		buf[LINELEN] = '\0';	/* insure line null-terminated	*/
		outchars = strlen(buf);
		if ((n=write(sock, buf, outchars))!=outchars)	/* send error */
		{

			printf("RemoteShell(%s, %d): has %d byte send when trying to send %d bytes to RemoteShell: `%s`\n",
			   destination, portN, n, outchars, buf);

			close(sock);
			return -1;
		}

		printf("RemoteShell(%s, %d): sent %d bytes to RemoteShell: `%s`\n",
			   destination, portN, n, buf);


		/* Get the result */

		if ((inchars=recv(sock, result, resultSz, 0))>0) /* got some result */
		{
			result[inchars]=0;
			fputs(result, stdout);
		}
		if (inchars < 0)
				errmesg("socket read failed\n");
	}

	close(sock);
	return 0;
}

// Writes messages to socket: Returns 0 if successful, 1 if there was an error
int write_message(int sock, Message *msg){
    // Size will be the message type + paylen + ID + payload
    int msgsize = sizeof(char) + sizeof(short) + (sizeof(char) * msg->paylen);
    // n will store return value of write()
	int n;

    //printf("The size of the message you are sending is: %d\n", msgsize);

    // Write the message type
    if ( (n = write(sock, &msg->msgtype, TYPESIZE)) != TYPESIZE ){
        printf("ERROR: Has %d byte send when trying to send %d bytes for Message Type: `%s`\n", n, TYPESIZE, &msg);
        close(sock);
        return -1;
    }

    // Write the message length
    if ( (n = write(sock, &msg->paylen, LENSIZE)) != LENSIZE ){
        printf("ERROR: Has %d byte send when trying to send %d bytes for Message Length: `%s`\n", n, LENSIZE, &msg);
        close(sock);
        return -1;
    }

    // Write the user ID
    if(msg->paylen >= IDSIZE){
    	if ( (n = write(sock, &msg->id, IDSIZE)) != IDSIZE ){
        	printf("ERROR: Has %d byte send when trying to send %d bytes for Message UserID: `%s`\n", n, IDSIZE, &msg);
        	close(sock);
        	return -1;
    	}
    }

    // Write the payload, check IDSIZE + 1 for null term
    if(msg->paylen > IDSIZE){
    	if ( (n = write(sock, msg->payload, (msg->paylen - IDSIZE) )) != (msg->paylen - IDSIZE) ){
        	printf("ERROR: Has %d byte send when trying to send %d bytes for Message UserID: `%s`\n", n, (msg->paylen - IDSIZE), &msg);
        	close(sock);
        	return -1;
    	}
    }

	return 0;
}


// Recv message from socket, returns NULL if there is an error during read
Message * recv_message(int sock){
	// Create pointer to hold in the message read-in
	Message *msg = (Message*)(malloc(sizeof(Message)));

	// Read the message type
	if (recv(sock, &msg->msgtype, TYPESIZE, 0) != TYPESIZE){
		// Return NULL if there is an error
		printf("ERROR: Could not read message type.\n");
		// Free memory
		free(msg);
		// Return NULL b/c of error
		return NULL;
	}

	// Read the message length
	if (recv(sock, &msg->paylen, LENSIZE, 0) != LENSIZE){
		// Return NULL if there is an error
		printf("ERROR: Could not read message length.\n");
		// Free memory
		free(msg);
		// Return NULL b/c of error
		return NULL;
	}

    // Check if 16 bytes of ID exists
    if(msg->paylen >= IDSIZE){
    	// Write the user ID
    	if ( (recv(sock, &msg->id, IDSIZE, 0)) != IDSIZE ){
        	printf("ERROR: Could not read message ID.\n");
			// Free memory
			free(msg);
			// Return NULL b/c of error
			return NULL;
    	}
    }

    // Check if more 16 bytes of length exist, b/c first 16 is ID, the rest would be payload...
    if(msg->paylen > IDSIZE){
    	// Need to malloc new memory for the incoming payload
    	// The size is the payload size described in the message - the ID bytes
    	msg->payload = (char*)malloc( (msg->paylen - IDSIZE) * sizeof(char));
    	// Write the payload
    	if ( (recv(sock, msg->payload, (msg->paylen - IDSIZE), 0)) != (msg->paylen - IDSIZE) ){
        	printf("ERROR: Could not read message payload.\n");
        	// Free memory
			free(msg);
			// Return NULL b/c of error
			return NULL;
    	}
    }

    // Return pointer to read-in message
	return msg;
}


/*------------------------------------------------------------------------
 * main  *------------------------------------------------------------------------
 */
int
main(int argc, char *argv[])
{

	printf("hello\n");
    // Command line arg variables
	// This is the ip address to connect to, in this case, localhost
	char *destination;
	// The port number to connect to on the server
	int portN;
	// User id, ie "Alice"
	char *userid;
	// Will contain the final SHA1 hash of the user's password, needs *2 SHA digest len in order to transmit over the socket
	unsigned char password[SHA_DIGEST_LENGTH * 2];

	// String to temporarily hold the hash after the cipher is finalized
	unsigned char tmphash[SHA_DIGEST_LENGTH];
	char *initialPass;
	char *userId;

	

    // Space for command is the Payload size - 16 for user ID - 1 for null term
    char buf[MAXBUFSIZE + 1];
    // The user ID.
    char id[IDSIZE];


    // For recv
    int inchars;
	Message *recvmsg;

	// Make sure 5 arguments-- The prog name and: <server IP> <server port number> <ID> <password>
	if (argc == 5){
		destination = argv[1];
		portN = atoi(argv[2]);
		userid = argv[3];
		initialPass = argv[4];


		printf("we are here\n");
		char buff[255];
		/*SHA1_CTX ctx;
		SHA1_Init(&ctx);
		SHA1_Update(&ctx, initialPass, strlen(initialPass));
		SHA1_Final(tmphash, &ctx);*/

		char result[20];
		printf("%s\n", initialPass);
		SHA1(result, initialPass, strlen(initialPass));
		printf("now we are here\n");





		

/*
        // ADD YOUR CODE HERE
        // Use the provided SHA1 code SHA1 hashing of the password. use SHA1 here, put result of SHA1 into string, copy result into variable password, pay attention to sprintf function in testfile
        

    Now let us assume that you have the finalized hash in a variable called tmphash.  You will need reformat tmphas properly -- 2 chars at a time for 1 byte each from temp hash into hash
        You want to write this in a loop, storing the value of the hash into a char string password.  After this loop, "hash" contains the properly formatted hash that
        the OpenSSL function: "openssl sha1 -hex" would return
      
        
*/

	
		
        // Setting a dummy value for password.  Replace this with the value you calculated!!
       /* char str_fake[20] = "FAKE FAKE";*/
       // strcpy(password, result);
        
        
        //printf("The password \"%s\" has a SHA1 hash of \"%s\".\n\n", argv[4], password);


	int offset;

	for(offset=0; offset<SHA_DIGEST_LENGTH; offset++)
	{
		sprintf(((unsigned char*) &(password[offset*2])),"%02x", result[offset]&0xff);
	}
	
	printf("The password \"%s\" has a SHA1 hash of \"%s\".\n\n", argv[4], password);



        // print the 4 primary credentials:
        printf("Running Client with the following credentials...\n");
        printf("    Destination: %s\n    Port: %d\n    User_ID: %s\n    Hashed_Password: %s\n\n",destination,portN,userid,password);

        // The password from argv[4] has now been hashed and saved in password[]

	}
	else {
		// Display usage information if wrong # of arguments
		usage(argv[0]);
	}

	// Create the socket
	int	sock;
	if ((sock = clientsock(SOCK_STREAM, destination, portN)) < 0){
		errmesg("Failed to obtain TCP socket.");
		exit(1);
	}

	// Create message for command RSHELL_REQ
    Message *msg;

    // Clear the buffer
	buf[0] = '\0';


    printf("Connection established. Type a command to run on the Remote Shell...\n");
    // Get the shell command from the user
	while(fgets(buf, sizeof(buf), stdin)){
		// Check if buffer has anything
		if(strlen(buf) > 1){
			// Print newline after entered character
			printf("\n");
			// Ensure the buffer is null-terminated
		    buf[strlen(buf) - 1] = '\0';

		    // Create message for command RSHELL_REQ
		    msg = malloc(sizeof(Message));
		    // Set message type
		    msg->msgtype = 0x01;
		    // Set payload length 16 + buffer
		    msg->paylen = IDSIZE + strlen(buf);
		    // Set 16 byte id, 15 bytes for user ID max
		    memcpy(msg->id,userid,(IDSIZE - 1));
		    // Ensure the user ID is null-terminated
		    msg->id[strlen(userid)] = '\0';
		    // Set variable length Payload
		    msg->payload = buf;


			// Send RShell Req 
			printf("Sending the following Message from Client to Server:\n");
		    print_message(msg);
			write_message(sock, msg);


			// Wait for AUTH_RESP
			recvmsg = recv_message(sock);
			printf("Received Message from Server:\n");
			print_message(recvmsg);
			switch(recvmsg -> msgtype){
				case AUTH_REQ :
				  	// Create message for command AUTH_RESP
				  	free(msg);
				  	msg = malloc(sizeof(Message));
				    // Set message type
				    msg->msgtype = 0x03;
				    // Set payload length 16 + buffer + 1 for null terminator
				    msg->paylen = IDSIZE + strlen(password) + 1;
				    // Set 16 byte id, 15 bytes for user ID max
				    memcpy(msg->id,userid,(IDSIZE - 1));
				    // Ensure the user ID is null-terminated
				    msg->id[strlen(userid)] = '\0';
				    // Esnure password is null-terminated
				    password[strlen(password)] = '\0';
				    // Set variable length Payload
				    msg->payload = password;

				    // Free recvmsg
				    free(recvmsg);

				   	// Send AUTH_RESP
				   	printf("Sending the following Message from Client to Server:\n");
				   	print_message(msg);
					write_message(sock, msg);


					// Wait for AUTH_SUCCESS / AUTH_FAIL 
					recvmsg = recv_message(sock);
					printf("Received Message from Server:\n");
					print_message(recvmsg);
					switch(recvmsg -> msgtype){
						case AUTH_SUCCESS :
						    // Free recvmsg
				    		free(recvmsg);
							printf("Authentication Success!\n");

							// Get the command exec result
							recvmsg = recv_message(sock);
							printf("Received Message from Server:\n");
							print_message(recvmsg);
							if(recvmsg -> msgtype == RSHELL_RESULT){
								// Got the result
								// Print the result
								if(recvmsg->payload != NULL){
									printf("\nThe result of the command was:\n%s\n\n", recvmsg->payload);
								}else{
									// command not found
									printf("\nThe result of the command was:\ncommand not found\n\n");
								}
							}else{
								printf("ERROR: Received Invalid message.\n");
							}

							break;
						case AUTH_FAIL :
						    // Free recvmsg
				    		free(recvmsg);
							printf("Authentication Failed!\n");
							exit(1);
							break;
						default :
							printf("ERROR: Received Invalid message.\n");
							break;
					}
					break;

				case RSHELL_RESULT :
					// Print the result
					if(recvmsg->payload != NULL){
						printf("\nThe result of the command was:\n%s\n\n", recvmsg->payload);
					}else{
						// command not found
						printf("\nThe result of the command was:\ncommand not found\n\n");
					}
					break;
				default :
					printf("ERROR: Received Invalid message.\n");
					break;
			}
			// Clear the buffer
		    buf[0] = '\0';
		    // Print seperating stars
		    printf("**********************************************************************\n\n");
		    // Ask for another command
		    printf("Type another command to run on the Remote Shell...\n");
		}else{
			// Quit program
			exit(0);
		}
	}



	// Terminate the program 
	exit(0);
}
