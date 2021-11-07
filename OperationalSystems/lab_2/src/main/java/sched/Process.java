package sched;

public class Process {
  public int cpu_time;
  public int cpu_done = 0;
  public int arrival_time;
  public int burst_time;
  public int burst_time_timer = 0;

  public int io_blocking_time;
  public int io_blocking_time_timer = 0;
  public boolean io_blocked = false;

  public int all_time;

  public Process(int cpu_time, int burst_time, int io_blocking_time, int arrival_time) {
    this.cpu_time = cpu_time;
    this.arrival_time= arrival_time;
    this.io_blocking_time = io_blocking_time;
    this.burst_time = burst_time;
  }

  public void print(int number){
    System.out.println("process " + number + " " + cpu_time + " " + io_blocking_time + " " + burst_time + " " + " " + arrival_time + " ");
  }

  public int remainingTime(){
    return cpu_time - cpu_done;
  }

  public boolean done(){
    return cpu_time == cpu_done;
  }
}
