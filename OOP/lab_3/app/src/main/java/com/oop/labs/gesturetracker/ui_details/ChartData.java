package com.oop.labs.gesturetracker.ui_details;

import android.hardware.SensorManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartData {
    public static final int CHART_HISTORY_LENGTH = 64;
    public static final int AVERAGE_WINDOW_LENGTH = 1;
    public static final int SENSOR_DELAY_FASTEST = SensorManager.SENSOR_DELAY_FASTEST;

    private LineChart line;
    private LineData data;
    private LineDataSet[] lineDataSets;
    private LineChartManager chartManager;
    private List<List<Float>> historyList;

    public void init() {
        data = new LineData();
        historyList = new ArrayList<>(Arrays.asList(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        ));
        line.setData(data);
        line.setAutoScaleMinMaxEnabled(true);
        line.getAxisLeft().setDrawLabels(false);

        Description emptyDescription = new Description();
        emptyDescription.setText("");
        line.setDescription(emptyDescription);

        lineDataSets = new LineDataSet[]{new LineDataSet(null, "X"), new LineDataSet(null, "Y"), new LineDataSet(null, "Z")};

        for (LineDataSet lineDataSet : lineDataSets) {
            data.addDataSet(lineDataSet);
        }
    }

    public void setLine(LineChart line) {
        this.line = line;
    }

    public LineDataSet[] getLineDataSets() {
        return lineDataSets;
    }

    public LineData getData() {
        return data;
    }

    public void setChartManager(LineChartManager chartManager) {
        this.chartManager = chartManager;
    }

    public LineChart getLine() {
        return line;
    }

    public List<List<Float>> getHistoryList() {
        return historyList;
    }

    public LineChartManager getChartManager() {
        return chartManager;
    }
}
