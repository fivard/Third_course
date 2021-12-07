package com.oop.labs.gesturetracker.ui_details;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Arrays;

public class LineChartManager {

    private final LineChart lineChart;
    private final LineDataSet[] dataSets;

    private final int window;
    private final float[] buffer;
    private int offset;

    public LineChartManager(final LineChart lineChart, final int window, final LineDataSet... dataSets) {
        this.lineChart = lineChart;
        this.dataSets = dataSets;
        this.window = window;
        this.buffer = new float[dataSets.length];
        this.offset = 0;
    }

    public final void onUpdateChart(final float... vertical) {
        offset++;
        for (int i = 0; i < vertical.length; i++) {
            buffer[i] += vertical[i];
        }
        if (offset % window == 0) {
            this.onAggregateUpdate(buffer);
            Arrays.fill(buffer, 0.0f);
        }
    }

    public void onAggregateUpdate(final float[] aggregate) {
        for (int i = 0; i < this.getDataSets().length; i++) {
            float average = this.getBuffer()[i] / window;
            LineDataSet lineDataSet = dataSets[i];
            aggregate[i] = average;
            lineDataSet.removeFirst();
            lineDataSet.addEntry(new Entry(offset, average));
        }
        this.getLineChart().invalidate();
    }

    private LineChart getLineChart() {
        return lineChart;
    }

    private LineDataSet[] getDataSets() {
        return dataSets;
    }

    private float[] getBuffer() {
        return buffer;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
