import java.util.ArrayList;

public class CardHand {
    CardDeck leftDeck;
    CardDeck rightDeck;
    ArrayList<Card> cards;

    public CardHand (ArrayList<Card> cards){
        this.cards = cards;
    }

    public boolean handEqual(){
        int first = cards.get(0).getValue();
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getValue() != first) {
                return false;
            }
        }
        return true; 
    }

    public void setLeftDeck(CardDeck leftNode) {
        this.leftDeck = leftNode;
    }

    public void setRightDeck(CardDeck rightNode) {
        this.rightDeck = rightNode;
    }

    public CardDeck getRightDeck() {
        return rightDeck;
    }

    public CardDeck getLeftDeck() {
        return leftDeck;
    }
}