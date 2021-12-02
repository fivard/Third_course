package task_c;

import java.util.concurrent.locks.ReadWriteLock;

public class Creator implements Runnable {
    private final ReadWriteLock lock;
    private final BusScheduler scheduler;
    private Instructions instruction;
    private String firstCity;
    private String secondCity;
    private Integer price;

    public Creator(ReadWriteLock lock, BusScheduler schedule) {
        this.lock = lock;
        this.scheduler = schedule;
    }

    public void addBusStop(String city) throws InterruptedException {
        this.instruction = Instructions.ADD_BUS_STOP;
        this.firstCity = city;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void deleteBusStop(String city) throws InterruptedException {
        this.instruction = Instructions.REMOVE_BUS_STOP;
        this.firstCity = city;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void changeFlightPrice(String firstCity, String secondCity, int price) throws InterruptedException {
        this.instruction = Instructions.CHANGE_PRICE;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.price = price;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void addFlight(String firstCity, String secondCity, int price) throws InterruptedException {
        this.instruction = Instructions.ADD_FLIGHT;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.price = price;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void deleteFlight(String firstCity, String secondCity) throws InterruptedException {
        this.instruction = Instructions.REMOVE_FLIGHT;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public Integer getFlightPrice(String firstCity, String secondCity) throws InterruptedException {
        this.instruction = Instructions.GET_FLIGHT_PRICE;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
        return price;
    }

    private void addBusStopImpl() {
        lock.writeLock().lock();
        scheduler.addBusStop(firstCity);
        lock.writeLock().unlock();
    }

    private void deleteBusStopImpl() {
        lock.writeLock().lock();
        scheduler.deleteBusStop(firstCity);
        lock.writeLock().unlock();
    }

    private void changeFlightPriceImpl() {
        lock.writeLock().lock();
        scheduler.changeFlightPrice(firstCity, secondCity, price);
        lock.writeLock().unlock();
    }

    private void addFlightImpl() {
        lock.writeLock().lock();
        scheduler.addFlight(firstCity, secondCity, price);
        lock.writeLock().unlock();
    }

    private void deleteFlightImpl() {
        lock.writeLock().lock();
        scheduler.deleteFlight(firstCity, secondCity);
        lock.writeLock().unlock();
    }

    private void getFlightPriceImpl() {
        lock.writeLock().lock();
        price = scheduler.getFlightPrice(firstCity, secondCity);
        lock.writeLock().unlock();
    }

    @Override
    public void run() {
        switch (instruction) {
            case ADD_BUS_STOP: {
                addBusStopImpl();
                break;
            }
            case REMOVE_BUS_STOP: {
                deleteBusStopImpl();
                break;
            }
            case CHANGE_PRICE: {
                changeFlightPriceImpl();
                break;
            }
            case ADD_FLIGHT: {
                addFlightImpl();
                break;
            }
            case REMOVE_FLIGHT: {
                deleteFlightImpl();
                break;
            }
            case GET_FLIGHT_PRICE: {
                getFlightPriceImpl();
                break;
            }
        }
    }
}