CC = gcc
CFLAGS = -g

TARGET1 = RShellClient1
TARGET2 = RShellServer1
SHA = sha1

all: $(TARGET1) $(TARGET2) 

$(TARGET1): 
	$(CC) $(CFLAGS) -o $(TARGET1) $(SHA).c $(TARGET1).c -lcrypto -lssl

$(TARGET2):
	$(CC) $(CFLAGS) -o $(TARGET2) $(SHA).c $(TARGET2).c -lcrypto -lssl


#all: simpleRShellServer1.c
#	gcc $(CFLAGS) -o simpleRShellServer1 simpleRShellServer1.c $(LIBS) 

clean:
	rm $(TARGET1) $(TARGET2)
	

