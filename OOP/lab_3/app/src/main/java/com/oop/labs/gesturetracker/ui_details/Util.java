package com.oop.labs.gesturetracker.ui_details;

import java.util.List;

public class Util {
    public static double[] getFloatPrimitiveArrayFromList(final List<Float> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
