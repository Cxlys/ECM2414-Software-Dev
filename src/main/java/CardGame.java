import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * CardGame is the entry class for our program, which sets up our objects, obtains the
 * player count and input pack, and finally begins our simulation with our Players.
 */
public class CardGame {
    static ArrayList<Player> players = new ArrayList<>();
    static ThreadGroup playerThreads = new ThreadGroup("Player Threads");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the Card Game Simulation!");
        Scanner scanner = new Scanner(System.in);

        // Wait for user input for both player count and input pack
        int playerCount = waitForValidPlayerCount(scanner); scanner.nextLine();
        ArrayList<Card> cards = waitForValidInputPack(scanner, playerCount); 
        scanner.close();

        // Initialise objects
        initialiseAllGroupObjects(cards);

        // Start game
        for (Player player : players) player.start();
    }

    /**
     * This method instantiates all objects required for the running of the Player objects,
     * including the decks, hands, cards and players themselves.
     * 
     * @param cards
     */
    static void initialiseAllGroupObjects(ArrayList<Card> cards) {
        // Creating Player objects
        for (int i = 0, j = 1; i < cards.size() / 2;) {
            ArrayList<Card> hand = new ArrayList<Card>(cards.subList(i, i + 4));

            Player player = new Player(playerThreads, hand, j++);
            players.add(player);

            i += 4;
        }

        ArrayList<CardDeck> decks = new ArrayList<>();
        // Creating Deck objects
        for (int i = cards.size() / 2, j = 1; i < cards.size();) {
            CardDeck deck = new CardDeck(
                new ArrayList<Card>(cards.subList(i, i + 4)),
                j++
            );
            decks.add(deck);
 
            i += 4;
        }

        // Creating circular structure
        for (int i = 0; i < players.size(); i++) {
            Player relevantPlayer = players.get(i);
            
            relevantPlayer.setLeftDeck(decks.get(i));
            relevantPlayer.setRightDeck(decks.get((i + 1) % decks.size()));
        }
    }
    
    /**
     * This method waits for a valid player count to be inputted via the CLI.
     * 
     * @param scanner
     */
    static int waitForValidPlayerCount(Scanner scanner) {
        while (true) {
            System.out.println("\nPlease enter the number of players.");

            if (!scanner.hasNextInt()) {
                System.out.println("Incorrect input, please try again");
                scanner.nextLine(); // Consume scanner input
                continue;
            }

            System.out.println("Valid player count!");
            return scanner.nextInt();
        }
    }

    /**
     * This method waits for a valid input pack to be inputted via the CLI.
     * 
     * @param scanner
     * @param playerCount
     */
    static ArrayList<Card> waitForValidInputPack(Scanner scanner, int playerCount) {
        while (true) {
            System.out.println("\nPlease enter the location of the input pack.");
            File file = null;

            // Checking to see if there is a file with the name given
            if (scanner.hasNextLine()) file = new File(scanner.nextLine());
            if (!file.exists()) {
                System.out.println("File not found!");
                continue; 
            }

            // If there is a file, put it into an ArrayList.
            ArrayList<Card> arrPack;
            try {
                arrPack = getInputPack(file, playerCount);
            } 
            catch (InvalidInputPackException e) { continue; } 
            catch (IOException e) { continue; }

            return arrPack;
        }
    } 

    /**
     * Checks a given File object for input pack validity.
     *  
     * @param pack
     * @param playerCount
     * @return True if file exists in the environment, pack has 2n lines (n = playerCount) and all lines are positive integers
     * @throws IOException
     * @throws InvalidInputPackException
     */
    static ArrayList<Card> getInputPack(File pack, int playerCount) throws IOException, InvalidInputPackException {
        try (Scanner rdr = new Scanner(new FileReader(pack))) {
            ArrayList<Card> buffer = new ArrayList<>();
            
            // We start looping through the input pack
            while (rdr.hasNextLine()) {
                // Checking for non-integers like characters
                if (!rdr.hasNextInt()) {
                    System.out.println("Not all lines are integer values!");
                    throw new InvalidInputPackException();
                }

                // Checking for non-positive numbers
                int next = rdr.nextInt();
                if (Integer.signum(next) < 1) {
                    System.out.println("Not all lines are positive!");
                    throw new InvalidInputPackException();
                }

                buffer.add(new Card(next));
            }
            // If our entire buffer is not of a valid size for the game, we throw an exception.
            if (!(buffer.size() == playerCount * 8)) {
                System.out.println("Incorrect line count!");
                throw new InvalidInputPackException();
            } 
            System.out.println("Valid file successfully found!");
            return buffer;
        }
    }

    /**
     * A method used by testing suites to tear down all static variables.
     */
    public static void resetAllVariables() {
        players = new ArrayList<>();
        playerThreads = new ThreadGroup("Player Threads");
    }
}
