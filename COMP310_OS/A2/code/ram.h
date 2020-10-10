//
//  ram.h
//  
//
//  Created by vincent young on 2020-02-22.
//

#ifndef ram_h
#define ram_h

#include <stdio.h>

void initRam(void);
void addToRAM(FILE *p, int *start, int *end);
//int addToRAM(char* filename, int *start, int *end);
void removeRam(int *start,int *end);
char* getIns(int pc);

#endif /* ram_h */
