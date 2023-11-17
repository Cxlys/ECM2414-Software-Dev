import java.io.File;
import java.io.IOException;

public class Player extends Thread {
    CardHand hand;
    int playerID;
    File file;
    StringBuilder fileBuffer = new StringBuilder();
    CardDeck leftDeck;
    CardDeck rightDeck;
    Card newCard;

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


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        fileBuffer.append("player " + playerID + " initial hand " + hand.toString() + "\n");   
  }

  public void run() {
    try{
      while (true){
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
        if(hand.handEqual()) break;
        leftDeck = hand.getLeftDeck();
        Card addedCard = hand.removeFromLeftDeck();
        int removedCardIndex = hand.bestCardToRemove(playerID);
        Card removedCard = hand.get(removedCardIndex);
        hand.addToRightDeck(removedCardIndex);
        fileBuffer.append("Player " + playerID + " draws a " + addedCard.getValue() + " from deck " + playerID + "\n");
        fileBuffer.append("Player " + playerID + " discards a " + removedCard.getValue() + " to deck " + (playerID +  1) + "\n");
        fileBuffer.append("player " + playerID + " hand " + hand.toString() + "\n"); 
        System.out.println(fileBuffer.toString());
      }
    }
    catch (InterruptedException e) {

    }
  }


}
