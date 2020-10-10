#include<stdio.h>
#include<stdlib.h>
#include "shell.h"
#include "pcb.h"
#include "ram.h"
#include "cpu.h"
#include "interpreter.h"
#include "memorymanager.h"

/*
This is a node in the Ready Queue implemented as 
a linked list.
A node holds a PCB pointer and a pointer to the next node.
PCB: PCB
next: next node
*/
struct ReadyQueueNode {
    struct PCB* PCB;
    struct ReadyQueueNode *next;
};

struct ReadyQueueNode* head = NULL;
struct ReadyQueueNode* tail = NULL;
int sizeOfQueue = 0;

void boot()
{
    raminit();
    memoryInit();
    system("rm -r BackingStore");
    system("mkdir BackingStore");
}

int kernel(){
    return shellUI();
}

int main(int argc, char const *argv[])
{
    int error=0;
    boot();
    error = kernel(); 
    return error;
}
/*
Adds a pcb to the tail of the linked list
*/
void addToReady(struct PCB* pcb) {
    struct ReadyQueueNode *newNode = (struct ReadyQueueNode *)malloc(sizeof(struct ReadyQueueNode));
    newNode->PCB = pcb;
    newNode->next = NULL;
    if (head == NULL){
        head = newNode;
        tail = newNode;
    } else {
        tail->next = newNode;
        tail = newNode;
    }
    sizeOfQueue++;
}

/*
Returns the size of the queue
*/
int size(){
    return sizeOfQueue;
}

/*
Pops the pcb at the head of the linked list.
pop will cause an error if linkedlist is empty.
Always check size of queue using size()
*/
struct PCB* pop(){
    struct PCB* topNode = head->PCB;
    struct ReadyQueueNode * temp = head;
    if (head == tail){
        head = NULL;
        tail = NULL;
    } else {
        head = head->next;
    }
    free(temp);
    sizeOfQueue--;
    return topNode;
}

/*
Passes a filename
Opens the file, copies the content in the RAM.
Creates a PCB for that program.
Adds the PCB on the ready queue.
Return an errorCode:
ERRORCODE 0 : NO ERROR
ERRORCODE -3 : SCRIPT NOT FOUND
ERRORCODE -5 : NOT ENOUGH RAM (EXEC)
*/
int myinit(struct PCB *pcb){
    if (pcb != NULL) {
		addToReady(pcb);
		return 1;
	}

	return 0;
}
int victimCleared(struct PCB *p, int frameNo) {
	int i;

	for(i=0;i<10 && p->pageTable[i]!=frameNo;i++);
	if (i==10) return 0;

	p->pageTable[i] = -1;
	return 1;
}

void clearVictimFrame(int frameNo) {
	struct ReadyQueueNode* node = head;
	struct PCB *temp;

	while(node!=NULL) {
		temp = node->PCB;
		if (victimCleared(temp,frameNo)) 
			return;
		node = node->next;
	}
}

int scheduler(){
    // set CPU quanta to default, IP to -1, IR = NULL
    int termin;
    initCPU();
    while (size() != 0){
        //pop head of queue
        struct PCB *pcb = pop();
        //copy PC of PCB to IP of CPU
        setCPU(pcb->PC,pcb->PC_offset);
        
        int errorCode = runCPU(2);
        pcb->PC = getCPUpc();
		pcb->PC_offset = getCPUoffset();
        
		if (pcb->PC_offset == 4){
			termin = pageFault(pcb);
		} 

        if ( errorCode == -2||  termin == 2){
            terminator(pcb);
            free(pcb);
        } else {
            addToReady(pcb);
        }
        
    }
    // reset RAM
    resetRAM();
    return 0;
}

/*
Flushes every pcb off the ready queue in the case of a load error
*/
void emptyReadyQueue(){
    while (head!=NULL){
        struct ReadyQueueNode * temp = head;
        head = head->next;
        free(temp->PCB);
        free(temp);
    }
    sizeOfQueue =0;
}

