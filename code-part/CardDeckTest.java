import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.*;

/** 
 * CardDeckTest is a set of tests aimed at testing the CardDeck class functionality.
 * 
 */ 
public class CardDeckTest {
    Card card1;
    Card card2;
    Card card3;
    Card card4;
    ArrayList<Card> decklist;
    CardDeck cardDeck;

    @Before
    public void setUp(){
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        card4 = new Card(4);
        decklist = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        cardDeck = new CardDeck(decklist, 1);
    }
    @After
    public void tearDown(){
        card1 = null;
        card2 = null;
        card3 = null;
        card4 = null;
        decklist = null;
        cardDeck = null;
    }

    /**
     * This test makes sure that a CardDeck is instantiated properly by reading the output file.
     */
    @Test
    public void cardDeckInstantiatonTest(){
        // Finding and deleting the output file to ensure there is none in the file system.
        File logFile = new File("deck1_output.txt");
        logFile.delete();
        cardDeck = new CardDeck(decklist, 1);

        // Reading the cardDeck output file to ensure that a file has been created.
        Assert.assertTrue(logFile.exists());
    }

    /**
     * This test makes sure that toString() gives us the expected value from our CardDeck.
     */
    @Test
    public void toStringTest(){
        // Asserting that toString() gives us the expected value when data exists
        Assert.assertEquals("1 2 3 4 ", cardDeck.toString());
        
        // ..and when data does not exist.
        cardDeck = new CardDeck(new ArrayList<>(), 1);
        Assert.assertEquals("", cardDeck.toString());
    }

    /**
     * This test makes sure that printLogFile accurately prints the data stored inside the CardDeck class.
     */
    @Test
    public void printLogFileTest(){
        cardDeck.printLogFile();

        // Reading output to ensure deck1_output.txt is exactly as we expect
        File file = new File("deck1_output.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String output = reader.readLine();
            Assert.assertEquals("deck1 contents: 1 2 3 4 ", output);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
