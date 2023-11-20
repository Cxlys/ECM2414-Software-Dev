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
        try {
            rightDeck.put(this.remove(index));
        } catch (InterruptedException e) { }
    }

    public Card removeFromLeftDeck() {
        try { 
            Card card = leftDeck.take();

            this.add(card);
            card.resetRoundCount();

            return card;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public CardDeck getLeftDeck() {
        return leftDeck;
    }

    public CardDeck getRightDeck() {
        return rightDeck;
    }

    @Override
    public String toString(){
        return (this.get(0).getValue() + " " + 
                this.get(1).getValue() + " " + 
                this.get(2).getValue() + " " + 
                this.get(3).getValue());
    }
}