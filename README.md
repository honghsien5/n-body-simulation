# N-body particle simulation

How to Compile the Program

Type "make all" in the command-line to compile.

Both versions of the program read in a file called init.txt, which is included
with the files.

If you wish to create your own init.txt file, ensure that you follow the
format of our included init.txt file, and have x and y coordinates of each body
separated on their own lines like this: "1.0 1.0"

To run the sequential program, type "Java nBody_Seq" followed by the command-line arguments:

Required Arguments:

arg1 = Number of bodies
arg2 = Size of the bodies
arg3 = Number of time steps

Optional Arugments:

arg4 = Type the size of a time step, or it will run with a time step of 500
arg5 = Type true to run with GUI, or it will run without it

To run the parallel program, type "Java nBody_Par" followed by the command-line arguments:

Required Arguments:

arg1 = Number of workers
arg2 = Number of bodies
arg3 = Size of the bodies
arg4 = Number of time steps

Optional Arugments:

arg5 = Type the size of a time step, or it will run with a time step of 500
arg6 = Type true to run with GUI, or it will run without it

Examples of command-lines we used in testing:

Java nBody_Seq 2 5 10000000 1000 false
Java nBody_Par 2 2 5 10000000 1000 true

In addition:

'make optimization' command would compile the programs with the -O flag
'make clean' would clean up any generated .class files

You can also run a testing python script by doing:

python project1_test.py
