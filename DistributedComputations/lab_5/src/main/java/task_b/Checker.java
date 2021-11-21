package task_b;

import java.util.ArrayList;

import static java.lang.System.*;

public class Checker implements Runnable {
    private final ArrayList<Changer> changerList;
    private boolean stopped;

    public Checker(ArrayList<Changer> changerList) {
        this.changerList = changerList;
        this.stopped = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public int countAB(String str) {
        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == 'A' || str.charAt(i) == 'B'){
                count++;
            }
        }
        return count;
    }

    private boolean checkEquality(){
        int[] sizes = new int[20];
        for (Changer changer : changerList) {
            sizes[countAB(changer.currentString)]++;
        }
        return sizes[4] >= 3;
    }


    @Override
    public void run() {
        out.println("Reached barrier");
        stopped = checkEquality();
    }
}