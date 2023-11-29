import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.After;

import org.junit.Assert;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * PlayerWinsInstantlyTest is a set of tests created to ensure correct functionality in the case
 * that a player wins the game immediately.
 */
@Category({ThreadTests.class})
public class PlayerWinsInstantlyTest {
    Player player;
    BufferedReader reader;

    @Before
    public void setUpClass() {
        // Setting up the dummy Player object
        Card c1 = new Card(1);
        Card c2 = new Card(1);
        Card c3 = new Card(1);
        Card c4 = new Card(1);

        Card[] cs = {c1,c2,c3,c4};
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(cs));

        player = new Player(null, cards, 1);
        player.setLeftDeck(new CardDeck(cards, 1));
        player.setRightDeck(new CardDeck(cards, 2));
        player.run();

        // Attempting to read the file created by player.run() after a win condition was assumedly hit
        File logFileLocation = new File("player1_output.txt");

        try {
            reader = new BufferedReader(new FileReader(logFileLocation));
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    @After
    public void tearDown() {
        try {
            reader.close();
            reader = null;
        } catch (IOException e) {
            e.printStackTrace();
            fail("Reader seems to have completely filled up the file system?");
        }
    }

    /**
     * This test makes sure that the winning thread prints to a file properly.
     */
    @Test 
    public void winningThreadPrintsProperlyTest() {
        try {
            String str = reader.readLine();
            if (str == null || str == " ") fail("File completely empty."); 
        } catch (IOException e) {
            fail("IOException thrown.");
        }
    }

    /**
     * This test makes sure that the game ends as expected, with the player immediately winning.
     */
    @Test
    public void gameEndsAsExpectedTest() {
        String winString =  "player1 initial hand 1 1 1 1" +
                            "player1 wins" +
                            "player1 exits" +
                            "player1 final hand: 1 1 1 1";

        StringBuilder builder = new StringBuilder();

        try {
            for (int i = 0; i < 4; i++) {
                builder.append(reader.readLine());
            }
        } catch (IOException e) {
            fail("IOException thrown");
        }

        Assert.assertEquals(builder.toString(), winString);
    }

    /**
     * This test makes sure that toString works effectively, by testing the number output after "initial hand".
     */
    @Test
    public void toStringTest() {
        try {
            Assert.assertEquals("player1 initial hand 1 1 1 1", reader.readLine());
        } catch (IOException e) {
            fail("IOException failed");
        }
    }
}
