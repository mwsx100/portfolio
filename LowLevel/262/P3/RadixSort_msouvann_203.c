/*Matthew Souvannaphandhu G01187346
 *  CS262 Lab Section 203
 *  Project 3
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct ListNode
{ 
	int data;
	struct ListNode* next;
	struct ListNode* self;
/*	struct ListNode* head;*/
} ListNode;

ListNode *newList(void); /*returns a head of new empty list*/
ListNode *removeNode(ListNode * prev); /*remove the node after prev from the list, and returns a pointer to the removed node*/
ListNode *insertNode(ListNode *prev, int num); /*inserts a new node with data field data after prev and returns a pointer to the new node*/
int length(ListNode *head); /*number of elements in the list*/
void printList(ListNode *head); /*print the data fields for the entire list*/
void setData(ListNode *node, int num);/*not used*/
int getDigits(int n); /*gets the n amount of digits from max rng value*/
int getnPlace(int num, int nPlace); /*retrieves the number from the nth place*/
int makeListUnsorted(ListNode *head, int numVals, int lowVal, int highVal); /*generates an unsorted list using given parameters and returns the largest rng value*/
void printRadArr(ListNode *radArr); /*prints the bucket list*/
void radix(ListNode *head, ListNode *radArr, int digits, int numVals); /*the radix sort algorithm*/
void walkList(ListNode ** list, ListNode *head); /*goes through buckets and adds values to a list*/
void deleteList(ListNode ** n); 
void clearRadArr(ListNode **radArr); /*supposed to clear radix array but couldt figure it out in time*/
void initializeRad(ListNode **radArr); /*initializes radix array*/
ListNode* stitch(ListNode *radArr); /*stiches bucket lists together into one list*/
void deleteRad(struct ListNode* rad);
void clearRadArr2(ListNode *radArr);

int main(int argc, char *argv[])
{
	ListNode* list = newList();
	ListNode* radArr = NULL;
	int i, rngSeed, numVals, lowVal, highVal, max = 0;
	if (argc != 5)
	{
		fprintf(stderr, "Wrong amount of arguments\n");
		exit(0);
	}
	rngSeed =atoi(argv[1]);
	numVals =atoi(argv[2]);
	lowVal =atoi(argv[3]);
	highVal =atoi(argv[4]);
	srandom(rngSeed);
	radArr = (ListNode*) malloc(sizeof(ListNode) * 10);
	initializeRad(&radArr);
	max = makeListUnsorted(list, numVals, lowVal, highVal);
	printList(list);

	for(i = 1; i <= getDigits(max); i++)
	{
		radix(list,radArr, i, numVals);
		printf("\n");	
		printRadArr(radArr);
		list = stitch(radArr);
		printf("S%d:\n", i-1);
		printList(list);
/*		clearRadArr(&radArr);*/
		clearRadArr2(radArr);
		radArr = (ListNode*) malloc(sizeof(ListNode) * 10);	
	/*	initializeRad(&radArr);*/
	/*	deleteList(&list);
		list = newList();*/
	}
	clearRadArr2(radArr);
/*	free(radArr);*/
	deleteList(&list);
	return 0;
}

void deleteRad(ListNode *rad)
{
	ListNode* current = malloc(sizeof(ListNode));

	if (rad == NULL) return;
	while (current != NULL)
	{
		current = rad;
		rad = rad->next;
		free(current);
		current = NULL;
	}
}
ListNode *insertNode(ListNode *prev, int num)
{

	ListNode* nuNode = NULL;
	nuNode = (ListNode*) malloc(sizeof(ListNode));
	(*nuNode).data = num;
	(*prev).next = nuNode;	
	
	return nuNode;
}
int makeListUnsorted(ListNode *head, int numVals, int lowVal, int highVal)
{
	int i, rng, max = 0;
	ListNode* current = NULL;
	ListNode* og = current;

	/*current = (ListNode*) malloc(sizeof(ListNode));*/

	current = head;

	for(i = 0; i < numVals; i++)
	{
		rng = (rand() % (highVal - lowVal + 1)) + lowVal;
		if (rng > max)
		{
			max = rng;
		}
		insertNode(current, rng);
		current = current->next;
	}
	deleteList(&og);
	return max;
}

void deleteList(ListNode ** n)
{
	ListNode* current = *n;
	ListNode* next;
	while (current != NULL)
	{
		next = current->next;
	/*	printf("%d i \n", current->data);*/
		free(current);
		current = next;
	}
	*n = NULL;
}

void deleteRadElem(ListNode *head)
{
	ListNode* current = head;
	ListNode* next;
	while ( current != NULL)
	{
		next = current->next;
		printf("%d\n", current->data);
		free(current);
		current = NULL;
		current = next;
	}
	head = NULL;
}

void clearRadArr(ListNode **radArr)
{
	int i = 0;
	for (i =0; i< 10; i++)
	{
		printf("%d\n", i);
		if (radArr[i] != NULL)
		{
			printf("here\n");
		/*	if (radArr[i]->next != NULL)	printf(" %d i = %d\n", radArr[i]->next->data, i);*/
			deleteList(&radArr[i]);
		
		}
	}
	free(radArr);
}


void clearRadArr2(ListNode *radArr)
{
	int i = 0;
	for (i =0; i< 10; i++)
	{
		if (&radArr[i] != NULL)
		{
		/*	printf("here i = %d\n", i);*/
/*			printf("%d  i = %d\n", radArr[i].next->data, i);*/

			if (radArr[i].next != NULL)
			{
				deleteList(&radArr[i].self);
			/*	free(&radArr[i].self);*/
			}
	/*		free(&radArr[i]);*/
			/*deleteRadElem(&radArr[i]);*/

		}
	}
	free(radArr);
	radArr = NULL;
}




void initializeRad(ListNode **radArr)
{
	int i = 0;
	for(i = 0; i < 10; i++)
	{
	/*	radArr[i] = NULL;*/
		(*radArr)[i] = *newList();
	}
	
}
void radix(ListNode *head, ListNode *radArr, int digits, int numVals)
{
	ListNode* current[10] = {0}; /*points to location in radArr*/
	int j, i, n = 0;
	head = head->next;
	for (i = 0; i < 10; i++)
	{
		current[i] = &radArr[i];	
	}	
	
	for (j=0; j< numVals; j++)
	{
		i = digits;
		n = getnPlace(head->data, i-1);
		current[n] = insertNode(current[n], head->data);
		if (j == numVals - 1) break;
		if (head->next!=NULL) 
		{
			head = head->next;
		}
	}
	
}

void printRadArr(ListNode *radArr)
{
	int i = 0;
	for (i=0; i<10; i++)
	{
		if(&radArr[i]!=NULL) 
		{
			printf("Bucket%d:\n", i);
			printList(&radArr[i]);
			printf("\n");
		}
	}
}

ListNode* stitch(ListNode *radArr)
{
	int i = 0;
	ListNode* list = NULL;
	ListNode* og = NULL;
	list = newList();
	og = list;
	for (i = 0; i < 10; i++)
	{
		walkList(&list, &radArr[i]);
	}
	list = og;
	return list;	
}

void walkList(ListNode ** list, ListNode *head)
{
	if (head == NULL)
	{
		printf("List is null.\n");
	}
	head = head->next;
	while (head!=NULL)
	{
		*list = insertNode(*list, head->data);
		head = head->next;
	}
}

void printList(ListNode *head)
{
	ListNode* h = head;
	if (h == NULL)
	{
		printf("List is null.\n");
	}
	h = h->next;
	while (h!=NULL)
	{
		fprintf(stdout, "%d\n", h->data);
		h = h->next;
	}
} 

void setData(ListNode *node, int num)
{
	(*node).data = num;
}

ListNode *newList(void)
{
	ListNode* nList = NULL;
	
	nList =(ListNode*) malloc(sizeof(ListNode));
	nList->self = nList;
	return nList;
}

int getDigits(int n)
{
	int c = 0; /*count*/
	do
	{
		c++;
		n /= 10;
	}
	while(n != 0);
	return c;
}

int getnPlace(int num, int nPlace)
{
	int div = 0;
	div = (int) pow(10, nPlace);
	return (num / div) % 10;	
}
