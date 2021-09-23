package task_b;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class Barber {
    private final Semaphore semaphore = new Semaphore(1);
    private final Queue<Visitor> visitors = new ConcurrentLinkedDeque<>();

    public void addVisitorToQueue(Visitor visitorRunnable){
        visitors.add(visitorRunnable);
        System.out.println(Thread.currentThread().getName() + " went into the queue.");
        cutHair();
    }

    public void cutHair() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " sat down in the chair.");
            visitors.remove();
            System.out.println(Thread.currentThread().getName() + " exited barbershop.");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}