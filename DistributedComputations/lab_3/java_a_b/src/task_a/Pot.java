package task_a;

public class Pot {

    private int maxAmount;
    private int currentAmount;

    Pot(int maxAmount){
        this.maxAmount = maxAmount;
        this.currentAmount = 0;
    }

    public synchronized void eatAll(){
        System.out.println("Pot is EMPTY");
        currentAmount = 0;
        notifyAll();
    }

    public synchronized void addHoney(){
        while(isFull()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        currentAmount++;
    }

    public synchronized boolean isFull(){
        return currentAmount == maxAmount;
    }
}
