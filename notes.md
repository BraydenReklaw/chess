# My Notes

## Java Fundamentals

Similar to C++. References, not pointers. Compiler vs interpreter : compilers define code instructions for a system and then go to the system, interpreters go to system first before defining code instructions. Compilers are fast but less portable (need multiple copies of the code for different platforms/systems) while interpreters are very portable. Java is a hybrid. code is compiled for a virtual machine (most common denominator for a machine) which is then further defined for each individual machine's required.

`MyCLass.java` = source file. `MyClass.class` = executable file

`public static void main(String [] args)` - executable main method function/class. (String ... args). 

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
