//
//  cpu.h
//  
//
//  Created by vincent young on 2020-02-22.
//

#ifndef cpu_h
#define cpu_h

#include <stdio.h>

struct CPU {
    int IP;
    char IR[1000];
    int quanta;
};

void initCPU(void);
int runCPU(int quanta);
int getIP(void);
int getQuanta(void);
int getIP(void);
void setQuanta(int q);
void setIP(int pc);
#endif /* cpu_h */
