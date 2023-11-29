import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Player is a threading class that contains the main functionality for the game simulation.<br><br>
 * It contains all of the player logic used to determine what cards to discard, what cards to
 * draw, when or when not to wait for other threads, and what to do when a player wins or loses
 * the game.
 */
public class Player extends Thread {
    /**
     * CardHand is a nested class that is used to store data on the Player's current hand.<br><br>
     * It inherits from ArrayList, allowing us to directly consult the list itself when performing,
     * operations, and on top of this contains methods to make evaluating the hand easier.
     */
    class CardHand extends ArrayList<Card> {
        CardDeck leftDeck;
        CardDeck rightDeck;

        CardHand(ArrayList<Card> cards) {
            this.addAll(cards);
        }

        /**
         * A method to check whether the player's hand is all matching numbers.
         * 
         * @returns true if equal, else false
         */
        boolean handEqual(){
            int first = this.get(0).getValue();

            // Loop through our hand, check if all values are equal to the first.
            for (int i = 1; i < this.size(); i++) {
                if (this.get(i).getValue() != first) {
                    return false;
                }
            }
            return true; 
        }

        /**
         * Calculates the best card to remove, and returns it as an index.
         * 
         * @returns index of best card to remove
         */
        int bestCardToRemove(int userID){
            int maxRoundCount = -1;
            int maxIndex = 0;

            for (int i = 0; i < 4; i++) {
                Card card = this.get(i);
                if (card.getValue() != userID && card.getRoundCount() > maxRoundCount) {
                    maxRoundCount = card.getRoundCount();
                    maxIndex = i;
                }
            }

            return maxIndex;
        }

        /**
         * Atomic drawing of cards, done in one function to ensure that both are done at the 
         * same time. 
         */
        Card handleCardDraw(int cardToAdd) throws InterruptedException {
            /* Getting our card to put on the left.
             * If we don't do this up here, the InterruptedException thrown by take() gets 
             * thrown AFTER we've already discarded one of our cards, leaving us with an 
             * uneven hand and giving us an IndexOutOfBounds error. 
             * 
             * The InterruptedException only throws when there are no cards in the left deck, and
             * there never *will* be any cards in the left deck. Therefore, we can assume that
             * this means the game is already over, and we can exit.
             */
            Card card = leftDeck.take();

            // Adding card onto the right
            rightDeck.put(this.remove(cardToAdd));

            this.add(card);
            card.resetRoundCount();

            return card;
        }

        /**
         * A method to get the left Deck object.
         */
        CardDeck getLeftDeck() {
            return leftDeck;
        }

        /**
         * A method to get the right Deck object.
         */
        CardDeck getRightDeck() {
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

    final CardHand hand;
    final int denomination;
    final File file;

    static volatile Integer winningPlayerID = null;
    final StringBuilder gameUpdateStream = new StringBuilder();
    
    public Player(ThreadGroup group, ArrayList<Card> hand, int denomination) {
        // We call on super() here to allow for the use of a ThreadGroup.
        super(group, String.valueOf(denomination));

        this.hand = new CardHand(hand);
        this.denomination = denomination;

        // Attempting to instantiate file, if it doesn't already exist.
        this.file = new File("player" + denomination + "_output.txt");
        try {
            if (file.createNewFile()) {
                System.out.println("File has been created: " + file.getName());
            } 
            else {
                // If it does exist, we delete and create a new one to clear it.
                System.out.println(
                    "File already exists. " +
                    "Deleting and creating a new file."
                );
                file.delete(); file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error has occured.");
            e.printStackTrace();
        }

        gameUpdateStream.append(
            "player" + denomination + " initial hand " + 
            this.hand.toString() + "\n"
        );
    }

    /**
     * Our main functionality for the player thread, running our simulation by checking for winning game states and 
     * drawing/discarding cards.
     */
    @Override
    public void run() {
        try {
            for (;;) {
                // Checking to see if this player has won the game.
                synchronized (Player.class) {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    if (hand.handEqual()) {
                        this.handleGameWin();
                        break;
                    }
                }

                // Getting the cards we need to remove and take
                int removeIndex = hand.bestCardToRemove(denomination);
                Card removedCard = hand.get(removeIndex);
                Card addedCard = hand.handleCardDraw(removeIndex);

                // Logging to update stream
                gameUpdateStream.append("player" + denomination + " draws a " + addedCard.getValue() + " from deck " + denomination + "\n");
                gameUpdateStream.append("player" + denomination + " discards a " + removedCard.getValue() + " to deck " + ((denomination) % 4 + 1) + "\n");
                gameUpdateStream.append("player" + denomination + " current hand is " + hand.toString() + "\n");
    
                for (Card card : hand) card.incrementRoundCount();
            }
        } catch (InterruptedException e) {   
            this.handleGameLoss();
        }
    }

    /**
     * This method is used as a way for our testing suite to interact with the bestCardToRemove method.<br><br>
     * It is left private, even though it is never used, so as to retain the encapsulation granted by nesting.
     */
    int bestCardToRemove() {
        return hand.bestCardToRemove(denomination);
    }

    /**
     * This method prints the accurate logs for when a player loses the game, then prints them to
     * our external log.
     */
    void handleGameLoss() {
        gameUpdateStream.append(
            "player" + winningPlayerID + 
            " has informed player" + denomination + 
            " that player" + winningPlayerID + 
            " has won\n"
        );
        this.printToOutputFile();
    }

    /**
     * This method prints the accurate logs for when a player wins the game, then prints to our 
     * external log.
     * It calls interrupt() on the ThreadGroup connecting all of the other threads, making all others
     * stop and reach handleGameLoss()
     */
    void handleGameWin() {
        // We update winning player ID, and interrupt the thread group, to signify all other threads to
        // close, and consider the game lost.
        this.getThreadGroup().interrupt();
        winningPlayerID = denomination;

        // Logging to game stream
        System.out.println("player " + denomination + " has won");
        gameUpdateStream.append("player" + denomination + " wins\n");

        this.printToOutputFile();
    }

    /**
     * This method prints the data currently in the game update stream, and the correct logs for when a player
     * exits the game (win or lose), to an external log. Printing is caught in a try() operation so as to ensure
     * garbage collection is done on the FileWriter.
     */
    void printToOutputFile() {
        gameUpdateStream.append("player" + denomination + " exits\n");
        gameUpdateStream.append("player" + denomination + " final hand: " + hand.toString() + "\n");
        
        // Appending all of the data stored in gameUpdateStream to our file
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(gameUpdateStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Also logging the final position of the relevant decks to their corresponding output files.
         * We only log the right deck, because if all players log the right deck, all decks will eventually
         * be logged. 
         */
        hand.getRightDeck().printLogFile();
    }

    /**
     * This method sets the left deck of the player's CardHand.
     * @param leftNode
     */
    public void setLeftDeck(CardDeck leftNode) {
        hand.leftDeck = leftNode;
    }

    /**
     * This method sets the right deck of the player's CardHand.
     * @param rightNode
     */
    public void setRightDeck(CardDeck rightNode) {
        hand.rightDeck = rightNode;
    }
}
