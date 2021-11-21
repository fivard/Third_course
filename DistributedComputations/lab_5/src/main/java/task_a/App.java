package task_a;

import java.util.Arrays;
import java.util.Random;
import static java.lang.System.*;

public class App {
    private static final Random random = new Random(currentTimeMillis());
    private static final int SIZE = 150;
    private static final int NUMBER_OF_PARTS = 3;
    private static final Thread[] threads = new Thread[NUMBER_OF_PARTS];
    private static final int [] recruits = new int[SIZE];
    private static final CustomCyclicBarrier barrier = new CustomCyclicBarrier(NUMBER_OF_PARTS);

    public static void main(String[] args) {
        fillRecruitsArray();
        createAndStartThreads();
        out.println("Result: " + Arrays.toString(recruits));
    }

    private static void fillRecruitsArray() {
        for(int i = 0; i < SIZE; i++) {
            if(random.nextBoolean()) {
                recruits[i] = 1;
            } else {
                recruits[i] = -1;
            }
        }
    }

    private static void createAndStartThreads() {
        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(new RecruitsPartRunnable(recruits, barrier, i, i * 50, (i + 1) * 50));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}