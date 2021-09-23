package task_b;

public class App {
    public static void main(String[] args) throws InterruptedException {
        int numberOfVisitors = 10;

        Thread[] threads = new Thread[numberOfVisitors];
        Barber barber = new Barber();
        System.out.println("Started working day");

        for (int i = 0; i < numberOfVisitors; i++) {
            threads[i] = new Thread(new Visitor(barber), "Visitor №" + i);
            threads[i].start();
        }
    }
}