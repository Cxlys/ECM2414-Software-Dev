import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CardGame {
    public static void main(String[] args) {
        System.out.println("Welcome to the Card Game Simulation!");

        int playerCount = 0;
        ArrayList<Card> arrayPackValues = null;
        
        Scanner scanner = new Scanner(System.in);

        playerCount = waitForValidPlayerCount(scanner);
        scanner.nextLine();

        arrayPackValues = waitForInputPack(scanner, playerCount);

        scanner.close();
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

    static ArrayList<Card> waitForInputPack(Scanner scanner, int playerCount) {
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
