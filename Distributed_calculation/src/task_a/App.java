package task_a;

import javax.swing.*;

public class App {
    private static Thread firstThread;
    private static Thread secondThread;
    private static final JSlider slider = new JSlider();
    private static boolean isStarted = false;

    public static void main(String[] args) {
        setGUI();

        firstThread = new Thread(new MyRunnable(10, slider));
        secondThread = new Thread(new MyRunnable(90, slider));
        firstThread.setDaemon(true);
        secondThread.setDaemon(true);
        firstThread.setPriority(1);
        secondThread.setPriority(1);
    }

    private static void startProcess(){
        if (!isStarted){
            firstThread.start();
            secondThread.start();
            isStarted = true;
        }
    }

    private static void setGUI(){
        JFrame frame = new JFrame("Task A");

        slider.setBounds(100,50,600,100);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JButton startButton = new JButton("Start");
        startButton.setBounds(300,150,200,50);
        startButton.addActionListener(e -> startProcess());

        SpinnerModel firstModel = new SpinnerNumberModel(1, 0, 10, 1);
        JSpinner firstSpinner = new JSpinner(firstModel);
        firstSpinner.addChangeListener(e -> {
            int changedValue = (int) firstSpinner.getValue();
            firstThread.setPriority(changedValue);
        });
        firstSpinner.setBounds(100, 250, 200, 50);

        SpinnerModel secondModel = new SpinnerNumberModel(1, 0, 10, 1);
        JSpinner secondSpinner = new JSpinner(secondModel);
        secondSpinner.addChangeListener(e -> {
            int changedValue = (int) secondSpinner.getValue();
            secondThread.setPriority(changedValue);
        });
        secondSpinner.setBounds(500, 250, 200, 50);

        frame.add(slider);
        frame.add(startButton);
        frame.add(firstSpinner);
        frame.add(secondSpinner);

        frame.setSize(800,400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
