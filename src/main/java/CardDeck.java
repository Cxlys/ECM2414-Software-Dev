public class CardDeck extends CardGroup{
    public CardDeck (ArrayList<int> deckList){
        super(deckList)
    }

    public void removeTop(){
        deckList.remove(0)
    }

    public void addTop(int newBottomCard){
        deckList.add(newBottomCard)
    }

    public int returnTopCard(){
        return decklist.get(0)
    }

}