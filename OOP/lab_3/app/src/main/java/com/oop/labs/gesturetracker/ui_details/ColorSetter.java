package com.oop.labs.gesturetracker.ui_details;

import com.github.mikephil.charting.data.LineDataSet;

public class ColorSetter {
    public static void color(final LineDataSet[] lineDataSets, final int[] color) {
        for (int i = 0; i < lineDataSets.length; i++) {
            ColorSetter.color(lineDataSets[i], color[i]);
        }
    }

    private static void color(final LineDataSet lineDataSet, final int color) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColorHole(color);
        lineDataSet.setCircleColor(color);
    }
}
