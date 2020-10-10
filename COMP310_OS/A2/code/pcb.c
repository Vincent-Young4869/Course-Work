//
//  pcb.c
//  
//
//  Created by vincent young on 2020-02-22.
//

#include "pcb.h"
#include <stdio.h>
#include <stdlib.h>

/*
 PCB* makePCB(int start, int end){
     
     
 }
 */

PCB* makePCB(int start, int end){
    PCB *pcb = malloc(sizeof(PCB));
    pcb->PC = start;
    pcb->start = start;
    pcb->end = end;
    return pcb;
}
