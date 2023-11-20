import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Player extends Thread {
    static Thread mainThread = Thread.currentThread();

    CardHand hand;
    int playerID;
    File file;

    StringBuilder gameUpdateStream = new StringBuilder();
    
    public Player(ThreadGroup group, CardHand hand, int playerID) {
        super(group, String.valueOf(playerID));

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

        this.handleGameWin();
        this.printToOutputFile();
    }

    void handleGameLoss(int winningPlayerID) {
        gameUpdateStream.append(
            "player" + winningPlayerID + 
            " has informed player" + playerID + 
            " that player" + winningPlayerID + 
            " has won\n"
        );
        gameUpdateStream.append("player" + playerID + " exits\n");
        gameUpdateStream.append("player" + playerID + " final hand: " + hand.toString() + "\n");
    }

    void handleGameWin() {
        // Somehow find a way to notify all other threads that we have won.
        // First thoughts is using a notify/wait solution? But we need the main thread to notify
        // How do we get the main thread to know the winner?
        // ....
        this.getThreadGroup().interrupt();
        System.out.println(gameUpdateStream);

        gameUpdateStream.append("player" + playerID + " wins\n");
        gameUpdateStream.append("player" + playerID + " exits\n");
        gameUpdateStream.append("player" + playerID + " final hand: " + hand.toString() + "\n");
    }

    void printToOutputFile() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(gameUpdateStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
