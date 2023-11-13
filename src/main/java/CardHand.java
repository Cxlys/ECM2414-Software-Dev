public class CardHand extends CardGroup{
    public CardHand (ArrayList<int> deckList){
        super(deckList)
    }

    public boolean handEqual(){
        int firstCard = deckList.get(0);
        for (int i = 1; i < deckList.size(); i++) {
            if (deckList.get(i) != firstCard) {
                return false;
            }
        }
        return true; 
    }


}