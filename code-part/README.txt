To whom it may concern,

TestSuite is our testing suite for our program.

In order to run this test suite, please click the test button next to the TestSuite class definition on your favourite IDE. 
We used VSCode, but IntelliJ works as well, and I would assume so does Eclipse.

In the case that you don't have access to an IDE, you can also try cd'ing into this folder, and run the following command from terminal:
java -cp "./lib/junit-4.13.2.jar; ./lib/hamcrest-core-1.3.jar; ." org.junit.runner.JUnitCore TestSuite