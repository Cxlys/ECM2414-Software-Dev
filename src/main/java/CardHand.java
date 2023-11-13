import java.util.ArrayList;

public class CardHand extends ArrayList<Card> implements CardGroup {
    public CardHand (ArrayList<Card> deckList){
        this.addAll(deckList);
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
}