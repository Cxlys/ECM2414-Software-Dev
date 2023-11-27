import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class CardDeck extends LinkedBlockingQueue<Card>{
    int index;
    File logFile;
    
    public CardDeck (ArrayList<Card> cards, int index){
        super(cards);
        this.index = index;

        this.logFile = new File("deck" + index + "_output.txt");

        try {
            if (logFile.createNewFile()) {
                System.out.println("Deck file has been created: " + logFile.getName());
            } 
            else {
                System.out.println(
                    "Deck file already exists. " +
                    "Deleting and creating a new file."
                );
                logFile.delete(); logFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }

    public int getIndex() {
        return index;
    }

    public void printLogFile() {
        try (FileWriter writer = new FileWriter(logFile)) {
            writer.append("deck" + index + " contents: " + this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        if (this.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        while (!this.isEmpty()) {
            Card card = this.poll();
            builder.append(card.getValue() + " ");
        }

        return builder.toString();
    }
 }