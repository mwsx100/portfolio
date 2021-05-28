/* Matthew Souvannaphandhu G01187346
 * CS 262, Lab Section 203
 * Project 2
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "GMUsms.h"

#define PACKED_SIZE 120
#define UNPACKED_SIZE 160

void menu(char * input); /*prints out the menu and asks for input*/
void menuCheck(char * input);  /*checks the user input from menu*/
void pack(); /*packs the message*/
void unpack(); /*unpacks the message*/
unsigned char getBit(unsigned char b0, int n);
void setBit(unsigned char * b0, int pos, int bit);
int getBit2(int b0, int pos)
{
	int buf = b0 & (1<<pos);
	
	if (buf == 0) return 0;
	else return 1;
/*	int bit = (b0 >> n) & 1;
	return bit;*/
}

int getNum(float x)
{
	int y = x;
	return y;
}
float getFrac(float x)
{
	int y = x;
	return x-y;
}

void printBits(float x)
{
	int i = 0;
	int bits[8];
	x = getFrac(x);
	if(x - .5 > 0)
	{
		x -= .5;
		bits[0] = 1;
	}
	if(x - .25 > 0)
	{
		x -=.25;
		bits[1] = 1;
	}
	if(x - .125 > 0)
	{
		x -= .125;
		bits[2] = 1;
	}
	if(x - .0625 > 0) 
	{
		x-= .0625;
		bits[3] = 1;
	}
	if(x - .03125 > 0) 
	{
		x-= .03125;
		bits[4] = 1;
	}
	if(x - .015625 > 0) 
	{
		x-= .015625;
		bits[5] = 1;
	}
	if(x - .0078125 > 0) 
	{
		x-= .0078125;
		bits[6] = 1;
	}
	if(x - .00390625 > 0) 
	{
		x-= .00390625;
		bits[7] = 1;
	}
	for(i = 0; i <8; i++)
	{
		printf("%d ", bits[i]);
	}
}
int normalE(float n)
{	
	int e = 0;
	int x = 0;
	while(x == 0)
	{
		if(n > 2)
		{
			n = n/2;
			e++;
			if (n < 2)
			{
				printf("%f\n", n);	
				x = 1;
				return e;
			/*	break;*/
			}
		}
	}
	return -1;
}

float normalF(float n)
{	
	int e = 0;
	int x = 0;
	while(x == 0)
	{
		if(n > 2)
		{
			n = n/2;
			e++;
			if (n < 2)
			{
			/*	x = 1;*/
				return getFrac(n);
			/*	break;*/
			}
		}
	}
	return -1;
}

void printEbits(float n)
{
	int bits[6];
	int exp = 0;
	int i = 0;
	int e = normalE(n);
	int bias = 31;
	exp = e + bias;
	if(exp - 32 >= 0)
	{
		exp -= 32;
		bits[0] = 1;	
	}
	if(exp - 16 >=0)
	{
		exp -= 32;
		bits[1] = 1;
	}
	if(exp - 8 >= 0)
	{
		exp -= 8;
		bits[2] = 1;
	}
	if(exp - 4 >= 0)
	{
		exp -= 4;
		bits[3] = 1;
	}
	if(exp - 2 >= 0)
	{
		exp -= 2;
		bits[4] = 1;
	}
	if(exp - 1 >= 0)
	{
		exp -= 1;
		bits[5] = 1;
	}
	for(i = 0; i < 6; i++)
	{
		printf("%d ", bits[i]);
	}
}

int main()
{
	float y = 6.9;
	printf("%f\n", getFrac(1.725));
	printBits(y);
	printf("%d\n", normalE(y));
	printf("%f\n", normalF(y));
	printEbits(y);
/*	char input[10];
	int i = 0;
	while (i == 0) 
	{
	menu(input);
	menuCheck(input);
	[2;2R*/
	return 0;

}


void menu(char * input)
{
	char buffer[10];	
	fprintf(stdout, "[P]ack and save a line of text (options are 'P' or 'p')\n[U]npack and print a line of text (options are 'U' or 'u')\n[Q]uit (optioms are 'Q' or 'q')\n");
	if (fgets(buffer, sizeof(buffer), stdin) == 0)
	{
		fprintf(stderr, "Error returned from fgets()...\n");
		exit(1);
	}
	if (sscanf(buffer, "%c", input) !=1)
	{
		fprintf(stderr, "Error scanning character value from buffer...\n");
	}
}

void menuCheck(char * input)
{
	switch(*input)		
	{
		case 'Q':
		case 'q':
		{
			exit(1);
		}
		case 'P':
		case 'p':
		{
			pack();
			break;
		}
		case 'U':
		case 'u':
		{
			unpack();
			break;
		}
		case 'f':
			printf(":'(\n");
			exit(1);
		default:
		{
			fprintf(stderr, "Invalid value. Try again.\n");
		}
	}
}

void pack()
{
	int i = 0;
	int j = 0;
	int pos = 0; /*position in the packed array*/
	int len = 0; /*length of the input str*/
	unsigned char sms[UNPACKED_SIZE];
	unsigned char buffer[UNPACKED_SIZE];
	unsigned char packedArr[PACKED_SIZE] = {0};
	FILE *out = NULL;
	fprintf(stdout, "You have chosen the 'Pack and save' option\n");
	fprintf(stdout, "Enter the filename in which you want to save your line of text to.\n");
	if (fgets((char*)buffer, 160, stdin) == 0)
	{
		fprintf(stderr, "Error at fgets() in pack()...");
		exit(1);
	}
	buffer[strlen((char*)buffer)-1] = '\0';
	out = fopen((char*)buffer, "wb");
	fprintf(stdout, "Enter your line of text.\n");
	fgets((char*)buffer, 160, stdin);
	len = strlen((char*)buffer);
	for(i = 0; i <= len; i++)
	{
		sms[i] = CharToSMS(buffer[i]);
	}
	for(i = 0; i < strlen((char*)buffer); i++) /*loop where the packing happens*/
	{	
		if (i == len) break;
		for(j = 0; j < 6; j++) 
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j));				
		}
		i++;			
		if (i == len) break; /*after i is incremented, make sure we aren't at the end of the array*/
		for(j = 6; j < 8; j++) /*pos incrememnted whenever we reach 8*/
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j-6));
		}
		pos++;
		for(j = 0; j < 4; j++)
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j+2));
		}
		i++;
		if (i== len) break;
		for(j = 4; j < 8; j++)
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j-4));
		}
		pos++;
		for(j = 0; j < 2; j++)
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j+4));
		}
		i++;
		if (i == len) break;
		for(j = 2; j < 8; j++)
		{
			setBit(&packedArr[pos], j, getBit(sms[i], j-2));
		}
		pos++;

	} 
	fwrite(packedArr, sizeof(unsigned char), PACKED_SIZE, out);
	fclose(out);
}

void unpack()
{
	FILE *in = NULL;
	char buffer[100];
	unsigned char sms[UNPACKED_SIZE];
	unsigned char text[UNPACKED_SIZE] = {0};
	int i = 0;
	int j = 0;
	int len = 0;
	int pos = 0;
	fprintf(stdout, "You have chosen the 'Unpack' option\n");
	fprintf(stdout, "Enter the filename that contains the saved message.\n");
	if (fgets(buffer, 100, stdin) == 0)
	{
		fprintf(stderr, "Error at fgets() in unpack()...");
		exit(1);
	}
	buffer[strlen(buffer)-1] = '\0';
	in = fopen(buffer, "rb");
	if (in == NULL)
	{
		fprintf(stderr, "Error opening file.\n");
		exit(1);
	}
	fread(sms, sizeof(unsigned char), UNPACKED_SIZE, in);
	len = strlen((char*)sms);	
	for (i = 0; i < len; i++) /*loop where the unpacking happens*/
	{
		if (i == len) break;
		for (j = 0; j < 6; j++)
		{
			setBit(&text[pos], j, getBit(sms[i], j));
		}
		pos++;
		for (j = 0; j < 2; j++)
		{
			setBit(&text[pos], j, getBit(sms[i], j + 6));	
		}
		i++;
		if (i == len) break;

		for (j = 2; j < 6; j++) 
		{
			setBit(&text[pos], j, getBit(sms[i], j - 2));
		}
		pos++;
		for (j = 0; j < 4; j++)
		{
			setBit(&text[pos], j, getBit(sms[i], j + 4));
		}
		i++;
		if (i == len) break;

		for (j = 4; j < 6; j++)
		{
			setBit(&text[pos], j, getBit(sms[i], j - 4));			
		}
		pos++;
		for (j = 0; j < 6; j++)
		{
			setBit(&text[pos], j, getBit(sms[i], j + 2));
		}
		pos++;
		
	}
	for (i=0; i <= strlen((char*)text); i++)
	{
		text[i] = SMSToChar(text[i]);
	}
	printf("%s\n", (char*)text);
	fclose(in);
}

unsigned char getBit(unsigned char b0, int n)
{
	unsigned char bit = (b0 >> n) & 1U;
	return bit;
}

void setBit(unsigned char * b0, int pos, int bit)
{
	switch (pos)
	
	{
		case 7:
			if (bit == 0)
			{
				*b0 &= 127;
			}
			else  *b0 |= 128;	
			break;
		case 6:
			if (bit == 0)
			{
				*b0 &= 191;
			}
			else  *b0 |= 64;	
			break;
		case 5:
			if (bit == 0)
			{
				*b0 &= 223;
			}
			else  *b0 |= 32;	
			break;
		case 4:
			if (bit == 0)
			{
				*b0 &= 239;
			}
			else *b0 |= 16;
			break;
		case 3:
			if (bit == 0)
			{
				*b0 &= 247;
			}
			else *b0 |= 8;
			break;
		case 2:
			if (bit == 0)
			{
				*b0 &= 251;
			}
			else *b0 |= 4;
			break;
		case 1:
			if (bit == 0)
			{
				*b0 &= 253;
			}
			else *b0 |= 2;
			break;
		case 0:
			if (bit == 0)
			{
				*b0 &= 254;
			}
			else *b0 |= 1;
			break;
		default:
			fprintf(stderr, "Error in setBit()...\n");
	}	
}


