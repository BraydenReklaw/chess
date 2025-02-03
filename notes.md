# My Notes

Check out the example code for each of the sections

programing exam - setup is identical to the first time setup. do it all again. make sure tests run (they will fail) before
you start running honorlock. Pass/Fail - based on if 100% of tests pass

## Java Fundamentals

Similar to C++. References, not pointers. Compiler vs interpreter : compilers define code instructions for a system and then go to the system, interpreters go to system first before defining code instructions. Compilers are fast but less portable (need multiple copies of the code for different platforms/systems) while interpreters are very portable. Java is a hybrid. code is compiled for a virtual machine (most common denominator for a machine) which is then further defined for each individual machine's required.

`MyCLass.java` = source file. `MyClass.class` = executable file

`public static void main(String [] args)` - executable main method function/class. (String ... args). Any main function in a class is runnable by the program/IDE

1 file per class

Compile and then run: `javac program.java \n  java program`

collection - like an array, also an interface

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

Methods (not inclusive):

* int length
* char charAt
* String trim
* boolean startsWith(String)
* int indexOf(int) - can be given a single char instead
* int indexOf(String)
* String substring(int) - returns the string at index int to end
* String substring(int, int)

Keep in mind special characters (\n, \t, etc.). examine the use cases of `\b`

string concatenation inefficient in large numbers, especially in a loop. Use stringbuilder instead: `StringBuilder builder = new StringBuilder(); builder.append("Using "); builder.append("stuff); String str = builder.tostring();`

### Arrays

check out example code

there is a difference between declaring and creating `int [] intArray; intArray = new int[10];` this creates an int array with size 10

`int [] intArray2 = {2, 7, 8}` does both at once and initializes as well

2 ways to iterate:

`for(int i = 0; i < intArray.length; i++)`

`for(int value : intArray)`

// multidimensional arrays //

tictactoe: `char[][] tictactoeboard = new char[3][3];` this creates an array of size 3 holding arrays of size 3. the 2nd 3 need not be present

`tictactoeboard[1][2] = 'X';`

`for(char[] row : tictactoeboard){for(char value : row) {;};}`

### Commandline Arguments

the main class will almost always have an array of arguments given to it which can be accessed and iterated over. you can edit what arguments are passed in
and used by running main once and then accessing the configuration editor under the file name in upper right by the debug and run buttons.

### Packages, Imports and CLASSPATH

Packages - organize classes. ex: `java.util.Date` a package that contains date utility functions/classes. Note the `package chess` in all chess files.

Import -  kind of like include. Allows shorthand references to packages

CLASSPATH - environment variable. the starting point for directory traversal amidst program operations

### Phase 0

schedule, due date, assumes repo creation (done).

ignore chessgame class implementation. shared/src/main/java/chess. code to pass the tests

ChessPosition class. `private int row;` `private int col;` this is ahead of the constructor. these create instance variables usable by the methods of the class

inside the constructor

`this.row = row;
this.col = col;`

then just return row and column as required

pieceMoves(board, position) - this method requires a board and a position. find the piece at that position on that board, then find all the legal moves in associated separate classes.

ChessBoard - use a matrix (array of arrays)

addpeice - allows testing of proper board creation

Interface - away of implementing pieceMoves. holds methods, but no bodies. Look at class example of pieceMoves implementation(mind the class name is different)

## Objects and Classes

Note the slide examples and lecture videos

reference equality vs object equality. References equal if referencing same object. object equality by default checks reference equality

## Records

records are immutable versions of classes, so if you want to change a variable, you need to create a method
that returns a new instance with the new variables.

## Exceptions

abnormal conditions, not necessarily errors. Allow us to write code without handling all the error logic required.
Errors are issues that no code can fix. Exceptions can be fixed with code

try / catch block : try { code that may throw exception }catch(exception) {code that handles specified exception}

Multi-catch -> allows catch to | multiple exceptions in one block

Checked exceptions (Java only). Handle or Declare rules apply here

finally - handles any code necessary to run but in risk of not running if an exception is thrown. Will always run
regardless of thrown exceptions

`throw new <exeption name>("error message")`

You can create custom exception classes

## Collections

only stores objects. used when regular arrays[] not sufficient. Made up of sub-interfaces:
* list - index accessible
* set - all elements unique `add(val), contains(val), remove(val)`
* Queue - holds elements for processing `add(val), peek(), remove()`
* Deque (deck) - Queue not fifo `addFirst(val), addLast(val)` as well as peek and remove first/last
* Stack - deprecated (unusable now due to age and inefficiency). Use a Deque
* Map - Python Dictionary of key-value pairs `put(key,val), get(key), keySet(), values()`

`Set<String> words; for(String w : words) {}`

### Equality checking

by default, Objects.equals compares by identity (is this at same address as that?). 

Comparable v Comparator

## Copying Objects

Used in chess game when determining checkmate by testing all available moves. 
Clone the board, apply a move, check for mate, toss the board

deep copy will recursively copy object and all its references

you can only shallow copy immutable objects

see CopyingObjects example code, especially for cloning a collection

// when it comes to checking for check, upon making a move, calculate all possible moves again. 
if any include the opponent King space, set check. Use this move collection to assist in determining
if another intervening piece can block check //