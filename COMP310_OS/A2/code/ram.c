//
//  ram.c
//  
//
//  Created by vincent young on 2020-02-22.
//

#include "kernel.h"
#include "ram.h"
#include "pcb.h"
#include "cpu.h"

#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <stdlib.h>

char *ram[1000];
int nextStart;


 void initRam(){
     nextStart = 0;
 }
 

void addToRAM(FILE *p, int *start, int *end){
    
    char buffer[1000];
    *start = nextStart;

    fgets(buffer,999,p);
        //printf("the buffer is %s\n",buffer);//////////////////////////////
        for(int temp = 0;temp < strlen(buffer);temp++){
            if(buffer[temp] == '\n' || buffer[temp] == '\r'){
                buffer[temp] = '\0';
                break;
            }
        }
    
    while(!feof(p)){
        ram[nextStart] = strdup(buffer);
        
        fgets(buffer,999,p);
        //printf("the buffer is %s\n",buffer);///////////////////////////
        for(int temp = 0;temp < strlen(buffer);temp++){
            if(buffer[temp] == '\n' || buffer[temp] == '\r'){
                buffer[temp] = '\0';
                break;
            }
        }
        
        nextStart++;
    }
    //printf("Complete: read buffer\n");////////////////////////
    //ram[nextStart] = "zhongyan";
    *end = nextStart - 1;
    //nextStart++;
    //printf("Complete: add to ram\n");//////////////////////
    
    return;
}

void removeRam(int *start,int *end){
    
    for(int i = *start;i<=*end;i++){
        ram[i] = NULL;
    }
    
    return;
}


 char* getIns(int pc){
     return ram[pc];
 }
 

