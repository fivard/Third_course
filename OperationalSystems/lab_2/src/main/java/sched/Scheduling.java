// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06
package sched;

import java.io.*;
import java.util.*;

public class Scheduling {

    private static int processnum = 3;
    private static int runtime = 1000;
    private static final ArrayList<Process> processVector = new ArrayList<>();
    private static final Results result = new Results("null","null",0);

    private static void Init(String file) {
        File f = new File(file);
        String line;

        try {
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("process")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    int cpu_time = Common.s2i(st.nextToken(" "));
                    int burst_time = Common.s2i(st.nextToken(" "));
                    int io_blocking_time = Common.s2i(st.nextToken(" "));
                    int arrival_time = Common.s2i(st.nextToken(" "));
                    processVector.add(new Process(cpu_time, burst_time, io_blocking_time, arrival_time));
                }
                if (line.startsWith("runtime")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    runtime = Common.s2i(st.nextToken());
                }
            }
            in.close();
        } catch (IOException e) { /* Handle exceptions */ }
    }

    public static void debug() {
        int size = processVector.size();
        for (int i = 0; i < size; i++) {
            Process process = processVector.get(i);
            process.print(i);
        }
        System.out.println("runtime " + runtime);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: 'java Scheduling <INIT FILE>'");
            System.exit(-1);
        }
        File f = new File(args[0]);
        if (!(f.exists())) {
            System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(f.canRead())) {
            System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
            System.exit(-1);
        }
        System.out.println("Working...");
        Init(args[0]);
//        processVector.add(new Process(500, 120, 100, 0));
//        processVector.add(new Process(1500, 500, 30, 0));
//        processVector.add(new Process(90, 30, 100, 100));
//        runtime = 10000;
        SchedulingAlgorithm.Run(runtime, processVector, result);
        try {
            String resultsFile = "Summary-Results";
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            out.println("Scheduling Type: " + result.schedulingType);
            out.println("Scheduling Name: " + result.schedulingName);
            out.println("Simulation Run Time: " + result.compuTime);
            out.println("Process #\tCPU Time\tCPU Completed\tArrival time\tAll time");
            for (int i = 0; i < processVector.size(); i++) {
                Process process = processVector.get(i);

                out.print(i);

                if (i < 100) {
                    out.print("\t\t\t");
                } else {
                    out.print("\t\t");
                }

                out.print(process.cpu_time);

                if (process.cpu_time < 100)
                    out.print(" (ms)\t\t");
                else
                    out.print(" (ms)\t");

                out.print(process.cpu_done);

                if (process.cpu_done < 100)
                    out.print(" (ms)\t\t\t");
                else
                    out.print(" (ms)\t\t");

                out.print(process.arrival_time);

                if (process.arrival_time > 10)
                    out.print(" (ms)\t\t");
                else
                    out.print(" (ms)\t\t\t");

                out.println(process.all_time);
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        System.out.println("Completed.");
    }
}

