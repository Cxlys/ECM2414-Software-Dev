public class Player extends Thread {
    CardHand hand;

    public Player(CardHand hand) {
        this.hand = hand;
    }
}
