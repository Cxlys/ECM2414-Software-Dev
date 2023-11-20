import java.io.File;
import java.io.IOException;

public class Player extends Thread {
    CardHand hand;
    int playerID;

    File file;

    StringBuilder gameUpdateStream = new StringBuilder();
    
    public Player(CardHand hand, int playerID) {
        this.hand = hand;
        this.playerID = playerID;
        this.file = new File("player" + playerID + "_output.txt");

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
            "player" + playerID + " initial hand " + 
            hand.toString() + "\n"
        );
    }

    @Override
    public void run() {
        try {
            for (;;) {
                if (Thread.currentThread().isInterrupted()) 
                    throw new InterruptedException();
                if (hand.handEqual()) 
                    break;
        
                Card addedCard = hand.removeFromLeftDeck();
        
                int removedCardIndex = hand.bestCardToRemove(playerID);
                Card removedCard = hand.get(removedCardIndex);
        
                hand.addToRightDeck(removedCardIndex);
        
                gameUpdateStream.append("player" + playerID + " draws a " + addedCard.getValue() + " from deck " + playerID + "\n");
                gameUpdateStream.append("player" + playerID + " discards a " + removedCard.getValue() + " to deck " + (playerID + 1) + "\n");
                gameUpdateStream.append("player" + playerID + " current hand is " + hand.toString() + "\n");
            }
        } catch (InterruptedException e) {            
            this.handleGameLoss(1); // 1 is a placeholder
        }

        System.out.println(gameUpdateStream.toString());
        this.handleGameWin();
    }

    void handleGameLoss(int winningPlayerID) {
        gameUpdateStream.append(
            "player" + winningPlayerID + 
            " has informed player" + playerID + 
            " that player" + winningPlayerID + 
            " has won"
        );
        gameUpdateStream.append("player" + playerID + " exits");
        gameUpdateStream.append("player" + playerID + " final hand: " + hand.toString());
    }

    void handleGameWin() {
        // Somehow find a way to notify all other threads that we have won.
        // First thoughts is using a notify/wait solution? But we need the main thread to notify
        // How do we get the main thread to know the winner?
        // Perhaps we add the players to our card decks? That way we can notify the objects themselves that we have won.
        // ....

        gameUpdateStream.append("player" + playerID + " wins");
        gameUpdateStream.append("player" + playerID + "exits");
        gameUpdateStream.append("player" + playerID + " final hand: " + hand.toString());
    }
}
