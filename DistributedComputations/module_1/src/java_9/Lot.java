package java_9;

public class Lot implements Runnable {
    private final int count;

    public Lot(int count){
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(count);
    }
}
