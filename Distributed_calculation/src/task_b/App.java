package task_b;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    private static Thread firstThread;
    private static Thread secondThread;
    private static final JSlider slider = new JSlider();
    public static AtomicInteger semaphore = new AtomicInteger(0);

    private static JButton startFirstThreadButton = new JButton("Start 1 Thread");
    private static JButton startSecondThreadButton = new JButton("Start 2 Thread");
    private static JButton stopFirstThreadButton = new JButton("Stop 1 Thread");
    private static JButton stopSecondThreadButton = new JButton("Stop 2 Thread");
    private static JLabel runningThreadLabel = new JLabel("Another thread is already running!");

    public static void main(String[] args) {
        setGUI();
    }

    private static void startFirstThread(){
        if (semaphore.get() == 1){
            runningThreadLabel.setVisible(true);
            return;
        }

        firstThread = new Thread(new MyRunnable(10, slider));
        firstThread.setDaemon(true);
        firstThread.start();
        firstThread.setPriority(1);

        runningThreadLabel.setVisible(false);
        stopSecondThreadButton.setEnabled(false);
    }

    private static void startSecondThread(){
        if (semaphore.get() == 1){
            runningThreadLabel.setVisible(true);
            System.out.println(runningThreadLabel.getVisibleRect());
            return;
        }

        secondThread = new Thread(new MyRunnable(90, slider));
        secondThread.setDaemon(true);
        secondThread.start();
        secondThread.setPriority(10);

        runningThreadLabel.setVisible(false);
        stopFirstThreadButton.setEnabled(false);
    }

    private static void stopFirstThread(){
        if (semaphore.get() == 1) {
            firstThread.interrupt();
            stopSecondThreadButton.setEnabled(true);
            runningThreadLabel.setVisible(false);
        }
    }

    private static void stopSecondThread(){
        if (semaphore.get() == 1) {
            secondThread.interrupt();
            stopFirstThreadButton.setEnabled(true);
            runningThreadLabel.setVisible(false);
        }
    }


    private static void setGUI(){
        JFrame frame = new JFrame("Task B");

        slider.setBounds(100,50,600,100);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        startFirstThreadButton.setBounds(100,150,200,50);
        startFirstThreadButton.addActionListener(e -> startFirstThread());

        startSecondThreadButton.setBounds(500,150,200,50);
        startSecondThreadButton.addActionListener(e -> startSecondThread());

        stopFirstThreadButton.setBounds(100,210,200,50);
        stopFirstThreadButton.addActionListener(e -> stopFirstThread());

        stopSecondThreadButton.setBounds(500,210,200,50);
        stopSecondThreadButton.addActionListener(e -> stopSecondThread());

        runningThreadLabel.setBounds(300, 260, 300, 50);
        runningThreadLabel.setVisible(false);

        frame.add(slider);
        frame.add(startFirstThreadButton);
        frame.add(startSecondThreadButton);
        frame.add(stopFirstThreadButton);
        frame.add(stopSecondThreadButton);
        frame.add(runningThreadLabel);

        frame.setSize(800,400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
