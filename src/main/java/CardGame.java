import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CardGame {
    public static void main(String[] args) {
        System.out.println("Welcome to the Card Game Simulation!");

        int playerCount = 0;
        File inputPackFile = null;
        
        Scanner scanner = new Scanner(System.in);

        while (playerCount == 0) {
            System.out.println("\nPlease enter the number of players.");

            if (!scanner.hasNextInt()) {
                System.out.println("Incorrect input, please try again");
                scanner.nextLine(); // Consume scanner input
                continue;
            }

            playerCount = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Valid player count!");
        }

        try {
            while (inputPackFile == null) {
                System.out.println("\nPlease enter the location of the input pack.");
                File file = null;

                if (scanner.hasNextLine()) file = new File(scanner.nextLine());

                if (!checkInputPack(file, playerCount)) {
                    System.out.println("Incorrect input, please try again");
                    continue;
                }

                inputPackFile = file;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
    }    

    /**
     * Checks a given File object for input pack validity.
     *  
     * @param pack
     * @param playerCount
     * @return True if file exists in the environment, pack has 2n lines (n = playerCount) and all lines are positive integers
     * @throws IOException
     */
    static boolean checkInputPack(File pack, int playerCount) throws IOException {
        try (Scanner rdr = new Scanner(new FileReader(pack))) {
            int count = 0;
            while (rdr.hasNextLine()) {
                if (!rdr.hasNextInt() || Integer.signum(rdr.nextInt()) < 1) {
                    System.out.println("Not all lines are integer values, or are not positive!");
                    return false;
                };
                rdr.nextLine();
                count++;
            }
            if (!(count == playerCount * 8)) {
                System.out.println("Too many lines in the file!");
                return false;
            } 
            System.out.println("Valid file successfully found!");
            return true;
        } 
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return false;
        }
    }
}