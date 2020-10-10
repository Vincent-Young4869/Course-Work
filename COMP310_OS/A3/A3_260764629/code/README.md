OS: macOS (linux system)

Compile: bash compile.sh (will run the command ./mykernel < TESTFILE.txt)
run: ./mykernel < TESTFILE.txt

Clarification: 
1. makefile attached to recompile and rebuild
2. all the txt file in backing store will be deleted after the exec command,
   but the  directory will remain and only get recreated in boot(). 
   If you want to see the actual file creation and deletion, 
   then you have to modify the code or use debug tool to see it when it's running.
3. testfiles provided:
     script1.txt 
     script2.txt 
     script3.txt 
     TESTFILE.txt
4. Due to the run() in cpu.c and the run() in interprter.c share the same name, the RUN function in cpu.c is renamed as "runCPU()"

Note:
if any error comes up try to execute the bash file again (the bash file is totally working on my server side) or simply execute "gcc -c *.c" and "gcc -o mykernel *.o".
