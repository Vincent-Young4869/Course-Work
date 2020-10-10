
#include <stdio.h>
#include <stdlib.h>
#include "pcb.h"
/*
PCB has 3 fields
PC : Points to the the current line reached for that program
start: Points to first line of that program
end: Points to last line of that program
*/



/*
Passes 2 parameters (start , end)
This method creates a PCB with fields set as this:
PC = start
start = start
end = end
*/
struct PCB* makePCB(int totalPages){
    int i;
    struct PCB* pcb = (struct PCB*)malloc(sizeof(struct PCB));
    pcb->PC = 0;
    pcb->pages_max = totalPages ;
    pcb->PC_page = 0;
    pcb->PC_offset = 0; 
    for(i=0;i<10;i++) pcb->pageTable[i]=-1;
    return pcb;
}

void setPCB(struct PCB *pcb){
	pcb->PC = pcb->pageTable[0]*4;
}





