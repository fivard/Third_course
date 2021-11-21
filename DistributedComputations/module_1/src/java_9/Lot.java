package java_9;

import java.util.concurrent.TimeUnit;

public class Lot implements Runnable {
    private final int count;
    private int maxPayment;
    private Participant maxPaymentCustomer;

    public Lot(int count){
        this.count = count;
    }

    @Override
    public void run() {
        try {
            Auction.startLot.set(true);
            TimeUnit.SECONDS.sleep(5);
            Auction.startLot.set(false);

            System.out.println("Stopped all raise");
            maxPayment = Auction.participants.get(0).getPayment();
            maxPaymentCustomer = Auction.participants.get(0);
            for (int i = 0; i < Auction.countParticipants; i++) {
                int newPayment = Auction.participants.get(i).getPayment();
                if (newPayment > maxPayment) {
                    maxPayment = newPayment;
                    maxPaymentCustomer = Auction.participants.get(i);
                }
            }

            System.out.println("Customer " + maxPaymentCustomer.number + " proposed the biggest payment " + maxPayment);
            maxPaymentCustomer.makePayment();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
