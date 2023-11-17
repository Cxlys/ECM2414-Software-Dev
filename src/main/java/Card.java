public class Card {
    int value;
    int roundCount = 0;

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void incrementRoundCount() {
        roundCount++;
    }

    public void resetRoundCount() {
        roundCount = 0;
    }

    public int getRoundCount(){
        return roundCount;
    }
}
