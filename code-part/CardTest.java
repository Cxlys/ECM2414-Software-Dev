import org.junit.*;

/** 
 * CardDeckTest is a set of tests aimed at testing the Card class functionality.
 * 
 */ 
public class CardTest {
    private Card card;

    @Before
    public void setUp(){
        card = new Card(1);
    }

    @After
    public void tearDown(){
        card = null;
    }

    @Test
    public void getValueTest(){
        Assert.assertEquals(1, card.getValue());
    }

    @Test
    public void getRoundCountTest(){
        Assert.assertEquals(0, card.getRoundCount());
    }

    @Test
    public void incrementRoundCountTest(){
        card.incrementRoundCount();
        Assert.assertEquals(1, card.getRoundCount());
    }
    
    @Test
    public void resetRoundCount(){
        card.incrementRoundCount();
        card.resetRoundCount();
        Assert.assertEquals(0, card.getRoundCount());
    }
}
