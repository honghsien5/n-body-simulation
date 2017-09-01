# N-body particle simulation

## How to Compile the Program

Run `make all` in the command-line to compile.

Both versions of the program read in a file called init.txt, which is included
with the files.

If you wish to create your own init.txt file, ensure that you follow the
format of the included init.txt file, and have x and y coordinates of each body
separated on their own lines like this: "1.0 1.0"

## How to run sequential version
Execute `Java nBody_Seq` followed by the command-line arguments:

### Required Arguments:

* arg1 = Number of bodies

* arg2 = Size of the bodies

* arg3 = Number of time steps

### Optional Arugments:

* arg4 = Type the size of a time step, or it will run with a time step of 500

* arg5 = Type true to run with GUI, or it will run without it

### Example
```
Java nBody_Par 2 2 5 10000000 1000 true
```
## How to run parallel version
Execute `Java nBody_Par` followed by the command-line arguments:

### Required Arguments:

* arg1 = Number of workers

* arg2 = Number of bodies

* arg3 = Size of the bodies

* arg4 = Number of time steps

### Optional Arugments:

* arg5 = Size of a time step. Default 500

* arg6 = Enabling GUI mode if this argument is true.

### Example
```
Java nBody_Seq 2 5 10000000 1000 false
```
## Additional notes:

`make optimization` command would compile the programs with the -O flag

`make clean` would clean up any generated .class files

You can also run a testing python script by executing:
```
python project1_test.py
```

Contributors
--------------

Shien Hong			honghsien5@gmail.com

License
--------------

	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
