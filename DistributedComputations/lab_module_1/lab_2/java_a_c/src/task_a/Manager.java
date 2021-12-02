package task_a;

import java.util.Random;

public class Manager {
    private boolean[][] forest;
    private int currentTask = 0;
    private boolean foundBear = false;

    public Manager() {
        forest = new boolean[100][100];
        locateBear();
    }
    private void locateBear() {
        Random r = new Random();
        forest[r.nextInt(99)][r.nextInt(99)] = true;
    }

    public boolean isFoundBear() {
        return foundBear;
    }

    public void setFoundBear(boolean foundBear) {
        this.foundBear = foundBear;
    }

    public synchronized Task getTask() {
        if(currentTask < forest.length) {
            return new Task(forest[currentTask++], currentTask);
        }
        return null;
    }

    public class Task{
        private boolean [] area;
        private int y;

        public Task(boolean[] area, int y) {
            this.area = area;
            this.y = y;
        }

        public boolean[] getArea() {
            return area;
        }

        public int getY() {
            return y;
        }
    }
}
