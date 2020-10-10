#include<string.h>
#include<stdlib.h>
#include"ram.h"
#include"interpreter.h"
#include"shell.h"

#define DEFAULT_QUANTA 2

struct CPU { 
    int IP; 
    char IR[1000]; 
    int quanta; 
    int offset;
};

struct CPU *mycpu;

void initCPU(){
	mycpu = (struct CPU *)malloc(sizeof(struct CPU));
	mycpu->IP = -1;
	mycpu->quanta = 2;
}

void setCPU(int PC,int offset){
	mycpu->IP = PC;
	mycpu->offset = offset;
}
/*
This method passes a quanta.
It will then executes quanta number of lines in RAM as from the address
pointed by the CPU IP.
Returns an errorCode.
*/
int runCPU(int quanta){
    // If a quanta greater than the default quanta of 2 is passed,
    // run will instead execute only default quanta of lines.
    char specific[100] = "quit";
    if (quanta > DEFAULT_QUANTA ){
        quanta = DEFAULT_QUANTA;
    }
    
    for (int i = 0; i < quanta; i++)
    {
        if(ChoosenIns(mycpu->IP) == NULL || strstr(ChoosenIns(mycpu->IP),specific) != NULL){
            return -2;
        }
        else{
            strcpy(mycpu->IR,ChoosenIns(mycpu->IP));
        }
        
        if(mycpu->IR[0] == '\0'){
            return -2;
        }
        int errorCode = parse(mycpu->IR);
        // Do error checking and if error, return error
        if (errorCode != 0){
            // Display error message if fatal
            return errorCode;
        }
        mycpu->IP ++;
        mycpu->offset ++;
    }
    return 0;
}

int getCPUpc() { return mycpu->IP; }


int getCPUoffset() { return mycpu->offset; }
