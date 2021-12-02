package task_a;

import java.util.concurrent.atomic.AtomicInteger;

public class Locker {
    private final AtomicInteger readLock;
    private final AtomicInteger writeLock;

    public Locker(){
        readLock = new AtomicInteger(0);
        writeLock = new AtomicInteger(0);
    }

    public void readLock() {
        while (readLock.get() == 1) {
            try {
                this.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (readLock) {
            readLock.set(1);
        }
    }

    public void readUnlock() {
        synchronized (readLock) {
            readLock.set(0);
        }
    }

    public void writeLock() {
        readLock();
        while (writeLock.get() == 1) {
            try {
                this.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (writeLock) {
            writeLock.set(1);
        }
    }

    public void writeUnlock() {
        synchronized (this) {
            readLock.set(0);
            writeLock.set(0);
        }
    }
}