//
//  shellmemory.c
//  COMP310_A1
//
//  Created by vincent young on 2020-01-20.
//  Copyright © 2020 vincent young. All rights reserved.
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "interpreter.h"
#include "shell.h"
#include "shellmemory.h"

typedef struct nodes{
    char var[100];
    char value[100];
}array;
array *shellmem ;

int used = 0;

int matchVar(char *string);
//int expand(char *string);

void createShellmem(){
    shellmem = (array *)calloc(1000,sizeof(array));
}

void freeShellmem(){
    free(shellmem);
}


void setVar(char *str, char *value) {
    
    int i = matchVar(str);
    
    if (i<used){
        strcpy(shellmem[i].value, value);
    }
//    else if (i>=1000){
//        expand(str);
//    }
    else{
        strcpy(shellmem[i].var, str);
        strcpy(shellmem[i].value, value);
        used = used + 1;
    }
    printf("set %s = %s successfully\n",str,value);

    return;
}

void printVar(char *var){
    int i = matchVar(var);

    if (i<used)
        printf("%s = %s\n",shellmem[i].var, shellmem[i].value);
    else
        printf("No such variable is defined.\n");
    
    return;
}

int matchVar(char *string){
    int i = 0;
    while(i<used && strcmp(shellmem[i].var,string)!=0 && i<1000)
        i++;
    
    return i;
}

//int expand(char *string){
//
//    retun 0;
//}
