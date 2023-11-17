import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class Player extends Thread {
    CardHand hand;
    int playerID;

    File file;
    FileWriter writer;

    public Player(CardHand hand, int playerID) {
        this.hand = hand;
        this.playerID = playerID;

        try {
            File myObj = new File("player" + playerID + "_output.txt");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
              myObj.delete(); myObj.createNewFile();
            }

            writer = new FileWriter(file);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
    }
}
