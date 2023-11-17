import java.util.ArrayList;

public class CardHand extends ArrayList<Card> {
    CardDeck leftDeck;
    CardDeck rightDeck;

    public CardHand(ArrayList<Card> cards) {
        this.addAll(cards);
    }

    public boolean handEqual(){
        int first = this.get(0).getValue();
        for (int i = 1; i < this.size(); i++) {
            if (this.get(i).getValue() != first) {
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

    public int bestCardToRemove(int userID){
        int cardToRemove = this.stream().filter(x -> x.getValue() != userID)
                                            .map(n -> n.getRoundCount())
                                            .max(Integer::compare)
                                            .orElse(null);
        return cardToRemove;
    }

    public void addToRightDeck(int index) {
        rightDeck.add(this.remove(index));
    }

    public Card removeFromLeftDeck() {
        Card newCard = leftDeck.remove();
        newCard.resetRoundCount();
        this.add(newCard);
        return newCard;
    }

    public CardDeck getLeftDeck(){
        return leftDeck;
    }
    public String toString(){
        return (this.get(0) + " " + this.get(1) + " " + this.get(2) + " " + this.get(3));
    }
}