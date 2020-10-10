//
//  pcb.h
//  
//
//  Created by vincent young on 2020-02-22.
//

#ifndef pcb_h
#define pcb_h

#include <stdio.h>

typedef struct PCB{
    int PC;
    int start;
    int end;
}PCB;

// slightly change the attributes to add ram index
PCB* makePCB(int start, int end);

#endif /* pcb_h */
