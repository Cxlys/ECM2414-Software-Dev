import java.util.ArrayList;
import CardGroup;
import CardDeck;
import CardHand;

public class Cards {
    private ArrayList<CardGroup> listOfCards = new ArrayList<>();

    public Cards(ArrayList<int> listOfCards){
        this.listOfCards = listOfCards;
    }

    public int getCard(int cardNo){
        return listOfCards[cardNo]
    }

}
