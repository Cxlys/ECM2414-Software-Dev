import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * PlayerTest is a set of tests that ensures that our threads properly function, even when given
 * extraneous inputs.
 */
@Category({ThreadTests.class})
public class PlayerTest {
    static InputStream stdin = System.in;
    static PrintStream stdout = System.out;

    static ByteArrayOutputStream byteArrayOutputStream;

    @BeforeClass
    public static void setUpClass() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
    }

    @After
    public void tearDown() {
        try {
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            fail("IOException thrown");
        }

        CardGame.resetAllVariables();
    }

    @AfterClass
    public static void tearDownClass() {
        System.setIn(stdin);
        System.setOut(stdout);
    }

    @Before
    public void setUp() {
        System.setIn(new ByteArrayInputStream("4\ninput_testing.txt\n".getBytes()));
        try {
            CardGame.main(new String[0]);

            // If you reach the bottom of this method, JUnit just gets bored and forcibly exits our threads.
            // We wait here to avoid that happening. The number is arbitrary.
            Thread.sleep(250);
            
        } catch (InterruptedException e) {
            
        }
    }

    /**
     * This method checks that the correct card (2) was chosen to be discarded by the player 1. 
     */
    @Test
    public void bestCardToRemoveTest() {
        File file = new File("player1_output.txt");
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            fail("File was never created.");
        }

        String output = "";
        try {
            output = reader.readLine() + reader.readLine() + reader.readLine();
        } catch (IOException e) {
            fail ("IOException thrown");
        }

        // Checking that player correctly discards the 2
        Assert.assertEquals("player1 initial hand 1 2 3 4player1 draws a 1 from deck 1player1 discards a 2 to deck 2", output);
    }

    /**
     * This method checks that after the game has been finished, only one player wins.
     */
    @Test
    public void onlyOnePlayerWinsTest() {
        // Instantiating file objects
        File file1 = new File("player1_output.txt");
        File file2 = new File("player2_output.txt");
        File file3 = new File("player3_output.txt");
        File file4 = new File("player4_output.txt");

        File[] fs = {file1, file2, file3, file4};
        Scanner scanner = null;
        String winningPlayer = null;

        ArrayList<File> files = new ArrayList<>(Arrays.asList(fs));

        // Looping through files, looking for "has won".
        for (File file : files) {
            try {
                scanner = new Scanner(file1);
                String playerLine = scanner.findInLine("has won");
                
                // If we find "has won" in this file, but "has won" has already been found, we fail the test.
                if (playerLine != null) {
                    if (winningPlayer != null) fail("More than one player won the game!");
                    winningPlayer = playerLine.substring(0, 7);
                }

                scanner.close();
            } catch (FileNotFoundException e) {
                fail("File not found on one of the scanners");
            }
        }
    }
}
