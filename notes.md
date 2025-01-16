# My Notes

Check out the example code for each of the sections

## Java Fundamentals

Similar to C++. References, not pointers. Compiler vs interpreter : compilers define code instructions for a system and then go to the system, interpreters go to system first before defining code instructions. Compilers are fast but less portable (need multiple copies of the code for different platforms/systems) while interpreters are very portable. Java is a hybrid. code is compiled for a virtual machine (most common denominator for a machine) which is then further defined for each individual machine's required.

`MyCLass.java` = source file. `MyClass.class` = executable file

`public static void main(String [] args)` - executable main method function/class. (String ... args). Any main function in a class is runnable by the program/IDE

1 file per class

Compile and then run: `javac program.java \n  java program`

### Javadoc

look up javadox 23 api

/**

This will be a javadoc comment attached to Myclass. First sentence is summary. @param references the parameters, @return references the return statement

*/

`public class Myclass {}`

### data types

* byte
* short
* int
* long
* float - `float var = 2.5f;` the f is absolutely required for compilation
* double
* char
* boolean

`long long1 = 10L` - the l encodes the long as a 64 bit number

`System.out.println(var1 + ", " + var2);` This is string contatenation. a newline is automatically included

`System.out.printf("$d, %d\n", var1, var2);` This is a formatted string. Each method is viable, but may prove easier in different ways. printf can specify decimal point length with `%.2f` allowing only 2 decimal places of the float to be printed

unicode characters possible in Java

### Strings

String to int ~ `int Integer.parseInt(String value)`

`String s = "Hello";` or `String s = new String("Hello");`. Strings are immutable so changed strings are simply new copies, but using the first one enables Java to not create redundant instances by storing it to a table.

Concatenation like python is possible, but so is formating: `String s3 = String.format("%s %s", s1, s2);`

Methods:

* int length
* char charAt
* String trim
* boolean startsWith(String)
* int indexOf(int) - can be given a single char instead
* int indexOf(String)
* String substring(int) - returns the string at index int to end
* String substring(int, int)

Keep in mind special characters (\n, \t, etc.)

### Phase 0

schedule, due date, assumes repo creation (done).

ignore chessgame class implementation. shared/src/main/java/chess. code to pass the tests

ChessPosition class. `private int row;` `private int col;` this is ahead of the constructor. these create instance variables usable by the methods of the class

inside the constructor

`this.row = row;
this.col = col;`

then just return row and column as required

pieceMoves(board, position) - this method requires a board and a position. find the piece at that position on that board, then find all the legal moves.

ChessBoard - use a matrix (array of arrays)

addpeice - allows testing of proper board creation
