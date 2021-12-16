import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class ReciprocalArraySum {

    private ReciprocalArraySum() {
    }

    protected static double seqArraySum(final double[] input) {
        double sum = 0;

        for (int i = 0; i < input.length; i++) {
            sum += 1 / input[i];
        }

        return sum;
    }

    private static int getChunkSize(final int nChunks, final int nElements) {
        return (nElements + nChunks - 1) / nChunks;
    }

    private static int getChunkStartInclusive(final int chunk,
                                              final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        return chunk * chunkSize;
    }

    private static int getChunkEndExclusive(final int chunk, final int nChunks,
                                            final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }

    private static class ReciprocalArraySumTask extends RecursiveAction {
        private final int startIndexInclusive;
        private final int endIndexExclusive;
        private final double[] input;
        private double value;

        private final int nChunks;

        ReciprocalArraySumTask(final int setStartIndexInclusive,
                               final int setEndIndexExclusive,
                               final double[] setInput,
                               final int nChunks) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
            this.nChunks = nChunks;
        }

        @Override
        protected void compute() {
            if (isWorkSmallEnough()) {
                sum();
            } else {
                Collection<ReciprocalArraySumTask> all = ForkJoinTask.invokeAll(createTasks());
                for (ReciprocalArraySumTask task : all) {
                    value += task.value;
                }
            }
        }

        boolean isWorkSmallEnough() {
            return nChunks == 0;
        }

        private void sum() {
            for (int i = startIndexInclusive; i < endIndexExclusive; i++) {
                value += 1 / input[i];
            }
        }

        private List<ReciprocalArraySumTask> createTasks() {
            List<ReciprocalArraySumTask> tasks = new ArrayList<>(nChunks);
            for (int i = 0; i < nChunks; i++) {
                ReciprocalArraySumTask task = new ReciprocalArraySumTask(
                        getChunkStartInclusive(i, nChunks, input.length),
                        getChunkEndExclusive(i, nChunks, input.length),
                        input,
                        0);
                tasks.add(task);
            }

            return tasks;
        }

    }
    protected static double parArraySum(final double[] input) {
        assert input.length % 2 == 0;
        return parManyTaskArraySum(input, 2);
    }

    protected static double parManyTaskArraySum(final double[] input,
                                                final int numTasks) {
        ReciprocalArraySumTask task = new ReciprocalArraySumTask(0, input.length, input, numTasks);
        ForkJoinPool pool = new ForkJoinPool(numTasks);
        pool.invoke(task);
        return task.value;
    }
}