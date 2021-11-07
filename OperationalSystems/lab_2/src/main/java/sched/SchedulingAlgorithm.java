// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package sched;

import java.util.ArrayList;
import java.io.*;

public class SchedulingAlgorithm {

  public static void Run(int runtime, ArrayList<Process> processVector, Results result) {
    int comptime = 0;
    int currentProcess = 0;
    int size = processVector.size();
    int completed = 0;
    Process process;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch";
    result.schedulingName = "Shortest remaining time first";

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      currentProcess = getCurrentProcess(processVector, comptime);
      process = processVector.get(currentProcess);

      out.println("Process: " + currentProcess + " " + comptime +" registered... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

      while (comptime < runtime) {

        if ((process.cpu_done == process.cpu_time && process.cpu_done > 0) || process.cpu_time == 0) {
          process.all_time = comptime;
          completed++;
          out.println("Process: " + currentProcess + " " + comptime +" completed... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return;
          }

          currentProcess = getCurrentProcess(processVector, comptime);
          process = processVector.get(currentProcess);
          out.println("Process: " + currentProcess + " " + comptime +" registered... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");
        }

        if (process.burst_time_timer == process.burst_time){
          out.println("Process: " + currentProcess + " " + comptime + " I/O blocked... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");

          if (process.io_blocking_time > 0) {
            process.io_blocked = true;
            process.burst_time_timer = 0;
          }

          currentProcess = getCurrentProcess(processVector, comptime);
          process =  processVector.get(currentProcess);
          out.println("Process: " + currentProcess + " " + comptime +" registered... (" + process.cpu_time + " " +  process.io_blocking_time + " " + process.cpu_done + ")");
        }

        process.cpu_done++;
        process.burst_time_timer++;
        comptime++;
        increaseIOTimers(processVector);
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return;
  }

  private static int getCurrentProcess(ArrayList<Process> processVector, int comptime){
    int currentProcess = 0;
    for (int i = 0; i < processVector.size(); i++)
      if (!processVector.get(i).io_blocked && processVector.get(i).arrival_time <= comptime && !processVector.get(i).done())
        currentProcess = i;
    Process process = processVector.get(currentProcess);
    for (int i = 0; i < processVector.size(); i++) {
      Process temp_process = processVector.get(i);
      if (!temp_process.io_blocked && !temp_process.done() && temp_process.remainingTime() < process.remainingTime()
              && comptime >= temp_process.arrival_time){
        currentProcess = i;
      }
    }
    return currentProcess;
  }

  private static void increaseIOTimers(ArrayList<Process> processVector){
    for (Process process : processVector) {
      if (process.io_blocked) {
        process.io_blocking_time_timer++;
        if (process.io_blocking_time_timer == process.io_blocking_time) {
          process.io_blocked = false;
          process.io_blocking_time_timer = 0;
        }
      }
    }
  }
}
