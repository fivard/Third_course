package task_a;

public class Bees implements Runnable {
    private static Manager manager;
    private static int bearX;
    private static int bearY;

    public static int getBearY() {
        return bearY;
    }

    public static int getBearX() {
        return bearX;
    }

    public static void setManager(Manager manager) {
        Bees.manager = manager;
    }

    @Override
    public void run() {
        while (!manager.isFoundBear()) {
            Manager.Task task = manager.getTask();
            System.out.println("Searching in area with number: " + task.getY());
            boolean [] area = task.getArea();

            if (area == null) return;

            for (int i = 0; i < area.length; i++) {
                if (area[i]){
                    bearX = i;
                    bearY = task.getY();
                    manager.setFoundBear(true);
                }
            }
        }
    }
}