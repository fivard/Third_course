package task_a;

public class App {
    public static void main(String[] args) {
        Bees.setManager(new Manager());
        int countBees = 5;
        Thread[] threads = new Thread[countBees];

        for (int i = 0; i < countBees; i++) {
            threads[i] = new Thread(new Bees());
            threads[i].start();
        }

        for (int i = 0; i < countBees; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Winni's X: " + Bees.getBearX() + " and Winni's Y: " + Bees.getBearY());
    }
}
