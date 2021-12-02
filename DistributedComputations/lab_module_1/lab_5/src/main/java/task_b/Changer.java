package task_b;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import static java.lang.System.*;

public class Changer implements Runnable {
    private final Random random = new Random();
    public String currentString;
    private final CyclicBarrier barrier;
    private final Checker checker;
    private final int indexOfThread;

    public Changer(String str, CyclicBarrier barrier, Checker checker, int index){
        this.currentString = str;
        this.barrier = barrier;
        this.checker = checker;
        this.indexOfThread = index;
    }

    @Override
    public void run(){
        while(!checker.isStopped()) {
            int randIndex = random.nextInt(currentString.length());
            switch (currentString.charAt(randIndex)) {
                case 'A': {
                    currentString = currentString.substring(0, randIndex) + 'C' + currentString.substring(randIndex + 1);
                    break;
                }
                case 'B': {
                    currentString = currentString.substring(0, randIndex) + 'D' + currentString.substring(randIndex + 1);
                    break;
                }
                case 'C': {
                    currentString = currentString.substring(0, randIndex) + 'A' + currentString.substring(randIndex + 1);
                    break;
                }
                case 'D': {
                    currentString = currentString.substring(0, randIndex) + 'B' + currentString.substring(randIndex + 1);
                    break;
                }
            }
            out.println("Thread #" + this.indexOfThread + " " + currentString + " " + checker.countAB(currentString));
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
