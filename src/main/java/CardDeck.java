import java.util.ArrayList;
import java.util.Stack;

public class CardDeck {
    Stack<Card> cards = new Stack<>();

    public CardDeck (ArrayList<Card> cards){
        for (Card card : cards) {
            this.cards.push(card);
        }
    }
    public CardDeck (Stack<Card> cards) {
        this.cards = cards;
    }

    public synchronized Card push(Card card) {
        return cards.push(card);
    }

    public synchronized Card pop() {
        return cards.pop();
    }
 }