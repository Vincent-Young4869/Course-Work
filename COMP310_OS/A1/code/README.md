OS: mimi.cs.mcgill.ca

run: ./mysh < mytest.txt

Clarification: 
1. for the command "set var value", this shell inlcudes everything after the variable. For example, "set x Montreal Canada" give "x=Montreal Canada".
2. if your commands have too many parameters or lack of parameter(s), the shell will give error message.
3. empty line will be ignored.
4. when runing test file in the shell(use RUN command), when receiving "quit" command, the shell will exit the RUN command, close the file and remain in the shell waiting for next command line.
5. If inputting "quit" command in command line, it will directly exit the shell.
