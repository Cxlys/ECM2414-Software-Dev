import java.util.ArrayList;
import java.util.EventListener;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CardGame {
    static int playerCount = 0;
    static ArrayList<Player> players = new ArrayList<>();
    static ThreadGroup playerThreads = new ThreadGroup("Player Threads");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the Card Game Simulation!");
        Scanner scanner = new Scanner(System.in);

        // Wait for user input for both player count and input pack
        playerCount = waitForValidPlayerCount(scanner); scanner.nextLine();
        ArrayList<Card> cards = waitForValidInputPack(scanner, playerCount); 
        scanner.close();

        // Initialise objects
        initialiseAllGroupObjects(cards);

        // Start game
        for (Player player : players) player.start();
    }

    static void initialiseAllGroupObjects(ArrayList<Card> cards) {
        ArrayList<CardHand> hands = new ArrayList<>();
        // Creating Player objects
        for (int i = 0, j = 1; i < cards.size() / 2;) {
            CardHand hand = new CardHand(new ArrayList<Card>(cards.subList(i, i + 4)));
            hands.add(hand);

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

            i+=4; j++;
        }

        System.out.println(decks.toString());

        // Creating circular structure
        for (int i = 0; i < hands.size(); i++) {
            CardHand hand = hands.get(i);
            
            hand.setLeftDeck(decks.get(i));
            hand.setRightDeck(decks.get((i + 1) % decks.size()));
        }
    }
    
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

    static ArrayList<Card> waitForValidInputPack(Scanner scanner, int playerCount) {
        while (true) {
            System.out.println("\nPlease enter the location of the input pack.");
            File file = null;

            if (scanner.hasNextLine()) file = new File(scanner.nextLine());
            if (!file.exists()) {
                System.out.println("File not found!");
                continue; 
            }

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
            while (rdr.hasNextLine()) {
                if (!rdr.hasNextInt()) {
                    System.out.println("Not all lines are integer values!");
                    throw new InvalidInputPackException();
                }

                int next = rdr.nextInt();
                if (Integer.signum(next) < 1) {
                    System.out.println("Not all lines are positive!");
                    throw new InvalidInputPackException();
                }

                buffer.add(new Card(next));
            }
            if (!(buffer.size() == playerCount * 8)) {
                System.out.println("Too many lines in the file!");
                throw new InvalidInputPackException();
            } 
            System.out.println("Valid file successfully found!");
            return buffer;
        }
    }
}
