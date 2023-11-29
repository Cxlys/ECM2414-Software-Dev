import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.*;

public class CardDeckTest {
    Card card1;
    Card card2;
    Card card3;
    Card card4;
    ArrayList<Card> decklist;
    CardDeck carddeck;

    @Before
    public void setUp(){
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        card4 = new Card(4);
        decklist = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        carddeck = new CardDeck(decklist, 1);
    }
    @After
    public void tearDown(){
        card1 = null;
        card2 = null;
        card3 = null;
        card4 = null;
        decklist = null;
        carddeck = null;
    }

    @Test
    public void CardDeckInstantiatonTest(){
        File logFile = new File("deck1_output.txt");
        logFile.delete();
        carddeck=null;
        carddeck = new CardDeck(decklist, 1);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String output = reader.readLine();
            Assert.assertEquals(null, output);
        } catch (IOException e) {
            Assert.fail();
        }try (FileWriter writer = new FileWriter(logFile)){
            writer.write("a");
        } catch (IOException e) {
            Assert.fail();
        }
        carddeck=null;
        carddeck = new CardDeck(decklist, 1);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String output = reader.readLine();
            Assert.assertEquals(null, output);
        } catch (IOException e) {
            Assert.fail();
        }
    }
    
    @Test
    public void getIndexTest(){
        Assert.assertEquals(1, carddeck.getIndex());
    }
    @Test
    public void toStringTest(){
        Assert.assertEquals("1 2 3 4 ", carddeck.toString());
        carddeck = null;
        carddeck = new CardDeck(new ArrayList<>(), 1);
        Assert.assertEquals("", carddeck.toString());
    }
    @Test
    public void printLogFileTest(){
        carddeck.printLogFile();
        File file = new File("deck1_output.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String output = reader.readLine();
            Assert.assertEquals("deck1 contents: 1 2 3 4 ", output);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
