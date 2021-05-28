#A simple Makefile
#Matthew Souvannaphandhu G01187346
#CS 262 Lab Section 203
#Project 3
# The compiler: gcc for C programs, g++ for C++, etc
CC = gcc
CFLAGS=-Wall -g -std=c89 -D_XOPEN_SOURCE=600 -pedantic-errors
TARGET = RadixSort_msouvann_203
LIBS=-lm
all: $(TARGET)

$(TARGET): $(TARGET).c
	$(CC) $(CFLAGS) -o $(TARGET) $(TARGET).c $(LIBS)
#all: lab6_msouvann_203.c
#	gcc $(CFLAGS) -o lab6_msouvann_203 lab6_msouvann_203.c $(LIBS) 

clean:
	rm $(TARGET)


