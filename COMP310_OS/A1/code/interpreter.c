//
//  interpreter.c
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

int script(char *words[]);
int help();
int quit();
int run(char *words[]);
int set(char *input[]);
int print(char *input[]);


int interpreter(char *words[]){
    
    int errCode = 0;
    
    if( strcmp(words[0], "help")==0 ){
        errCode = help(words);
    }
    else if ( strcmp(words[0], "quit")==0 ){
        errCode = quit(words);
    }
    else if ( strcmp(words[0], "set")==0 ){
        errCode = set(words);
    }
    else if ( strcmp(words[0], "print")==0 ){
        errCode = print(words);
    }
    else if ( strcmp(words[0], "run")==0 ){
        errCode = run(words);
    }
    else if ( strcmp(words[0], "jieshu*^%#!\0")==0 ){
        errCode = -3;
    }
    else {
        errCode = -1;
    }
    
    return errCode;
}

int run(char *words[]){
    
    int errCode = 0;
    char line[100];

    int i = 1;
    
    while( strcmp(words[i],"jieshu*^%#!\0")!=0 ){
        i++;
    }
    
    if(i==1){
        printf("lack of parameters, expected one.\n");
        return -3;
    }
    else if(i>2){
        printf("too many parameters, expected one\n");
        return -3;
    }
    else{
        
        //printf("enter run function\n");
        //printf("file name is %s\n",words[1]);
        
        FILE *fp = fopen(words[1],"rt");
        
        //printf("fopen pass\n");
        
        if(fp == NULL){
            printf("fail opening file\n");
            char* words[100];
            words[0] = strdup("quit\0");
            words[1] = strdup("jieshu*^%#!\0");
            quit(words);
            return -3;
        }
        else{
            fgets(line,999,fp);
            //line[strlen(line)-2] = '\0';
            //printf("first line scanned: %s\n",line);
            //int k=1;
            
            while(!feof(fp)){
                //printf("the %d-th line passed,errorCode=%d, userInput=%s\n",k,errCode,line);
                //printf("lenth of input = %lu\n",strlen(line));
                
                errCode = parserInput(line);
                
                if (errCode == -2){
                    fclose(fp);
                    return -3;
                }
                else if( errCode != 0){
                    fclose(fp);
                    return errCode;
                }

                fgets(line,999,fp);
                //line[strlen(line)-2] = '\0';
                //k++;
            }
        }
        
        fclose(fp);
    }
    
    return errCode;
}

int help(char *input[]){
    int i = 0;
    
    while( strcmp(input[i],"jieshu*^%#!\0")!=0){
        i++;
    }
    
    if(i==1){
        printf("COMMAND                  DESCRIPTION\n");
        printf("help                     Displays all the commands\n");
        printf("quit                     Exits / terminates the shell with “Bye!”\n");
        printf("set VAR STRING           Assigns a value to shell memory \n");
        printf("print VAR                Displays the STRING assigned to VAR\n");
        printf("run SCRIPT.TXT           Executes the file SCRIPT.TXT\n");
        return 0;
    }
    else{
        printf("too many parameters, expected one\n");
        
        return -3;
    }
}

int quit(char *input[]){
    int i = 0;
    
    while( strcmp(input[i],"jieshu*^%#!\0")!=0 ){
        i++;
    }
    
    if(i==1){
        printf("Bye!\n");
        //freeShellmem();
        return -2;
    }
    else{
        printf("too many parameters, expected one\n");
        
        return -3;
    }
}

int set(char *input[]){
    //printf("set not implemented yet\n");
    char *var,*value;
    int i = 1;
    
    while( strcmp(input[i],"jieshu*^%#!\0")!=0 ){
        if(i==1){
            var = strdup(input[1]);
        }
        else if(i==2){
            value = strdup(input[2]);
        }
        else{
            value = strcat(value, " ");
            value = strcat(value, input[i]);
        }
        i++;
    }
    
    if(i==1 || i==2){
        printf("lack of parameters, expected two.\n");
        return -3;
    }
    else{
        setVar(var, value);
    }
    
    return 0;
}

int print(char *input[]){
    //printf("print not implemented yet\n");
    char *var;
    int i = 1;
    
    while( strcmp(input[i],"jieshu*^%#!\0")!=0 ){
        i++;
    }
    
    if(i==1){
        printf("lack of parameters, expected one.\n");
        return -3;
    }
    else if(i>2){
        printf("too many parameters, expected one\n");
        
        //printf("%s %s\n",input[1],input[2]);
        
        return -3;
    }
    else{
        var = strdup(input[1]);
        printVar(var);
    }
    
    return 0;
}


