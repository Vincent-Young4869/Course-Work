#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include"shell.h"
#include"pcb.h"
#include"ram.h"
#include"cpu.h"
#include"interpreter.h"
#include"kernel.h"

int numberOfPCB;
struct PCB *PCBs[10];
char *fileNames[10];

void memoryInit(){
	numberOfPCB = 0;
    int i;
    for(i=0;i<10;i++){
        fileNames[i] = NULL;
    }
}

int findPCBs(struct PCB *p){
	int i;
	for (i = 0;i<numberOfPCB;i++){
		if (PCBs[i] == p){
			return i;
		}
	}
	return 0;
}
void terminator(struct PCB *pcb){
	char command[100];
	int index = findPCBs(pcb);
	strcpy(command,"rm -f ");
	strcat(command,fileNames[index]);
	system(command);
}

int countTotalPages(FILE *f) {
	int lines = 0;
    int page = 0;
    char ch;

    while ((ch = getc(f)) != EOF){
        if (ch == '\n')
            lines++;
    }
    if (lines == 0) return -1;
    page = ceil(lines/4.0);
    return page;
}

int findFrame(){
	return findSpace();
}
int findVictim(struct PCB *p){
	int r = rand() % 10;
    for (int i=0; i<10; i++){
        if (p->pageTable[i] == r){
            break;
        }
        else if (i == 9){
            return r;   // this slot doesn't belong to this pcb
        }
    }
    // if this one is in use by this pcb
    for (int i=1; i<10; i++){
        r = (r+i)%10;
        for (int i=0; i<10; i++){
            if (p->pageTable[i] == r){
                break;
            }
            else if (i == 9){
                return r;   // this slot doesn't belong to this pcb
            }
        }
    }
    return -1;
}

int updatePageTable(struct PCB *p, int pageNumber, int frameNumber, int victimFrame){
	if (frameNumber == -1) {
		p->pageTable[pageNumber]=victimFrame;
		clearVictimFrame(victimFrame);
	} else {
		p->pageTable[pageNumber]=frameNumber;
	}

	return 0;
}
void loadPage(int pageNumber, FILE *f, int frameNumber){
	int count = 0;
	char buffer[1000];
	int i;
	rewind(f);
	do {
		if (count/4 == pageNumber){
			break;
		}
		fgets(buffer,999,f);

		count++;
	} while(!feof(f));
	addToRAM (f,frameNumber);	
}
int pageFault(struct PCB *pcb) {
	int nextFrame;
	int result;
	FILE *temp;

	// set veriables to get next page
	pcb->PC_page++;
	
	pcb->PC_offset = 0;

	// check to see if there is another page
	if (pcb->PC_page < 10) {
		nextFrame = pcb->pageTable[pcb->PC_page];

		if (nextFrame != -1) return 0; // page already loaded
		if (nextFrame == -1 && pcb->PC_page >= pcb->pages_max) 
			return 2; // end of program
		else if (nextFrame == -1) { // load next page
			temp = fopen(fileNames[findPCBs(pcb)],"rt");
			result = findFrame();
			if (result == -1) {
				result = findVictim(pcb); 
				if (result == -1) return -1;
				loadPage(pcb->PC_page, temp, result);
				updatePageTable(pcb,pcb->PC_page , -1, result);
			}else{
				loadPage(pcb->PC_page, temp, result);
				updatePageTable(pcb,pcb->PC_page, result, -1);
			}
			fclose(temp);
		} else return 4; // unexpected error
		pcb->PC = pcb->pageTable[pcb->PC_page]*4;
	}
	else
		return 1; // error, no more pages

	return 0; // loaded successfully
}

int launcher(FILE *p) {
	char buffer[1000];
	char ch;
	char backName[100];
	char filename[1000] = "BackingStore/";
	int  frameNum, victimNum;

    backName[0] = numberOfPCB / 10 + 48;
    backName[1] = numberOfPCB % 10 + 48;
    backName[2] = '\0';
	strcat(filename,backName);

	FILE* target = fopen(filename,"w");
	if (target == NULL) return -1;
    while((ch = fgetc(p)) != EOF){
        fputc(ch,target);
    }
    fclose(p);
    fclose(target);
    
    FILE* fp = fopen(filename,"r");
    if (fp == NULL) return -1;
    int pageNum = countTotalPages(fp);
    
    if(pageNum <= 0){
        printf("FIle has no page\n");
        return 0;
    }
	
	struct PCB *newPCB;
	newPCB = makePCB(pageNum);

	PCBs[numberOfPCB] = newPCB;
	fileNames[numberOfPCB] = strdup(filename);
	numberOfPCB = numberOfPCB + 1;

	int limit;
    if(pageNum > 1){
        limit = 2;
    }else{
        limit = 1;
    }

	for(int i=0;i<limit;i++) {
		frameNum  = findFrame();
		victimNum = -1;

		if (frameNum != -1){
			loadPage(i, fp, frameNum);
		}else{
			victimNum = findVictim(newPCB);
			if (victimNum == -1){
				return -1;
			}else{
				loadPage(i, fp, victimNum);
			}
		}
		updatePageTable(newPCB, i, frameNum, victimNum);
	}

	setPCB(newPCB);
	myinit(newPCB);
	fclose(fp);
	return 1;
}
