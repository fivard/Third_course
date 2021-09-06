package task_a;

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
        while (!Thread.currentThread().isInterrupted()){
            slider.setValue(value);
        }
    }
}
