package com.oop.labs.gesturetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.oop.labs.gesturetracker.dtw.DTWAlgorithm;
import com.oop.labs.gesturetracker.ui_details.ChartData;
import com.oop.labs.gesturetracker.ui_details.GestureRecognitionMode;
import com.oop.labs.gesturetracker.ui_details.ColorSetter;
import com.oop.labs.gesturetracker.ui_details.LineChartManager;
import com.oop.labs.gesturetracker.ui_details.Util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private GestureRecognitionMode gestureRecognitionMode;
    private boolean responsive;
    private SensorManager sensorManager;

    private Button gestureInputTracker;
    private TextView modeTitle;
    private TextView modeDescription;
    private SwitchCompat modeSwitch;
    private TextView resultView;

    private final ChartData trainingChart = new ChartData();
    private final ChartData recognitionChart = new ChartData();

    private void initChart(ChartData chart, LineChart xmlElement){
        chart.setLine(xmlElement);
        chart.init();

        onInitializeData(chart.getLineDataSets());
        chart.getData().notifyDataChanged();
        ColorSetter.color(chart.getLineDataSets(), new int[]{Color.RED, Color.GREEN, Color.BLUE});
        chart.setChartManager(new LineChartManager(chart.getLine(),
                ChartData.AVERAGE_WINDOW_LENGTH, chart.getLineDataSets()) {
            @Override
            public final void onAggregateUpdate(float[] aggregate) {
                super.onAggregateUpdate(aggregate);

                for (int i = 0; i < aggregate.length; i++) {
                    chart.getHistoryList().get(i).add(aggregate[i]);
                }
            }
        });
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureRecognitionMode = GestureRecognitionMode.TRAINING;
        responsive = false;

        setContentView(R.layout.main_activity);
        gestureInputTracker = findViewById(R.id.gesture_input);
        resultView = findViewById(R.id.DTW_result);
        modeTitle = findViewById(R.id.label_mode);
        modeDescription = findViewById(R.id.mode_info);
        modeSwitch = findViewById(R.id.mode_switcher);
        setSwitchListener();

        initChart(trainingChart, findViewById(R.id.train_chart));
        initChart(recognitionChart, findViewById(R.id.recognition_chart));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setGestureInputListener();
        onStopTrackingGesture();
    }

    private void onInitializeData(LineDataSet[] dataSet) {
        for (LineDataSet lineDataSet : dataSet) {
            lineDataSet.clear();
        }
        for (int i = 0; i < ChartData.CHART_HISTORY_LENGTH; i++) {
            Entry entry = new Entry(i, 0);
            for (LineDataSet lineDataSet : dataSet) {
                lineDataSet.addEntry(entry);
            }
        }
    }

    private void onStopTrackingGesture() {
        gestureInputTracker.setBackgroundColor(ContextCompat.getColor(this,
                R.color.colorTransparent));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setGestureInputListener() {
        gestureInputTracker.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                modeSwitch.setEnabled(false);
                if (gestureRecognitionMode == GestureRecognitionMode.TRAINING) {
                    resetChartData(trainingChart);
                } else if (gestureRecognitionMode == GestureRecognitionMode.RECOGNITION) {
                    resetChartData(recognitionChart);
                }
                responsive = true;
            } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                responsive = false;
                onStopTrackingGesture();
                if (gestureRecognitionMode == GestureRecognitionMode.RECOGNITION) {
                    new Thread(() -> {
                        double[] DTWResultsByAxis = new double[3];
                        for (int i = 0; i < 3; i++) {
                            double[] training = Util
                                    .getFloatPrimitiveArrayFromList(trainingChart
                                            .getHistoryList().get(i));
                            double[] recognition = Util
                                    .getFloatPrimitiveArrayFromList(recognitionChart
                                            .getHistoryList().get(i));
                            Log.i("myTag", Arrays.toString(training));
                            DTWResultsByAxis[i] = DTWAlgorithm.compute(recognition, training);
                        }
                        runOnUiThread(() -> {
                            String result = "D = (X:" + DTWResultsByAxis[0]
                                    + ", Y:" + DTWResultsByAxis[1]
                                    + ", Z:" + DTWResultsByAxis[2] + ")";
                            resultView.setText(result);
                        });
                    }).start();
                }
                modeSwitch.setEnabled(true);
            }
            return true;
        });
    }

    private void resetChartData(ChartData chart) {
        for (List<Float> element : chart.getHistoryList()) {
            element.clear();
        }
        chart.getChartManager().setOffset(0);
        onInitializeData(chart.getLineDataSets());
    }

    private void setSwitchListener() {
        modeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            setGestureRecognitionMode(isChecked ? GestureRecognitionMode.RECOGNITION
                    : GestureRecognitionMode.TRAINING);
            getModeTitle().setText(isChecked ? R.string.mode_recognition : R.string.mode_training);
            getModeDescription().setText(isChecked ? R.string.mode_recognition_info
                    : R.string.mode_training_info);
        });
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (isResponsive()) {
                if (gestureRecognitionMode == GestureRecognitionMode.TRAINING) {
                    trainingChart.getChartManager().onUpdateChart(sensorEvent.values);
                } else if (gestureRecognitionMode == GestureRecognitionMode.RECOGNITION) {
                    recognitionChart.getChartManager().onUpdateChart(sensorEvent.values);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getSensorManager().registerListener(
                this, this.getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                ChartData.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.getSensorManager().unregisterListener(this);
    }

    public boolean isResponsive() {
        return responsive;
    }

    public TextView getModeTitle() {
        return modeTitle;
    }

    public TextView getModeDescription() {
        return modeDescription;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setGestureRecognitionMode(GestureRecognitionMode gestureRecognitionMode) {
        this.gestureRecognitionMode = gestureRecognitionMode;
    }
}
