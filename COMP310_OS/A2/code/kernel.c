//
//  main.c
//  comp310_A2
//
//  Created by vincent young on 2020-02-22.
//  Copyright © 2020 vincent young. All rights reserved.
//
#include "kernel.h"
#include "ram.h"
#include "pcb.h"
#include "cpu.h"

#include "shell.h"
#include "interpreter.h"
#include "shellmemory.h"

#include <stdio.h>
#include <stdlib.h>

int myInit(char *filename);
int scheduler(void);
void TaskSwitch(void);
void addToReady(PCB *p);
void removeFromReady(void);

struct readyNode{
    struct PCB *pcb;
    struct readyNode *next;
};
struct readyNode *head, *tail;


int main(int argc, const char * argv[]) {
    printf("Kernel 1.0 loaded!\n");
    head = NULL;
    tail = NULL;
    
    initCPU();
    initRam();
    return shellUI();
}

int myInit(char *filename){
    
    FILE* fp = fopen(filename,"rt");
    if(fp==NULL){
        printf("Error: Script %s not found\n", filename);
        return 0;
    }
    
    int start = -1;
    int end = -1;
    //printf("adding to ram...\n");//////////////////////////////
    addToRAM(fp, &start, &end);
    //printf("Complete: add to ram\n");//////////////////////////////
    //out of range of ram handling
    
    PCB* pcb = makePCB(start, end);
    //printf("pcb created\n");//////////////////////////////
    addToReady(pcb);
    //printf("Complete:pcb add to ready queue\n");//////////////////////////////
    fclose(fp);
    
    return 0;
}

int scheduler(){
    int status = 0;
    int c = 1;
    while(head != NULL){
	//printf("the %dth sheduling\n",c);//////////////////////////////
        if(getIP() == -1){
            setIP(head->pcb->PC);
	    //printf("the %dth runCPU\n",c);////////////////////////////
            status = runCPU(getQuanta());
	    //printf("Complete the %dth runCPU\n",c);////////////////////////////
            setIP(-1);
            head->pcb->PC = head->pcb->PC + 2;
            if(status != 0){
                removeFromReady();
            }
            else{
                TaskSwitch();
            }
        }
	//printf("Complete the %dth sheduling\n",c);///////////////////////////////
    }
    
    return 0;
}

void TaskSwitch(){
    if(head == tail){
        return;
    }
    
    struct readyNode *temp = (struct readyNode*)malloc(sizeof(struct readyNode));
    temp = head;
    head = head->next;
    temp->next = NULL;
    tail->next = temp;
    tail = temp;
}


 void addToReady(PCB *p){
     
     struct readyNode *temp = (struct readyNode*)malloc(sizeof(struct readyNode));
     
     if(temp == NULL){
         printf("out of memory\n");
         return;
     }
     
     temp->pcb = p;
     temp->next = NULL;
     
     if(head == NULL){
         head = temp;
         tail = temp;
     }else{
         tail->next = temp;
         tail = temp;
     }
     
     return;
 }

void removeFromReady(){
    
    struct readyNode *temp = (struct readyNode*)malloc(sizeof(struct readyNode));
    
    if(temp == NULL){
        printf("out of memory\n");
        return;
    }
    
    temp = head;
    
    if(head == tail){
        head = NULL;
        tail = NULL;
        free(temp->pcb);
        free(temp);
        return;
    }
    head = head->next;
    free(temp->pcb);
    free(temp);
    
}

int emptyQ(){
    if(head == NULL){
        return 0;
    }
    return -1;
}
 

