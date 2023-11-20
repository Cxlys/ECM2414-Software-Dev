import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Player extends Thread {
    CardHand hand;
    int playerID;
    File file;

    // I really do not like this solution, but I've been doing this for like 
    // nearly 3 hours now and I want to get off Mr. Bones' Wild Ride 
    static Integer winningPlayerID = null;
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
                synchronized (this) {
                    if (winningPlayerID != null) 
                        throw new InterruptedException();
                    if (hand.handEqual()) 
                        break;
                }
        
                Card addedCard = hand.removeFromLeftDeck();
        
                int removedCardIndex = hand.bestCardToRemove(playerID);
                Card removedCard = hand.get(removedCardIndex);
        
                hand.addToRightDeck(removedCardIndex);
        
                gameUpdateStream.append("player" + playerID + " draws a " + addedCard.getValue() + " from deck " + playerID + "\n");
                gameUpdateStream.append("player" + playerID + " discards a " + removedCard.getValue() + " to deck " + (playerID + 1) + "\n");
                gameUpdateStream.append("player" + playerID + " current hand is " + hand.toString() + "\n");
            }
        } catch (InterruptedException e) {   
            this.handleGameLoss();
            this.printToOutputFile();
            return;
        }

        this.handleGameWin();
        this.printToOutputFile();
    }

    void handleGameLoss() {
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
        // We update winning player ID, to signify to all other threads
        // that this thread needs to close.

        // I still hate this solution, as we have not used any thread methods except start.
        // I think we should still look for an interrupt() solution, as whenever two players
        // win at the same time, they both try and "win", and synchronised(this) is slow.
        // High likelihood I am also overthinking this
        winningPlayerID = playerID;

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
