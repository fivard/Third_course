package java_9;

import java.util.concurrent.TimeUnit;

public class Participant extends Thread {
    private int currentPayment = 0;
    private boolean skippedTheCurrentLot;
    private int lotsToSkip;
    public int number;

    public Participant(int number){
        this.number = number;
        setDaemon(true);
        start();
    }

    public void makePayment(){
        double chanceToPay = Math.random();
        currentPayment = 0;
        if (chanceToPay < 0.1){
            System.out.println("Customer " + number + " can't make a payment. Skip the next 2 lots");
            lotsToSkip = 2;
        }
    }

    public void raisePayment(){
        currentPayment += 100;
        System.out.println("Customer " + number + " raised payment to " + currentPayment);
    }

    public int getPayment(){
        return currentPayment;
    }

    @Override
    public void run() {
        System.out.println("Started new customer " + number);
        while (true){
            if (Auction.startLot.get()){
                if (lotsToSkip > 0 && !skippedTheCurrentLot){
                    lotsToSkip--;
                    skippedTheCurrentLot = true;
                    System.out.println("Customer " + number + " skipped the current lot");
                }

                double chanceToRaise = Math.random();
                if (chanceToRaise > 0.7)
                    raisePayment();

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (lotsToSkip > 0)
                    skippedTheCurrentLot = false;
                currentPayment = 0;
            }
        }
    }

}
