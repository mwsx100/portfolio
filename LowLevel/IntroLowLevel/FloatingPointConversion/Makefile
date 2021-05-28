#A simple Makefile
#Matthew Souvannaphandhu G01187346
#CS 262 Lab Section 203
#Project 2
# The compiler: gcc for C programs, g++ for C++, etc
CC = gcc
CFLAGS=-Wall -g -std=c89 -D_XOPEN_SOURCE=600 -pedantic-errors
TARGET = Project2_msouvann_203
LIBS=-lm
all: $(TARGET)

$(TARGET): $(TARGET).c GMUsms.o 
	$(CC) $(CFLAGS) -o $(TARGET) $(TARGET).c GMUsms.o 
#all: lab3_msouvann_203.c
#	gcc $(CFLAGS) -o lab3_msouvann_203 lab3_msouvann_203.c $(LIBS) 

clean:
	rm $(TARGET)


