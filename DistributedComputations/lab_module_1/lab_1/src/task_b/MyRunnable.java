package task_b;

import javax.swing.*;

public class MyRunnable implements Runnable{
    private int value;
    private JSlider slider;

    public MyRunnable(int value, JSlider slider){
        this.value = value;
        this.slider = slider;
    }

    @Override
    public void run() {
        if (App.semaphore.compareAndSet(0, 1)) {
            while (!Thread.currentThread().isInterrupted()) {
                slider.setValue(value);
            }
            App.semaphore.set(0);
        }
    }
}
