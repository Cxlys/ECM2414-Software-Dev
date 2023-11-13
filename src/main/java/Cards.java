import java.util.ArrayList;

public class Cards {
    ArrayList<Card> cards;
    ArrayList<CardGroup> groups;

    public Cards(ArrayList<Card> cards) {
        this.cards = cards;

        int groupCount = cards.size() / 4;

        for (int i = 0; i < groupCount; i++) {
            ArrayList<Card> buffer = new ArrayList<>();
            CardGroup group;

            for (int j = 0; j < 4; j++) {
                buffer.add(cards.get(i));
            }

            if (i < groupCount / 2) group = new CardDeck(buffer);
            else group = new CardHand(buffer);

            groups.add(group);
        }
    }
}
