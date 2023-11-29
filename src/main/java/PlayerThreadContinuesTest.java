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

@Category({ThreadTests.class})
public class PlayerThreadContinuesTest {
    Card c1;
    Card c2;
    Card c3;
    Card c4;
    Player player;

    BufferedReader reader;

    @Before
    public void setUpClass() {
        c1 = new Card(3);
        c2 = new Card(2);
        c3 = new Card(4);
        c4 = new Card(3);

        Card[] cs = {c1,c2,c3,c4};
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(cs));

        player = new Player(null, cards, 1);
        player.setLeftDeck(new CardDeck(cards, 1));
        player.setRightDeck(new CardDeck(cards, 2));
        player.run();

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
        } catch (IOException e) {
            e.printStackTrace();
            fail("Reader seems to have completely filled up the file system?");
        }
    }

    @Test
    public void correctCardPickedForRemovalTest() {
        fail();
    }
}
