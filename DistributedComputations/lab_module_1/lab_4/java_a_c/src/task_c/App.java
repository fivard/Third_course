package task_c;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class App {
    private static final String KYIV = "Kyiv";
    private static final String LVIV = "Lviv";
    private static final String ODESSA = "Odessa";
    private static final String KHARKIV = "Kharkiv";

    public static void main(String... args) throws InterruptedException {
        BusScheduler schedule = new BusScheduler();
        Creator threadCreator = new Creator(new ReentrantReadWriteLock(false), schedule);
        threadCreator.addBusStop(KYIV);
        threadCreator.addBusStop(LVIV);
        threadCreator.addBusStop(ODESSA);
        threadCreator.addBusStop(KHARKIV);
        threadCreator.addFlight(KYIV, LVIV, 150);
        threadCreator.addFlight(ODESSA, LVIV, 250);
        threadCreator.addFlight(ODESSA, KHARKIV, 100);
        threadCreator.addFlight(KYIV, ODESSA, 180);
        threadCreator.changeFlightPrice(KYIV, ODESSA, 200);
        System.out.println("Price for flight " + ODESSA + " - " + KHARKIV + " = " +
                threadCreator.getFlightPrice(ODESSA, KHARKIV));
        threadCreator.deleteFlight(ODESSA, KYIV);
        System.out.println("Price for flight " + ODESSA + " - " + KYIV + " = " +
                threadCreator.getFlightPrice(ODESSA, KYIV));
        threadCreator.deleteBusStop(LVIV);
        System.out.println("Price for flight " + ODESSA + " - " + LVIV + " = " +
                threadCreator.getFlightPrice(ODESSA, LVIV));
    }
}
