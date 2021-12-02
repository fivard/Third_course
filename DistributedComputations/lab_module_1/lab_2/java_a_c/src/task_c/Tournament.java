package task_c;

import java.util.concurrent.RecursiveTask;

public class Tournament extends RecursiveTask<Monk> {
    private Monk[] monks;
    private int start;
    private int end;

    public Tournament(Integer numberOfMonks) {
        this.monks = new Monk[numberOfMonks];
        for(int i = 0; i < numberOfMonks; i++){
            this.monks[i] = new Monk();
            System.out.println(this.monks[i].toString());
        }
        System.out.println("Tournament has begun, monks: ");
        for (Monk monk : monks ){
            System.out.println(monk.toString());
        }
        this.start = 0;
        this.end = monks.length - 1;
    }

    private Tournament(Monk[] monks, int end, int start) {
        this.monks = monks;
        this.end = end;
        this.start = start;
    }

    protected Monk compute() {
        int length = end - start;

        if (length == 0) {
            return monks[end];
        } else if (length == 1) {
            return Monk.max(monks[end], monks[start]);
        } else {
            Tournament leftChampion = new Tournament(monks, start, (end + start) / 2);
            leftChampion.fork();

            Tournament rightChampion = new Tournament(monks, (end + start) / 2 + 1, end);
            rightChampion.fork();

            return Monk.max(leftChampion.join(), rightChampion.join());
        }
    }
}
