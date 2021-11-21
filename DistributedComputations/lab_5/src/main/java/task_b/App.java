package task_b;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class App {
    private static final int NUMBER_OF_THREADS = 4;
    private static final ArrayList<Changer> changerList = new ArrayList<>();

    public static void main(String[] args) {
        Checker checker = new Checker(changerList);
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS, checker);

        Changer firstChanger = new Changer("ABCDCDAABCD", barrier, checker, 1);
        Changer secondChanger = new Changer("AAACAACBBAC", barrier, checker, 2);
        Changer thirdChanger = new Changer("ACDCADCACDC", barrier, checker, 3);
        Changer fourthChanger = new Changer("CDABBABCDAB", barrier, checker, 4);

        changerList.add(firstChanger);
        changerList.add(secondChanger);
        changerList.add(thirdChanger);
        changerList.add(fourthChanger);

        new Thread(firstChanger).start();
        new Thread(secondChanger).start();
        new Thread(thirdChanger).start();
        new Thread(fourthChanger).start();
    }
}
