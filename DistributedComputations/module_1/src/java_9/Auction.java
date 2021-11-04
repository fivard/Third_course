package java_9;

public class Auction {
    public static void main(String[] args) {
        int countLots = 5 + (int) (Math.random() * 10);

        for (int i = 0; i < countLots; i++){
            Lot lot = new Lot(i);
            lot.run();
        }
    }
}
