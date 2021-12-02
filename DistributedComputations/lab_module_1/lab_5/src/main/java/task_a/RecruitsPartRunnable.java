package task_a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import static java.lang.System.*;

public class RecruitsPartRunnable implements Runnable {
    private static final AtomicBoolean finished = new AtomicBoolean(false);
    private static final List<Boolean> finishedPart = new ArrayList<>(){{
        add(false);
        add(false);
        add(false);
    }};

    private final int[] recruits;
    private final CustomCyclicBarrier barrier;

    private final int partIndex;
    private final int leftIndex;
    private final int rightIndex;

    public RecruitsPartRunnable(int[] recruits, CustomCyclicBarrier barrier, int partIndex, int leftIndex, int rightIndex) {
        this.recruits = recruits;
        this.barrier = barrier;
        this.partIndex = partIndex;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    public void run() {
        while (!finished.get()) {
            boolean currentPartFinished = finishedPart.get(partIndex);
            if (!currentPartFinished) {
                out.println("Part "+ partIndex + ": " + Arrays.toString(recruits));
                boolean isFormatted = true;
                for (int i = leftIndex; i < rightIndex - 1; i++) {
                    if (recruits[i] != recruits[i+1]){
                        recruits[i] *= -1;
                        isFormatted = false;
                    }
                }
                if(isFormatted) {
                    finish();
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void finish() {
        out.println("Part " + partIndex + " has finished sorting.");
        finishedPart.set(partIndex, true);
        for (boolean part: finishedPart) {
            if (!part) {
                return;
            }
        }
        finished.set(true);
    }
}
