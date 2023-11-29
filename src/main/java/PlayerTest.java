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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

@Category({ThreadTests.class})
public class PlayerTest {
    BufferedReader reader;
    Player player;

    static InputStream stdin = System.in;
    static PrintStream stdout = System.out;

    static String outputText;

    @BeforeClass
    public static void setUpClass() {
        System.setIn(new ByteArrayInputStream("4\ninput_testing.txt\n".getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        try {
            CardGame.main(new String[0]);

            // If you reach the bottom of this method, JUnit just gets bored and forcibly exits our threads.
            // We wait here to avoid that happening. The number is arbitrary.
            Thread.sleep(250);

        } catch (InterruptedException e) {
            // Ah, the consequences of my actions
        }

        outputText = byteArrayOutputStream.toString();
        
        System.setIn(stdin);
        System.setOut(stdout);
    }

    @Test
    public void handleCardDrawTest() {
        
    }
}
