//
//  main.c
//  COMP310_A1
//
//  Created by vincent young on 2020-01-20.
//  Copyright © 2020 vincent young. All rights reserved.
//

#include <stdio.h>
#include <string.h>
#include "interpreter.h"
#include "shellmemory.h"

int parserInput(char ui[]);

int main() {
    // insert code here...
    //char prompt[100] = {'$','\0'};
    char userInput[1000];
    int errorCode = 0;
    
    createShellmem();
    
    printf("Welcome to the <Yichao Yang> shell!\n");
    printf("Version 1.0 Created January 2020\n");
    
    //int i=0;
    
    while(1){
        
        printf("$");
        fgets(userInput, 999, stdin);
        
        //printf("loop %d: userInput = %s\n",i,userInput);
        
        errorCode = parserInput(userInput);
        if (errorCode == -1){
            printf("Unknown command\n");
            continue;
        }
        else if (errorCode == -2){
            freeShellmem();
            break;
        }
        else if(errorCode == -3){
            continue;
        }
        //i++;
    }
    return 0;
}

int parserInput(char ui[]){
    
    int a,b;
    int w = 0;
    char temp[200];
    char *words[100];
    
    for(a=0; ui[a]==' ' && a<1000; a++);
    
    while(ui[a]!='\0' && a<1000){
        
        for(b=0; ui[a]!='\0' && ui[a]!=' ' && a<1000; a++,b++){
            if(ui[a]=='\n' || ui[a]=='\r'){
                ui[a] = '\0';
                break;
            }
            else{
                temp[b] = ui[a];
            }
        }
        
        temp[b] = '\0';
        words[w] = strdup(temp);
        
        if(ui[a]=='\0'){
            if(strlen(words[w])==0){
                a++;
            }
            else{
                w++;
                break;
            }
        }
        else if(strlen(words[w])==0){
            a++;
        }
        else{
            a++;
            w++;
        }
        
    }
    words[w] = strdup("jieshu*^%#!\0");
    
    return interpreter(words);
}
