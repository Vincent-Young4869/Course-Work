#ifndef PCBH
#define PCBH
/*
PCB has 3 fields
PC : Points to the the current line reached for that program
start: Points to first line of that program
end: Points to last line of that program
*/
struct PCB
{
    int PC;
    int PC_page, PC_offset, pages_max;
    int pageTable[10];
};

/*
Passes 2 parameters (start , end)
This method creates a PCB with fields set as this:
PC = start
start = start
end = end
*/
struct PCB* makePCB(int totalPages);
void setPCB(struct PCB *pcb);
#endif