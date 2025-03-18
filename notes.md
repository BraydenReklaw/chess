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

## I/O

streams, scanner class (1 token at a time), files class, RandomAccessFile class

### File Class

This class can check a file's existence, create one, or delete one.

`File file = new File("/use/MyFile.txt"); if(file.exists());`

### Streams

input and output streams are bytes binary. reader and writer convert to strings. 
* FileInputStream
* PipedInputStream
* URLConnection.getInputStream() (phase 3)
* HttpExchange.getRequestBody()
* ResultSet.getBinaryStream(int col_index) - reading bytes off a database

Filter Input streams - allow the chaining of streams. allows for decompressing, decrypting, counting, etc.

## JSON and Serialization

Parseble string represenation that supports objects, arrays, numbers, strings, boolean, null.

{"name":"Bob","age":32} - object. [] array. See cd_catalogue.json example

Parsers:
*DOM - convert object to tree structure Document-Object-model. (think HTML and whatnot). traverse the tree to get data
*Stream - Tokenizer returning 1 token at a time from JSON data file
*Serializers/Deserializers - use library to conert JSON to java object. Use this in Project. use [this](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/json/json.md) Look at the sample code, especially parser/json/simpleobject.java

`private List<CD> parse(File file) throws IOException {try(FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {Gson gson = new Gson();
            Catalog catalog = gson.fromJson(bufferedReader, Catalog.class);
            return catalog.getCds();
        }
    }`

you'll need to make GSON available to your project. see instructions in slides

[Generators](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/json/example-code/generator/json/JsonSimpleObjectSerializationExample.java) convert from object to string. 

## Phase 2:

create a sequence diagram of phase 3. look especially at the sample diagram for Register and the starter diagram. Focus on the "happy path". if you can understand the error-less path, then great

Chess Client -> web -> Chess Server  (HTTP -> request, <- HTTP response). Server has the site and the API. Console Client will access the API and is how the game will actually be played

### Software Design Principles

*Single Responsibility Principle (SRP) - classes do only what they are intended to do, nothing more
*Non-duplicated code
*Encapsulation/Information Hiding - what you can hide you can change. Make things private when you can.

### Server

User records, Auth token - when you login, a random number is generated and tied to your credentials. while using the site, if authorization is required and you have the token, you need not log in again, Game records keep track of users on the game and references the ChessGame class.

Packages: Record classes (User, Authtoken, Game/Gamedata), Data access (Data Access Object) classes (UserDao, AuthTokenDao, GameDao) (will initiallily be internally stored), Service Classes (business logic) (User(register, login, logout), Game(create, join, list), Clear), Request/Result

The request objects are JSON strings that are GSON parsed. RegisterResult register(RegisterRequest r).

The main server class points to HTTP Handlers which point to the Request/Result and Service classes. Handlers will handle string/object JSON conversions. Look there for JSON issues. Look at Service classes for business logic errors

Clear function does not need a request class (it needs no extra data given to it to clear data)

IP addresses are tied to servers/machines, but there are muliple processes running on the machine that are willing to talk to any import, so ports are assigned to each process. IP and port are like phone number and extension.

## HTTP

### GET

request data

URL: protocol, domain address, port number, path `https://www.google.com:443/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png`

method: `GET <path> HTTP/1.1 (http version)\n Accept: < >\n Accept-Endoding < >\n User-Agent <accepted browsers>`
The Accept lines are Headers and specify accepted meta data
The Response to the above GET would be the HTTP version, status code, Reason Phrase (OK, success, fail, etc.), the header information, an empty line, and then the response body containing what was requested.

[API call](https://docs.google.com/presentation/d/1VECGwiLgXd541yq9BWVSYiphHAXHG0ca/edit#slide=id.p1): (Follow link for slides and sample code)

URL: `http://macho.cs.byu.edu:7979/event/1234`

`GET /event/1234 HTTP/1.1 \n Authorization: 1gh2jdkj37` the response body would be a JSON String containing the event data

### POST

Give data to the server to do something

Post will replace GET with POST and also include a request body after a newline (we will send JSON strings). Response will be the same as above.

### cURL

commandline tool to experiment with HTTP endpoints and debug. Checkout the slides

## Phase 3

handlers require knowledge of generics and lambdas

## Quality Code & Style

### Style

Cohesion - methods in a class should do similar/related things. Names should properly describe the function of the method.

A method without a return should be a verb/verb phrase describing what is being done. A returning method should be a verb/verb phrase or what it returns. Avoid meaningless verbs or stuttering (person.copyPerson(Person person) vs person.clone). 

Establish naming conventions/methods for consistency

classes -> things. Methods -> Algorithms 

Algorithm Decomposition: long/complex methods can be hard to understand. Break apart into sub methods and sub-sub methods until the problem is trivial to solve. This allows recallable methods which cuts down on
duplicated code.

Comments: Well named and readable code is self-commenting. 

Avoid Code Duplication: callable code is easier to debug than duplicated code.

Deep Nesting: avoid nesting code within 3-4+ levels of nesting in while, for, if, etc. Do this by calling out to other methods (Probably should address this in PawnMoveCalculator)

Parameters: in, in-out, out. The parameter is used but not returned. The parameter is used, potentially changed and result is returned. The parameter passed in does not matter, only the result returned.

Initializing: Initialize variables when they are declared and close to where they are used. Always check the need to re-initialize a variable (like counters in a loop)

### Layout

Consistency in style is paramount in Human Readability. Follow standards when they exist, then follow your own.

White space (spaces, tabs, lines) enhances readability. Indentation directs logic structure.

Logic operators should be separated by lines `if ( a == b || \n a == c) {}` or collapsed into a method call. `if (isB(a) || isC(a)) {}` or `if (isA(a)) {} boolean isA(char a) {return (isB(a) || isC(a))}`

line wrap between 80-100 characters. watch readability standards

Psuedo-code: capture the overall logic, the what, before getting lost in the how of syntax. A Non programmer should be able to get what you are trying to do

Variable names: be careful of too long `NumberOfPeopleAtTheParty` or too short `n`. Short variables as loop control variables are fine, as well as `temp` and `x y z` in the instance of naturally short named variables as seen in math. 
Camel-case : `WebCrawler, documentMap` `Web_crawler, document_map`. First char of class name : uppercase. First char of method name: lowercase. First char of variable name: lowercase

abreviate only when you need to. Computer -> cmptr, Calculate -> calc. Be consistent

### Style Checking

some IDEs can reformat code to a certain standard. Checkstyle, Prettier

## Unit Testing

individual parts need to be verified before being integrated with other parts. If the system breaks after a part is added, then you know where the issue is. Untested code is broken code. "Unit" is a generic term for the smaller pieces of a program, most generally a class. Unit testing tests a unit in isolation.

Unit Tests, Integration tests, End-to-End tests. Faster to slower, less integration to more integration

let Intellij generate getters and setters for you

### Unit Tests

create, call, verify. Actual results vs Expected results. Automated testing = frequent testing which is best. Fast, Cohesive, Independent, Unique

test driver required - run, add and report tests

### JUnit Testing Framework

`@Test` `@BeforeEach` or `@BeforeAll` indicates code that is run before each test that is run. `@AfterEach/@AfterAll` runs after any test is run. Implement tests with `Assertions.assert*`

The class example WordExtractor will take a string and return each word in it. WordExtractorTest is the unit tests. Should probably ensure JUnit 5 or later is used

### Code Coverage

How do I know I've written enough tests? standard is 90% of code is run during testing. Doesn't affirm you tested every line, but un-run code is definitely untested. 

Coverages: Line, Statement, Branch (do you test the true and false case of each `if` statement?), Function

in intelliji, right click test, `more run/debug`, `run with coverage`. see slides for more examples of how to access

## Relational Databases

load driver, open database connection, start transaction, execute queries/updates, commit transaction, close connection. Dependency required [(see slides)](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/db-jdbc/db-jdbc.md). 
Follow the implementation examples in the slides for understanding of that process. Make sure to close every transaction. 
<b>Pay attention to the Execute a Query page</b>. 
Working with a results set is just about the only place where Java is 1-based indexed, not 0-based. The goal is to pull from database and put into array for handling in the code.
Watch out for SQL injection attacks. `String sql = "update book" + "set title = ?, author = ?" + "where id = ?"` helps prevent SQL injection. 
SQLexceptions result from users trying to access sections of Database without permission. Look at the example database for a [Booklist](https://docs.google.com/presentation/d/12XS7en64-oQYivKayGyNGueWphiL6mm5/edit#slide=id.p69)

## PHASE 4

Your code must create a database. Create new DAOs that run with JDBC instead with jdbc and sql. Alter the instantiations in server.java with the JDBC DAO. 
Maybe use a parent DAO class with a static initializer so the database is created when server starts up. 
DatabaseManager creates database, but not tables, so add createTables method with create user, auth and game if not exists. call createTables form createDatabase. 

The autograder will call and create its own database. make sure that you also call/create one yourself. DAO's should not be serializing/deserializing.

### MySQL

installation required (server and shell) `mysql -u [username] -p [password]` `use [database]` open a database. most commands require a ;. 
MySQL workbench (also download), a GUI for mySQL.
remember `grant` for permission handling and whatnot

LOOK AT SECURING PASSWORDS

## Phase 5

Draw Menus and handle Input, Draw Chess Board, Invoke ServerAPI endpoints, write tests. To test with mutliple instances of a server running at once, edit configurations, modify, allow multiple instances.
phase 6 will implement socket so cross updating is usable. 
Play Game and Observe should just draw the board for now.

if login option is selected, create loginRequest object, pass to login() which should probably return a login result. because register returns the same, loginresult can be used here as well.
Think of what your handlers do for Server. ServerFacade will need to call Server at some point.

Look at the example code for how to draw a board([TicTacToe](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/console-ui/example-code/src/ui/TicTacToe.java)) and other console output stuff

Suggested file structure: 
Client inside UI draws menu. ChessBoard inside UI draws chessboard. Client depends on chessboard. This allows logic separation. ServerFacade would contain the server call methods, called by UI Client.
ServerFacade would then call Client Communicator, which would actually make server and HTTP calls.

Consider studying enum logic for determining if I am drawing a white or black board

### menus

No strict design Guidelines, just need to make sure it is readable and usable. Think back to cs110. display options, handle specific input. Don't explicitly show game ids, authtokens, etc.

If authtoken = null, not logged in. Use this a semi-boolean flag. use a scanner to wait for and accept input. 

Play Game and Observe Game should draw a board (The only ones to do so).

### ChessBoard

Your color should be "closest" to you (black on bottom if you are black player). reorientate board as necessary for this.

Draw row by row. Call methods, not 1 long logic map. Set background color of \n to desired color (like white), because rest of line will be that color.
Consider logic that checks if piece present on a space if not. consider creating a matrix of pieces. Start with a main method that will be tossed out later. Client will call from menu eventually, but
the main method will allow you to run and render as you test.

Try using unicode chess characters instead of letters. You'll want to set Terminal font to Monospace and use Em Space (file - settings - editor - colorScheme - console font)

### Server from Client

Client -> web -> Server HTTP request -> <- HTTP response. connection required. Use Streams (JSON strings). GSON conversion
required for class instances. See the Get and Post examples from slides. Authorization Header required for most functions
and relevent endpoints. Headers are only necessary if you will be doing something with them.

trickiest thing is reading from the InputStream returned by connection. It must be read and then used as needed/required
In the instance of POST, you will need to build a request body to OutputStream after connect. See IO lecture for stream 
handling. Attach writer to stream and reader to writer

http://hostname:port/PATH - likely use localhost as hostname

server facade would have 7 methods for the 7 functions, and client communicator would do the actual GET POST HTTP stuff
