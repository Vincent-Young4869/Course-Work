//
//  kernel.h
//  
//
//  Created by vincent young on 2020-02-22.
//

#ifndef kernel_h
#define kernel_h

int myInit(char *filename);
int scheduler(void);
void TaskSwitch(void);
//void addToReady(PCB *p);
void removeFromReady(void);
int emptyQ();

#endif /* kernel_h */
