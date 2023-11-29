import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * CardDeck is a class used to store data on the decks that Players draw from.<br><br>
 * It inherits from LinkedBlockingQueue to allow for direct operations on the data,
 * and includes methods to allow for printing of the deck when complete.
 */
public class CardDeck extends LinkedBlockingQueue<Card>{
    final int index;
    final File logFile;
    
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

    /**
     * Gets the index of the deck.
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Prints the final value in the deck to the externally stored deck output file.
     */
    void printLogFile() {
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