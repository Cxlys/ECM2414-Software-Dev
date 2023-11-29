import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Thread {
    class CardHand extends ArrayList<Card> {
        CardDeck leftDeck;
        CardDeck rightDeck;

        CardHand(ArrayList<Card> cards) {
            this.addAll(cards);
        }

        boolean handEqual(){
            int first = this.get(0).getValue();
            for (int i = 1; i < this.size(); i++) {
                if (this.get(i).getValue() != first) {
                    return false;
                }
            }
            return true; 
        }

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

        CardDeck getLeftDeck() {
            return leftDeck;
        }

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
        super(group, String.valueOf(denomination));

        this.hand = new CardHand(hand);
        this.denomination = denomination;
        this.file = new File("player" + denomination + "_output.txt");

        try {
            if (file.createNewFile()) {
                System.out.println("File has been created: " + file.getName());
            } 
            else {
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

    @Override
    public void run() {
        try {
            for (;;) {
                synchronized (Player.class) {
                    // System.out.println("Thread " + Thread.currentThread().getName() + " has started synchronised block");
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    if (hand.handEqual()) {
                        // System.out.println("Thread " + Thread.currentThread().getName() + " has started win game sequence");
                        this.handleGameWin();
                        break;
                    }
                    // System.out.println("Thread " + Thread.currentThread().getName() + " has ended synchronised block");
                }

                int removeIndex = hand.bestCardToRemove(denomination);
                Card removedCard = hand.get(removeIndex);
                Card addedCard = hand.handleCardDraw(removeIndex);

                gameUpdateStream.append("player" + denomination + " draws a " + addedCard.getValue() + " from deck " + denomination + "\n");
                gameUpdateStream.append("player" + denomination + " discards a " + removedCard.getValue() + " to deck " + ((denomination) % 4 + 1) + "\n");
                gameUpdateStream.append("player" + denomination + " current hand is " + hand.toString() + "\n");
    
                for (Card card : hand) card.incrementRoundCount();
            }
        } catch (InterruptedException e) {   
            this.handleGameLoss();
        }
        this.printToOutputFile();
    }

    void handleGameLoss() {
        gameUpdateStream.append(
            "player" + winningPlayerID + 
            " has informed player" + denomination + 
            " that player" + winningPlayerID + 
            " has won\n"
        );
    }

    void handleGameWin() {
        // We update winning player ID, to signify to all other threads
        // that this thread needs to close.
        this.getThreadGroup().interrupt();
        System.out.println("player " + denomination + " has won");
        winningPlayerID = denomination;

        gameUpdateStream.append("player" + denomination + " wins\n");
        
    }

    void printToOutputFile() {
        gameUpdateStream.append("player" + denomination + " exits\n");
        gameUpdateStream.append("player" + denomination + " final hand: " + hand.toString() + "\n");
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(gameUpdateStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Do this in the same method, as we don't want to duplicate code again.
        // Only do the right deck so we don't end up writing to the same file twice.
        hand.getRightDeck().printLogFile();
    }

    public void setLeftDeck(CardDeck leftNode) {
        hand.leftDeck = leftNode;
    }

    public void setRightDeck(CardDeck rightNode) {
        hand.rightDeck = rightNode;
    }
}
