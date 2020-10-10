#!/bin/bash

rm *.o
gcc -c *.c
gcc -o mykernel *.o
./mykernel < TESTFILE.txt

