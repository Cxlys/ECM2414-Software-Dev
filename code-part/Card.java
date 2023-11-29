/**
 * Card is a data class used to store data on the Cards used throughout the game.<br><br>
 * It contains variables used to hold face values and round counts, and methods
 * to allow for the updating of round count data.
 */
public class Card {
    final int value;
    int roundCount = 0;

    public Card(int value) {
        this.value = value;
    }

    /**
     * Returns the face value of the card.
     */
    public int getValue() {
        return value;
    }

    /**
     * Increments the counter for how long the card has been in a player's hand.
     */
    public void incrementRoundCount() {
        roundCount++;
    }
    
    /**
     * Resets the round count. Used when a card leaves a player's hand.
    */
    public void resetRoundCount() {
        roundCount = 0;
    }

    /**
     * Returns the card's round count.
     */
    public int getRoundCount(){
        return roundCount;
    }
}
