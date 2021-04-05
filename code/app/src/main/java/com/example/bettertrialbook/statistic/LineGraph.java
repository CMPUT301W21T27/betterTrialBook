package com.example.bettertrialbook.statistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineGraph extends AppCompatActivity {
    private ArrayList<Trial> trialDataList;
    private final LineGraphInfo lineGraphInfo = new LineGraphInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

        LineChart lineChart = findViewById(R.id.LineChart);

        // Testing: Plot the Graph
        createLineChart(lineChart, "Mean", trialDataList);
        lineChartSetting(lineChart, trialDataList.size());

    }

    public void createLineChart(LineChart lineChart, String category, ArrayList<Trial> trials) {
        int index = 0;
        ArrayList<Double> data;
        ArrayList<Entry> values = new ArrayList<>();
        String[] xLabels = new String[trials.size()];
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        // Create the labels for x-Axis (Respective number of the trial)
        for (int i = 0; i < trials.size(); i++) {
            xLabels[i] = String.valueOf(i);
        }

        // Create the data corresponding to the
        if (category.equals("Mean")) {
            data = lineGraphInfo.MeanOverTime(trials);
        } else if (category.equals(("Median"))) {
            data = lineGraphInfo.MedianOverTime(trials);
        } else if (category.equals("StdDev")) {
            data = lineGraphInfo.StdDevOverTime(trials, lineGraphInfo.MeanOverTime(trials));
        } else {
            data = null;
        }

        if (data != null) {
            values.add(new Entry(0, 0));
            for (int i = 1; i <= data.size(); i++) {
                values.add(new Entry(i, data.get(index).floatValue()));
                index += 1;
            }
        }

        LineDataSet dataset = new LineDataSet(values, "Trial");
        dataset.setLineWidth(3f);
        dataset.setValueTextSize(12f);

        dataSets.add(dataset);

        LineData lineData = new LineData(dataset);
        lineChart.setData(lineData);
    }


    public void lineChartSetting(LineChart lineChart, int size) {
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();


        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setExtraBottomOffset(50);

        /* Make a marker and make the x-axis name to be dynamical.
        lineChart.getDescription().setPosition(600, 1400);
        lineChart.getDescription().setText("Trial");
        lineChart.getDescription().setTextSize(25);
        */

        xAxis.setTextSize(15f);
        xAxis.setLabelCount(size);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(size);
        xAxis.setDrawGridLines(false);
        xAxis.setYOffset(20);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        rightAxis.setEnabled(false);

        leftAxis.setTextSize(15f);
        leftAxis.setLabelCount(size);
        leftAxis.setAxisMinimum(0);
        leftAxis.setGridLineWidth(1);
        leftAxis.setDrawZeroLine(false);
    }

    // -----------------------------------------Used For ToolBar------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistic_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ExperimentOverView) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}