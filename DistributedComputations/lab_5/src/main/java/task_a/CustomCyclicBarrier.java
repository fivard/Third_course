package task_a;

public class CustomCyclicBarrier {
    private int partiesAtStart;
    private int partiesAwait;

    public CustomCyclicBarrier(int parties) {
        this.partiesAtStart = parties;
        this.partiesAwait = parties;
    }

    public synchronized void await() throws InterruptedException {
        partiesAwait--;
        if(partiesAwait > 0) {
            this.wait();
        }

        partiesAwait = partiesAtStart;
        notifyAll();
    }
}
