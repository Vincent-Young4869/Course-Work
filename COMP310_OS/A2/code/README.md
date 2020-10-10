OS: mimi.cs.mcgill.ca

compile: ./compile.sh
run: ./mykernel < testfile.txt

Clarification: 
1. The CPU data structure is created in cpu.h
   The PCB data structure is created in pcb.h
   *ram[1000] is created in ram.c
   Ready Queue data structure and its related functions are in kernel.c
2. Due to the run() in cpu.c and the run() in interprter.c share the same name, the RUN function in cpu.c is renamed as "runCPU()"
