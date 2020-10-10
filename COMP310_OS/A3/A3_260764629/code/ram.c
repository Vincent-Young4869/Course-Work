#include<stdio.h>
#include<string.h>
#include<stdlib.h>
/*
Stores the index of the next available block of cell
*/
int nextFree = 0;

/*
Ram structure implemented as an array.
Size is 1000 strings
*/
char *ram[40]; 

void raminit(){
    int i = 0;
    for(i=0;i<40;i++){
        ram[i] = NULL;
    }
}

/*
This function will delete the content in ram between the 2 indices parameters (inclusive)
start : pointer to variable which will store the index of first line of file in ram
end : pointer to variable which will store the index of last line of file in ram
*/
void removeFromRam (int start, int end){
    for (int i = start; i <= end; i++)
    {
        ram[i] = NULL;
    }
}

/*
This function will add the content of a file to the local ram array variable
In the case of an error (due to lack of RAM), -1 will be assigned to
the values pointed by start and end. Use this to check for errors.
p : file pointer
start : pointer to variable which will store the index of first line of file in ram
end : pointer to variable which will store the index of last line of file in ram
*/
void addToRAM (FILE *p,int frameNumber){
    int i;
    char buffer[1000];
    for(i=0;i<4&&!feof(p);i++){
            fgets(buffer,999,p);
            ram[frameNumber*4+i]= strdup(buffer);
    }
}

/*
Reset the pointer to the free cell back to index 0
*/
void resetRAM(){
    nextFree = 0;
    int i;
    for (i =0 ;i<40;i++){
    ram[i] = NULL;
    }
}
int findSpace(){
    int i;
    for(i = 0;i<10;i++){
        if (ram[4*i] == NULL){
            return i;
        } 
    }
    return -1;
}
char* ChoosenIns(int IP){
    return ram[IP];
}


