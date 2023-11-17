import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class CardDeck extends LinkedBlockingQueue<Card>{
    int index;
    
    public CardDeck (ArrayList<Card> cards, int index){
        super(cards);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
 }