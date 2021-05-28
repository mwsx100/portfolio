/* Fill in your Name and GNumber in the following two comment fields
 * Name: Matthew Souvannaphandhu
 * GNumber: 01187346
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "clock.h"
#include "structs.h"
#include "constants.h"
#include "scheduler.h"

int getBit(int b0, int n);
void setUpperBit(int * b0, int pos, int bit);


/* Initialize the Schedule Struct
 * Follow the specification for this function.
 * Returns a pointer to the new Schedule or NULL on any error.
 */
Schedule *scheduler_init() {
	Schedule* new;
	new = (Schedule*)malloc(sizeof(Schedule));
	if (new == NULL) return NULL;
	new->ready_queue = (Queue*)malloc(sizeof(Queue));
	new->ready_queue->count = 0; new->ready_queue->head = NULL;	
	new->stopped_queue = (Queue*)malloc(sizeof(Queue));
	new->stopped_queue->count = 0; new->stopped_queue->head = NULL;
	new->defunct_queue = (Queue*)malloc(sizeof(Queue));
	new->defunct_queue->count = 0; new->defunct_queue->head = NULL;
	if(new->ready_queue == NULL || new->stopped_queue == NULL || new->defunct_queue == NULL) return NULL;
	return new;

}

/* Add the process into the appropriate linked list.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_add(Schedule *schedule, Process *process) 
{
	
	int state = process->flags;
	Queue * rQ = schedule->ready_queue;
	Queue * dQ = schedule->defunct_queue;
	if (getBit(state, 30))                       /*getBit func checks their state*/
	{
		setUpperBit(&state, 30, 0);
		setUpperBit(&state, 29, 1);
		process->flags = state;
		if (scheduler_count(rQ) > 0) process->next = rQ->head;
		rQ->head = process;
		rQ->count +=1;
		return 0;
	}
	if (getBit(state, 29))
	{
		if (process->time_remaining > 0)
		{
			if (scheduler_count(rQ) > 0)	process->next = rQ->head;
				rQ->head = process;
				rQ->count +=1;
				return 0;
			
		}
		else 
		{
			setUpperBit(&state, 29, 0);
			setUpperBit(&state, 27, 1);
			process->flags = state;
			if (scheduler_count(dQ) > 0) process->next = dQ->head;
			dQ->head = process;
			dQ->count +=1;
			return 0;
		}
	}
	if (getBit(state, 27)) 
	{

		if (scheduler_count(dQ) > 0)	process->next = dQ->head;
		dQ->head = process;
		dQ->count +=1;
		return 0;
			
	}
 	return -1; /*error if it fails all 3 checks*/
}

/* Move the process with matching pid from Ready to Stopped.
 * Change its State to Stopped.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_stop(Schedule *schedule, int pid) 
{

	Queue *rQ = schedule->ready_queue;
	Queue *sQ = schedule->stopped_queue;
 	Process *current = rQ->head; 
	Process * prev = NULL;
	Process * stop = (Process*) malloc(sizeof(Process));
	if (stop == NULL) return -1;
	if (current != NULL && current->pid == pid) /*checks if head is what were looking for*/
	{
		rQ->head = current->next;
		stop = current;
		setUpperBit(&stop->flags, 29, 0);
		setUpperBit(&stop->flags, 28, 1);
		stop->next = sQ->head;
		sQ->head = stop;
		sQ->count +=1;
		rQ->count -=1;
		return 0;
	}
	while (current != NULL && current->pid != pid) /*searches list for matching pid*/
	{
		prev = current;
		current = current->next;
	}
	
	if (current== NULL) return -1;
	

	stop = current;
	setUpperBit(&stop->flags, 29, 0);
	setUpperBit(&stop->flags, 28, 1);
	stop->next = sQ->head;
	sQ->head = stop;
	prev->next = current->next;
	sQ->count +=1;
	rQ->count -=1;	
	return 0;

}

/* Move the process with matching pid from Stopped to Ready.
 * Change its State to Ready.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_continue(Schedule *schedule, int pid) { /*almost identical to stop but does the opposite*/

	Queue *rQ = schedule->ready_queue;
	Queue *sQ = schedule->stopped_queue;
 	Process *current = sQ->head; 
	Process * prev = NULL;
	Process * ready = (Process*) malloc(sizeof(Process));
	if (ready == NULL) return -1;
	if (current != NULL && current->pid == pid)
	{
	//	printf("ready here\n");
		sQ->head = current->next;
		ready = current;
		setUpperBit(&ready->flags, 28, 0);
		setUpperBit(&ready->flags, 29, 1);
		ready->next = rQ->head;
		rQ->head = ready;
		rQ->count +=1;
		sQ->count -=1;
		return 0;
	}
	while (current != NULL && current->pid != pid)
	{
		prev = current;
		current = current->next;
	}
	
	if (current== NULL) return -1;
	

	ready = current;
	setUpperBit(&ready->flags, 28, 0);
	setUpperBit(&ready->flags, 29, 1);
	ready->next = rQ->head;
	rQ->head = ready;
	prev->next = current->next;
	rQ->count +=1;
	sQ->count -=1;	
	return 0;

}

/* Remove the process with matching pid from Defunct.
 [2;2Rlow the specification for this function.
 * Returns its exit code (from flags) on success or a -1 on any error.
 */
int scheduler_reap(Schedule *schedule, int pid) {
	int exit = 0;
	Queue *q = schedule->defunct_queue;
	Process *current = q->head;
	Process *prev = NULL;
	int c = q->count;
	int i = 0;
	for(i = 0; i < c; i++)
	{
		if (current->pid == pid)
		{
			if (prev != NULL)  /*checks whether or not we are at the head*/
			{
				q->count -=1;
				setUpperBit(&current->flags, 26, 1);
				setUpperBit(&current->flags, 27, 0);
				exit = current->flags & 0x3FFFFFF;
				prev->next = current->next;
				current = NULL;

				free(current); free(prev);
				return exit;
			}
			else
			{
				q->count -=1;
				setUpperBit(&current->flags, 26, 1);
				setUpperBit(&current->flags, 27, 0);
				exit =  current->flags & 0x3FFFFFF;	
				current = NULL;
				q->head = NULL;
				free(current); free(prev); 
				
				return exit;
	
			}
		}
	prev = current;
	current = current->next;	
	}
  return -1; /*return should have happened before it gets here so if it gets here it's an error*/
}

/* Create a new Process with the given information.
 * - Malloc and copy the command string, don't just assign it!
 * Set the CREATED state flag.
 * If is_sudo, also set the SUDO flag.
 * Follow the specification for this function.
 * Returns the Process on success or a NULL on any error.
 */
Process *scheduler_generate(char *command, int pid, int base_priority, 
                            int time_remaining, int is_sudo) {
	Process *newP = (Process*)malloc(sizeof(Process));          /*there's a memory leak here that I can't figure out how to fix*/         
	if (newP == NULL) return NULL;  
	newP->command = malloc(sizeof(command));
	if (newP->command == NULL) return NULL;
	strcpy(newP->command, command);
	newP->pid = pid;
	newP->base_priority = base_priority;
	newP->time_remaining = time_remaining;
	if (is_sudo) newP->flags = 0xC0000000;             /*these hex numbers pertain to specific flag values that we want*/
	else newP->flags = 0x40000000;
 	return newP;
}

/* Select the next process to run from Ready Queue.
 * Follow the specification for this function.
 * Returns the process selected or NULL if none available or on any errors.
 */
Process *scheduler_select(Schedule *schedule) 
{
	if (schedule == NULL || schedule->ready_queue->head == NULL || scheduler_count(schedule->ready_queue) < 1) return NULL;

	Queue * q = schedule->ready_queue;
	Process *priority = (Process*) malloc(sizeof(Process));
	if (priority == NULL) return NULL;
	Process *current = q->head;
	Process *prev = NULL;
	int lowest = 5000;
	int c = q->count;
	int i = 0;
	for ( i= 0; i < c; i++)
	{
		if (current->cur_priority < lowest)   /*goes through entire list to get lowest value for cur_priority and saves it to lowest*/
		{
				lowest = current->cur_priority;
		}
		if (current-> next != NULL)	current = current->next;
	}
	current = q->head;

	for (i = 0; i < c; i++)                            /*goes through list again and stops on the the first process that matches lowest*/
	{
		if (current->cur_priority == lowest)
		{
			
			priority = current; 		/*sets process as the one to return*/
			if (prev != NULL)	prev->next = current->next;
	
			else q->head = current->next;
			
		break;
		}
		if (current->next != NULL)
		{
			prev = current;
			current = current->next;
		}
	}
	q->count -= 1;
	priority->cur_priority = priority->base_priority; /*changes cur to base priority as outlined in specs*/
	c = q->count;
	if (c > 0) 
	{
		current = q->head;
		for (i = 0; i < c; i++)                 /*goes through list one last time to decrement each cur_priority*/
		{
			current->cur_priority -= 1;
			current = current->next;		
		}
	}

	return priority;
}

/* Returns the number of items in a given Linked List (Queue) (Queue)
 * Follow the specification for this function.
 * Returns the count of the Linked List, or -1 on any errors.
 */
int scheduler_count(Queue *ll) {
//	printf("scheduler_count\n");
	if (ll == NULL) return -1;
 	return ll->count;
}

/* Completely frees all allocated memory in the scheduler
 * Follow the specification for this function.
 */
void scheduler_free(Schedule *scheduler) {
//	printf("scheduler_free\n");
	Queue * q1 = scheduler->ready_queue;
	Queue * q2 = scheduler->stopped_queue;
	Queue * q3 = scheduler->defunct_queue;
//	Process * f = q1->head;                      
	Process * f2 = q2->head;
	Process * f3 = q3->head;
	Process * next = NULL;
	int c1 = q1->count;
	int c2 = q2->count;
	int c3 = q3->count;
	while (c1 > 0)                                /*goes through all the queues and frees the processes */
	{		
		if (next != NULL)  /*checks to see if we're at the end of the list*/
		{
			next = q1->head->next;   
			free(q1->head->command);
			free(q1->head);
			//f = NULL;
			q1->head = next;
		}
		else free(q1->head);
		c1--;
	}
	while (c2 > 0)
	{
		if (next != NULL) 
		{
		next = f2->next;
		free(f2->command);
		free(f2);
		f2 = next;
		}
		else free(f2);
		c2--;
	}
	while (c3 > 0)
	{
		if (next != NULL) 
		{
		next = f3->next;
		free(f3->command);
		free(f3);
		f3 = next;
		}
		else free(f3);
		
		c3--;
	}
//	free(q2->head); free(q3->head);
	free(q1); free(q2); free(q3);
	free(scheduler);




	
 	return;
}

int getBit(int b0, int n)  /*returns the bit the specified index */
{
	int bit = (b0 >> n) & 1;
	return bit;
}

void setUpperBit(int * b0, int pos, int bit)  /*function I made to help with the bitwise operations. only sets the top bits that we need to edit in the specs */
{
	switch (pos)
	{
		case 31:
			if (bit == 0)
			{
				*b0 &= 0x7FFFFFFF;
			}
			else *b0 |= 0x80000000;
			break;
		case 30:
		if (bit == 0)
			{
				*b0 &= 0xBFFFFFFF;
			}
			else *b0 |= 0x40000000;
			break;
		
		case 29:
		if (bit == 0)
			{
				*b0 &= 0xDFFFFFFF;
			}
			else *b0 |= 0x20000000;
			break;
		
		case 28:
		if (bit == 0)
			{
				*b0 &= 0xEFFFFFFF;
			}
			else *b0 |= 0x10000000;
			break;
		
		case 27:
			if (bit == 0)
			{
				*b0 &= 0x7FFFFFF;
			}
			else *b0 |= 0x8000000;
			break;
				
		case 26:
		if (bit == 0)
			{
				*b0 &= 0xBFFFFFF;
			}
			else *b0 |= 0x4000000;
			break;
		default:
			fprintf(stderr, "Error in setUpperBit()...\n");
		


	}
}
