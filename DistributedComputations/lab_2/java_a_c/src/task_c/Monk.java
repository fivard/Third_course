package task_c;

import java.util.Random;

public class Monk implements Comparable {
    private Integer energy;
    private String monastery;

    public Monk() {
        Random r = new Random();
        energy = r.nextInt(100);
        monastery = (r.nextInt(2) == 0) ? "Guan-un" : "Guan-yan";
    }

    @Override
    public String toString() {
        return "Monk from " + monastery +" with energy: "+energy;
    }

    public int compareTo(Object o) {
        Monk other = (Monk)o;
        if(this.energy > other.energy) {
            return 1;
        } else if (this.energy < other.energy){
            return -1;
        } else {
            return 0;
        }
    }

    static Monk max(Monk first, Monk second){
        if(first.energy > second.energy){
            return first;
        } else {
            return second;
        }
    }
}