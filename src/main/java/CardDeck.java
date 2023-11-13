import java.util.ArrayList;
import java.util.Stack;

public class CardDeck extends Stack<Card> implements CardGroup {
    public CardDeck (ArrayList<Card> deckList){
        for (int i = 0; i < deckList.size(); i++) {
            this.push(deckList.get(i));
        }
    }
}