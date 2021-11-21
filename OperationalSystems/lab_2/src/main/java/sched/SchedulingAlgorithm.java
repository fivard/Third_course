// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package sched;

import java.util.ArrayList;
import java.io.*;

public class SchedulingAlgorithm {
  private static int compTime = 0;

  public static void Run(int runtime, ArrayList<Process> processVector, Results result) {
    int currentProcess = 0;
    int size = processVector.size();
    int completed = 0;
    Process process;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch";
    result.schedulingName = "Shortest remaining time first";

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      currentProcess = getCurrentProcess(processVector);
      process = processVector.get(currentProcess);

      out.println("Process: " + currentProcess + " " + compTime +" registered... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

      while (compTime < runtime) {

        if (currentProcess != -1 && ((process.cpu_done == process.cpu_time && process.cpu_done > 0) || process.cpu_time == 0)) {
          process.all_time = compTime;
          completed++;
          out.println("Process: " + currentProcess + " " + compTime +" completed... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

          if (completed == size) {
            result.compuTime = compTime;
            out.close();
            return;
          }

          currentProcess = getCurrentProcess(processVector);
          if (currentProcess != -1) {
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cpu_time + " " + process.io_blocking_time + " " + process.cpu_done + ")");
          }
        }

        if (process.burst_time_timer == process.burst_time){
          out.println("Process: " + currentProcess + " " + compTime + " I/O blocked... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

          if (process.io_blocking_time > 0) {
            process.io_blocked = true;
            process.burst_time_timer = 0;
          }

          currentProcess = getCurrentProcess(processVector);
          if (currentProcess != -1) {
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cpu_time + " " + process.io_blocking_time + " " + process.cpu_done + ")");
          }
        }
        if (currentProcess != -1) {
          process.cpu_done++;
          process.burst_time_timer++;
        }

        boolean hasArrived = increaseCompTime(processVector, out);
        boolean hasUnblocked = increaseIOTimers(processVector, out);
        if (hasUnblocked || hasArrived){
          currentProcess = getCurrentProcess(processVector);
          if (currentProcess != -1) {
            process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " " + compTime + " registered... (" + process.cpu_time + " " + process.io_blocking_time + " " + process.cpu_done + ")");
          }
        }

      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = compTime;
    return;
  }

  private static int getCurrentProcess(ArrayList<Process> processVector){
    int currentProcess = -1;
    for (int i = 0; i < processVector.size(); i++)
      if (!processVector.get(i).io_blocked && processVector.get(i).arrival_time <= compTime && !processVector.get(i).done())
        currentProcess = i;
    if (currentProcess == -1)
      return currentProcess;
    Process process = processVector.get(currentProcess);
    for (int i = 0; i < processVector.size(); i++) {
      Process temp_process = processVector.get(i);
      if (!temp_process.io_blocked && !temp_process.done() && temp_process.remainingTime() < process.remainingTime()
              && compTime >= temp_process.arrival_time){
        currentProcess = i;
      }
    }
    return currentProcess;
  }

  private static boolean increaseCompTime(ArrayList<Process> processVector, PrintStream out){
    compTime++;
    boolean arrivedProcess = false;

    for (int i = 0; i < processVector.size(); i++){
      if (processVector.get(i).arrival_time == compTime) {
        arrivedProcess = true;
        out.println("Process: " + i + " " + compTime + " has arrived... (" + processVector.get(i).cpu_time + " " + processVector.get(i).io_blocking_time + " " + processVector.get(i).cpu_done + ")");
      }
    }

    return arrivedProcess;
  }

  private static boolean increaseIOTimers(ArrayList<Process> processVector, PrintStream out){
    boolean finishedProcess = false;

    for (int i = 0; i < processVector.size(); i++) {
      if (processVector.get(i).io_blocked) {
        processVector.get(i).io_blocking_time_timer++;
        if (processVector.get(i).io_blocking_time_timer == processVector.get(i).io_blocking_time) {
          processVector.get(i).io_blocked = false;
          processVector.get(i).io_blocking_time_timer = 0;
          finishedProcess = true;
          out.println("Process: " + i + " " + compTime +" unblocked... (" + processVector.get(i).cpu_time + " " +  processVector.get(i).io_blocking_time + " " + processVector.get(i).cpu_done + ")");
        }
      }
    }

    return finishedProcess;
  }
}
