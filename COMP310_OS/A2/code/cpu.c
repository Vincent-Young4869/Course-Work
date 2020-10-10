//
//  cpu.c
//  
//
//  Created by vincent young on 2020-02-22.
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
#include <string.h>

struct CPU *cpu;

int runCPU(int quanta){
    
    int status = 0;
    
    for(int i=0;i<quanta;i++){
        strcpy(cpu->IR, getIns(cpu->IP));
	//printf("the cpu-IR is %s\n",cpu->IR);////////////////////
        if(strcmp(cpu->IR, "quit") != 0){
	    //printf("the cpu-IR is %s\n",cpu->IR);////////////////////
            interpret(cpu->IR);
        }
        else{
            printf("Bye!\n");
            status = -1;
            break;
        }
	cpu->IP = cpu->IP + 1;
    }
    
    return status;
}

void initCPU(){
    cpu = (struct CPU *)malloc(sizeof(struct CPU));
    cpu->IP = -1;
    //char IR[1000];
    cpu->quanta = 2;
}

int getQuanta(){
    return cpu->quanta;
}

int getIP(){
    return cpu->IP;
}

void setQuanta(int q){
    cpu->quanta = q;
}

void setIP(int pc){
    cpu->IP = pc;
}



