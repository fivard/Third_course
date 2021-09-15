package task_c;

import java.util.concurrent.ForkJoinPool;

public class App {
    public static void main(String[] args) {
        Integer numberOfMonks = 50;
        Tournament tournament = new Tournament(numberOfMonks);
        ForkJoinPool pool = new ForkJoinPool();
        Monk winner = pool.invoke(tournament);
        System.out.println("The winner is " + winner.toString());
    }
}