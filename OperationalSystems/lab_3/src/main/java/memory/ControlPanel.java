package memory;

import java.awt.*;
import java.util.Vector;

public class ControlPanel extends Frame {
    Kernel kernel;
    Button runButton = new Button("run");
    Button stepButton = new Button("step");
    Button resetButton = new Button("reset");
    Button exitButton = new Button("exit");
    Vector<Button> pagesButtons = new Vector<>();
    Vector<Label> pagesLabels = new Vector<>();
    Label statusValueLabel = new Label("STOP", Label.LEFT);
    Label timeValueLabel = new Label("0", Label.LEFT);
    Label instructionValueLabel = new Label("NONE", Label.LEFT);
    Label addressValueLabel = new Label("NULL", Label.LEFT);
    Label pageFaultValueLabel = new Label("NO", Label.LEFT);
    Label virtualPageValueLabel = new Label("x", Label.LEFT);
    Label physicalPageValueLabel = new Label("0", Label.LEFT);
    Label inMemTimeValueLabel = new Label("0", Label.LEFT);
    Label lastTouchTimeValueLabel = new Label("0", Label.LEFT);
    Label agingValueLabel = new Label("00000000", Label.LEFT);

    public ControlPanel(String title) {
        super(title);
        for (int i = 0; i < 64; i++){
            pagesButtons.add(new Button("page " + (i)));
            pagesLabels.add(new Label(null, Label.CENTER));
        }
    }

    public void init(Kernel useKernel, String commands, String config) {
        kernel = useKernel;
        kernel.setControlPanel(this);
        setLayout(null);
        setBackground(Color.white);
        setForeground(Color.black);
        resize(700, 545);
        setFont(new Font("Courier", 0, 12));

        runButton.setForeground(Color.blue);
        runButton.setBackground(Color.lightGray);
        runButton.reshape(0, 25, 70, 15);
        add(runButton);

        stepButton.setForeground(Color.blue);
        stepButton.setBackground(Color.lightGray);
        stepButton.reshape(70, 25, 70, 15);
        add(stepButton);

        resetButton.setForeground(Color.blue);
        resetButton.setBackground(Color.lightGray);
        resetButton.reshape(140, 25, 70, 15);
        add(resetButton);

        exitButton.setForeground(Color.blue);
        exitButton.setBackground(Color.lightGray);
        exitButton.reshape(210, 25, 70, 15);
        add(exitButton);

        for (int i = 0; i < 64; i++) {
            Button b = pagesButtons.elementAt(i);
            b.reshape(i / 32 * 140, (i % 32 + 2) * 15 + 25, 70, 15);
            b.setForeground(Color.magenta);
            b.setBackground(Color.lightGray);
            add(b);
        }

        statusValueLabel.reshape(345, 0 + 25, 100, 15);
        add(statusValueLabel);

        timeValueLabel.reshape(345, 15 + 25, 100, 15);
        add(timeValueLabel);

        instructionValueLabel.reshape(385, 45 + 25, 100, 15);
        add(instructionValueLabel);

        addressValueLabel.reshape(385, 60 + 25, 300, 15);
        add(addressValueLabel);

        pageFaultValueLabel.reshape(385, 90 + 25, 100, 15);
        add(pageFaultValueLabel);

        virtualPageValueLabel.reshape(395, 120 + 25, 200, 15);
        add(virtualPageValueLabel);

        physicalPageValueLabel.reshape(395, 135 + 25, 200, 15);
        add(physicalPageValueLabel);

        inMemTimeValueLabel.reshape(395, 150 + 25, 200, 15);
        add(inMemTimeValueLabel);

        lastTouchTimeValueLabel.reshape(395, 165 + 25, 200, 15);
        add(lastTouchTimeValueLabel);

        agingValueLabel.reshape(395, 180 + 25, 230, 15);
        add(agingValueLabel);

        Label virtualOneLabel = new Label("virtual", Label.CENTER);
        virtualOneLabel.reshape(0, 15 + 25, 70, 15);
        add(virtualOneLabel);

        Label virtualTwoLabel = new Label("virtual", Label.CENTER);
        virtualTwoLabel.reshape(140, 15 + 25, 70, 15);
        add(virtualTwoLabel);

        Label physicalOneLabel = new Label("physical", Label.CENTER);
        physicalOneLabel.reshape(70, 15 + 25, 70, 15);
        add(physicalOneLabel);

        Label physicalTwoLabel = new Label("physical", Label.CENTER);
        physicalTwoLabel.reshape(210, 15 + 25, 70, 15);
        add(physicalTwoLabel);

        Label statusLabel = new Label("status: ", Label.LEFT);
        statusLabel.reshape(285, 0 + 25, 65, 15);
        add(statusLabel);

        Label timeLabel = new Label("time: ", Label.LEFT);
        timeLabel.reshape(285, 15 + 25, 50, 15);
        add(timeLabel);

        Label instructionLabel = new Label("instruction: ", Label.LEFT);
        instructionLabel.reshape(285, 45 + 25, 100, 15);
        add(instructionLabel);

        Label addressLabel = new Label("address: ", Label.LEFT);
        addressLabel.reshape(285, 60 + 25, 85, 15);
        add(addressLabel);

        Label pageFaultLabel = new Label("page fault: ", Label.LEFT);
        pageFaultLabel.reshape(285, 90 + 25, 100, 15);
        add(pageFaultLabel);

        Label virtualPageLabel = new Label("virtual page: ", Label.LEFT);
        virtualPageLabel.reshape(285, 120 + 25, 110, 15);
        add(virtualPageLabel);

        Label physicalPageLabel = new Label("physical page: ", Label.LEFT);
        physicalPageLabel.reshape(285, 135 + 25, 110, 15);
        add(physicalPageLabel);

        Label inMemTimeLabel = new Label("inMemTime: ", Label.LEFT);
        inMemTimeLabel.reshape(285, 150 + 25, 110, 15);
        add(inMemTimeLabel);

        Label lastTouchTimeLabel = new Label("lastTouchTime: ", Label.LEFT);
        lastTouchTimeLabel.reshape(285, 165 + 25, 110, 15);
        add(lastTouchTimeLabel);

        Label agingLabel = new Label("aging: ", Label.LEFT);
        agingLabel.reshape(285, 180 + 25, 110, 15);
        add(agingLabel);

        for (int i = 0; i < 64; i++){
            Label l = pagesLabels.elementAt(i);
            l.reshape(70 + i / 32 * 140, (i % 32 + 2) * 15 + 25, 60, 15);
            l.setForeground(Color.red);
            l.setFont(new Font("Courier", 0, 10));
            add(l);
        }

        kernel.init(commands, config);

        show();
    }

    public void paintPage(Page page) {
        virtualPageValueLabel.setText(Integer.toString(page.id));
        physicalPageValueLabel.setText(Integer.toString(page.physical));
        inMemTimeValueLabel.setText(Integer.toString(page.inMemTime));
        lastTouchTimeValueLabel.setText(Integer.toString(page.lastTouchTime));
        agingValueLabel.setText(toBinary(page.aging, 8));
    }

    public static String toBinary(long n, int len)
    {
        String binary = "";
        for (long i = (1L << len - 1); i > 0; i = i / 2) {
            binary += (n & i) != 0 ? "1" : "0";
        }
        return binary;
    }

    public void setStatus(String status) {
        statusValueLabel.setText(status);
    }

    public void addPhysicalPage(int pageNum, int physicalPage) {
        if (physicalPage < 64) {
            pagesLabels.elementAt(physicalPage).setText("page " + pageNum);
        }
    }

    public void removePhysicalPage(int physicalPage) {
        if (physicalPage < 64)
            pagesLabels.elementAt(physicalPage).setText(null);
    }

    public boolean action(Event e, Object arg) {
        if (e.target == runButton) {
            setStatus("RUN");
            runButton.disable();
            stepButton.disable();
            resetButton.disable();
            kernel.run();
            setStatus("STOP");
            resetButton.enable();
            return true;
        } else if (e.target == stepButton) {
            setStatus("STEP");
            kernel.step();
            if (kernel.runCycles == kernel.runs) {
                stepButton.disable();
                runButton.disable();
            }
            setStatus("STOP");
            return true;
        } else if (e.target == resetButton) {
            kernel.reset();
            runButton.enable();
            stepButton.enable();
            return true;
        } else if (e.target == exitButton) {
            System.exit(0);
            return true;
        } else {
            for (int i = 0; i < 64; i++)
                if (e.target == pagesButtons.elementAt(i)) {
                    kernel.getPage(i);
                    return true;
                }
        }
        return false;
    }
}
