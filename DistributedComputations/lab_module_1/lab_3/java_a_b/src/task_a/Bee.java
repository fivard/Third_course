package task_a;

public class Bee implements Runnable{
    private static int counterBeeID = 0;
    Bear bear;
    Pot pot;
    int beeID;

    Bee (Bear bear, Pot pot){
        this.beeID = counterBeeID++;
        this.bear = bear;
        this.pot = pot;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Bee #" + beeID + " brought honey");
            pot.addHoney();

            if (pot.isFull()) {
                System.out.println("Pot is FULL");
                bear.wakeUp();
            }
        }
    }
}
