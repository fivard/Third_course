package memory;

import java.lang.Thread;
import java.io.*;
import java.util.*;

public class Kernel extends Thread {
    private static final int virtualPageNum = 63;

    public static final int countAddresses = 10;
    private String output = null;
    private static final String lineSeparator =
            System.getProperty("line.separator");
    private String commandFile;
    private String configFile;
    private ControlPanel controlPanel;
    private final Vector<Page> memVector = new Vector<>();
    private final Vector<Instruction> instructVector = new Vector<>();
    private boolean doStdoutLog = false;
    private boolean doFileLog = false;
    public int runs;
    public int runCycles;

    public void init(String commands, String config) {
        File f = new File(commands);
        commandFile = commands;
        configFile = config;
        String line;
        String tmp = null;
        String command = "";
        byte R = 0;
        byte M = 0;
        int i = 0;
        int j = 0;
        int id = 0;
        int physical = 0;
        int physical_count = 0;
        int inMemTime = 0;
        int lastTouchTime = 0;
        int map_count = 0;

        for (i = 0; i <= virtualPageNum; i++) {
            memVector.addElement(new Page(i, -1, R, M, 0, 0));
        }
        if (config != null) {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(f));
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("memset")) {
                        StringTokenizer st = new StringTokenizer(line);
                        st.nextToken();
                        while (st.hasMoreTokens()) {
                            id = Common.s2i(st.nextToken());
                            tmp = st.nextToken();
                            if (tmp.startsWith("x")) {
                                physical = -1;
                            } else {
                                physical = Common.s2i(tmp);
                            }
                            if ((0 > id || id > virtualPageNum) || (-1 > physical || physical > ((virtualPageNum - 1) / 2))) {
                                System.out.println("MemoryManagement: Invalid page value in " + config);
                                System.exit(-1);
                            }
                            inMemTime = Common.s2i(st.nextToken());
                            if (inMemTime < 0) {
                                System.out.println("MemoryManagement: Invalid inMemTime in " + config);
                                System.exit(-1);
                            }
                            lastTouchTime = Common.s2i(st.nextToken());
                            if (lastTouchTime < 0) {
                                System.out.println("MemoryManagement: Invalid lastTouchTime in " + config);
                                System.exit(-1);
                            }
                            Page page = (Page) memVector.elementAt(id);
                            page.physical = physical;
                            page.inMemTime = inMemTime;
                            page.lastTouchTime = lastTouchTime;
                        }
                    }
                    if (line.startsWith("enable_logging")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            if (st.nextToken().startsWith("true")) {
                                doStdoutLog = true;
                            }
                        }
                    }
                    if (line.startsWith("log_file")) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            tmp = st.nextToken();
                        }
                        if (tmp.startsWith("log_file")) {
                            doFileLog = false;
                            output = "tracefile";
                        } else {
                            doFileLog = true;
                            doStdoutLog = false;
                            output = tmp;
                        }
                    }
                }
                in.close();
            } catch (IOException e) { /* Handle exceptions */ }
        }
        f = new File(commands);
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("READ") || line.startsWith("WRITE")) {
                    if (line.startsWith("READ")) {
                        command = "READ";
                    }
                    if (line.startsWith("WRITE")) {
                        command = "WRITE";
                    }
                    Vector<Integer> addresses = new Vector<>();
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    for (i = 0; i < countAddresses; i++) {
                        addresses.add(Integer.parseInt(st.nextToken()));
                    }
                    instructVector.addElement(new Instruction(command, addresses));
                }
            }
            in.close();
        } catch (IOException e) { /* Handle exceptions */ }
        runCycles = instructVector.size();
        if (runCycles < 1) {
            System.out.println("MemoryManagement: no instructions present for execution.");
            System.exit(-1);
        }
        if (doFileLog) {
            File trace = new File(output);
            trace.delete();
        }
        runs = 0;
        for (i = 0; i < virtualPageNum; i++) {
            Page page =  memVector.elementAt(i);
            if (page.physical != -1) {
                map_count++;
            }
            for (j = 0; j < virtualPageNum; j++) {
                Page tmp_page = (Page) memVector.elementAt(j);
                if (tmp_page.physical == page.physical && page.physical >= 0) {
                    physical_count++;
                }
            }
            if (physical_count > 1) {
                System.out.println("MemoryManagement: Duplicate physical page's in " + config);
                System.exit(-1);
            }
            physical_count = 0;
        }
        if (map_count < (virtualPageNum + 1) / 2) {
            for (i = 0; i < virtualPageNum; i++) {
                Page page = memVector.elementAt(i);
                if (page.physical == -1 && map_count < (virtualPageNum + 1) / 2) {
                    page.physical = i;
                    map_count++;
                }
            }
        }
        for (i = 0; i < virtualPageNum; i++) {
            Page page = memVector.elementAt(i);
            if (page.physical == -1) {
                controlPanel.removePhysicalPage(i);
            } else {
                controlPanel.addPhysicalPage(i, page.physical);
            }
        }
    }

    public void setControlPanel(ControlPanel newControlPanel) {
        controlPanel = newControlPanel;
    }

    public void getPage(int pageNum) {
        Page page = memVector.elementAt(pageNum);
        controlPanel.paintPage(page);
    }

    private void printLogFile(String message) {
        String line;
        String temp = "";

        File trace = new File(output);
        if (trace.exists()) {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(output));
                while ((line = in.readLine()) != null) {
                    temp = temp + line + lineSeparator;
                }
                in.close();
            } catch (IOException e) {
                /* Do nothing */
            }
        }
        try {
            PrintStream out = new PrintStream(new FileOutputStream(output));
            out.print(temp);
            out.print(message);
            out.close();
        } catch (IOException e) {
            /* Do nothing */
        }
    }

    public void run() {
        step();
        while (runs != runCycles) {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                /* Do nothing */
            }
            step();
        }
    }

    public void step() {

        Instruction instruct = instructVector.elementAt(runs);
        Vector<Integer> overwrittenPages = new Vector<>();

        for (int i = 0; i < countAddresses; i++) {
            int pageNum = instruct.addresses.elementAt(i);
            controlPanel.instructionValueLabel.setText(instruct.inst);
            controlPanel.addressValueLabel.setText(instruct.addressesToString());

            if (controlPanel.pageFaultValueLabel.getText().equals("YES")) {
                controlPanel.pageFaultValueLabel.setText("NO");
            }
            if (instruct.inst.startsWith("READ")) {
                Page page = memVector.elementAt(pageNum);
                if (page.physical == -1) {
                    if (doFileLog) {
                        printLogFile("READ " + instruct.addresses.elementAt(i) + " ... page fault");
                    }
                    if (doStdoutLog) {
                        System.out.println("READ " + instruct.addresses.elementAt(i) + " ... page fault");
                    }
                    pageNum = PageFault.replacePage(memVector, virtualPageNum, pageNum, controlPanel);
                    page = memVector.elementAt(pageNum);
                    controlPanel.pageFaultValueLabel.setText("YES");
                } else {
                    if (doFileLog) {
                        printLogFile("READ " + instruct.addresses.elementAt(i) + " ... okay");
                    }
                    if (doStdoutLog) {
                        System.out.println("READ " + instruct.addresses.elementAt(i) + " ... okay");
                    }
                }
                page.lastTouchTime = 0;
                page.aging = 0b10000000 | page.aging >> 1;
            }
            if (instruct.inst.startsWith("WRITE")) {
                Page page = memVector.elementAt(pageNum);
                if (page.physical == -1) {
                    if (doFileLog) {
                        printLogFile("WRITE " + instruct.addresses.elementAt(i) + " ... page fault");
                    }
                    if (doStdoutLog) {
                        System.out.println("WRITE " + instruct.addresses.elementAt(i) + " ... page fault");
                    }
                    pageNum = PageFault.replacePage(memVector, virtualPageNum, pageNum, controlPanel);
                    page = memVector.elementAt(pageNum);
                    controlPanel.pageFaultValueLabel.setText("YES");
                } else {
                    if (doFileLog) {
                        printLogFile("WRITE " + instruct.addresses.elementAt(i) + " ... okay");
                    }
                    if (doStdoutLog) {
                        System.out.println("WRITE " + instruct.addresses.elementAt(i) + " ... okay");
                    }
                }
                page.lastTouchTime = 0;
                page.aging = 0b10000000 | page.aging >> 1;
            }
            overwrittenPages.add(pageNum);
        }
        for (int i = 0; i < virtualPageNum; i++) {
            Page page = memVector.elementAt(i);
            if (page.physical != -1) {
                page.inMemTime = page.inMemTime + 10;
                page.lastTouchTime = page.lastTouchTime + 10;
                boolean inOverwrittenPages = false;
                for (int j = 0; j < countAddresses; j++)
                    if (i == overwrittenPages.elementAt(j))
                        inOverwrittenPages = true;

                if (!inOverwrittenPages){
                    page.aging = page.aging >> 1;
                }
            } else {
                page.aging = 0;
            }

        }
        runs++;
        getPage(instruct.addresses.elementAt(countAddresses-1));
        controlPanel.timeValueLabel.setText(runs * 10 + " (ns)");
    }

    public void reset() {
        memVector.removeAllElements();
        instructVector.removeAllElements();
        controlPanel.statusValueLabel.setText("STOP");
        controlPanel.timeValueLabel.setText("0");
        controlPanel.instructionValueLabel.setText("NONE");
        controlPanel.addressValueLabel.setText("NULL");
        controlPanel.pageFaultValueLabel.setText("NO");
        controlPanel.virtualPageValueLabel.setText("x");
        controlPanel.physicalPageValueLabel.setText("0");
        controlPanel.inMemTimeValueLabel.setText("0");
        controlPanel.lastTouchTimeValueLabel.setText("0");
        init(commandFile, configFile);
    }
}
