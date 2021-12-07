package com.oop.labs.gesturetracker.dtw;

class WPStructure {
    private int i;
    private int j;
    private double value;

    WPStructure(int i, int j, double value) {
        this.i = i;
        this.j = j;
        this.value = value;
    }

    int getI() {
        return i;
    }

    int getJ() {
        return j;
    }

    double getValue() {
        return value;
    }
}
