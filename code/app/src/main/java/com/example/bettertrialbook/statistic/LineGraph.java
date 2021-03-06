/* Credit details can be also be found in README.md
 * Credit: Mar 31, 2021, PhilJay MPAndroidChart, Apache 2.0.
 * https://github.com/PhilJay/MPAndroidChart
 * Used the library for the line chart plotting
 */
package com.example.bettertrialbook.statistic;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * Activity for displaying the line graph feature
 */
public class LineGraph extends AppCompatActivity {
    private String experimentType;
    private ArrayList<Trial> trialDataList;
    private LineGraphInfo lineGraphInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        experimentType = intent.getStringExtra("Type");
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

        // Variables
        lineGraphInfo = new LineGraphInfo(trialDataList, experimentType);

        // Layout Variables
        Button mean = findViewById(R.id.MeanOverTime);
        Button median = findViewById(R.id.MedianOverTime);
        Button stdDev = findViewById(R.id.StdDevOverTime);
        LineChart lineChart = findViewById(R.id.LineChart);
        TextView trialDisplay = findViewById(R.id.Trial_Display);
        Button resultOverTime = findViewById(R.id.ResultOverTime);

        // Layout Settings
        if (experimentType.equals(Extras.COUNT_TYPE)) {
            // Mean, Median and stdDev in Count Trials are meaningless
            mean.setVisibility(View.INVISIBLE);
            median.setVisibility(View.INVISIBLE);
            stdDev.setVisibility(View.INVISIBLE);
            trialDisplay.setVisibility(View.INVISIBLE);
        } else {
            resultOverTime.setVisibility(View.INVISIBLE);
        }

        if (trialDataList.size() > 0) {
            lineChartSetting(lineChart, trialDataList.size());

            mean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createLineChart(lineChart, "Mean", trialDataList);
                    lineChart.invalidate();
                }
            });

            median.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createLineChart(lineChart, "Median", trialDataList);
                    lineChart.invalidate();
                }
            });

            stdDev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createLineChart(lineChart, "StdDev", trialDataList);
                    lineChart.invalidate();
                }
            });

            resultOverTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createLineChart(lineChart, "Result", trialDataList);
                    lineChart.invalidate();
                }
            });
        }
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

        // Create the data corresponding to the requirement
        if (category.equals("Mean")) {
            data = lineGraphInfo.MeanOverTime();
        } else if (category.equals(("Median"))) {
            data = lineGraphInfo.MedianOverTime();
        } else if (category.equals("StdDev")) {
            data = lineGraphInfo.StdDevOverTime(lineGraphInfo.MeanOverTime());
        } else if (category.equals("Result")) {
            data = lineGraphInfo.ResultOverTime();
        } else {
            data = null;
        }

        if (data!= null && category.equals("Result")) {
            for (int i = 0; i < data.size(); i++) {
                values.add(new Entry(i, data.get(i).floatValue()));
            }
        }
        else{
            values.add(new Entry(0, 0));
            for (int i = 1; i <= data.size(); i++) {
                values.add(new Entry(i, data.get(index).floatValue()));
                index += 1;
            }
        }

        LineDataSet dataset = new LineDataSet(values, "Trial");
        // Settings for the dataset
        dataset.setLineWidth(3f);
        dataset.setDrawValues(false);
        dataset.setCircleRadius(6f);
        dataset.setCircleHoleRadius(5f);

        dataSets.add(dataset);

        // Settings for the line to plot
        LineData lineData = new LineData(dataset);
        lineChart.setData(lineData);

        // Maker for the dataSet
        IMarker marker = new Marker(this, R.layout.custom_marker_view, trialDataList, data, experimentType);
        lineChart.setMarker(marker);

        lineChart.invalidate();
    }


    public void lineChartSetting(LineChart lineChart, int size) {
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();

        // LineChart setting
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setExtraBottomOffset(50);
        lineChart.setExtraRightOffset(50);
        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        // x-axis setting
        if (!experimentType.equals(Extras.COUNT_TYPE)) {
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(size);
            xAxis.setTextSize(10f);
            xAxis.setLabelCount(size);
            xAxis.setDrawGridLines(false);
            xAxis.setYOffset(20);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        } else {
            xAxis.setEnabled(false);
        }

        // Right y-axis setting
        rightAxis.setEnabled(false);

        // Left y-axis setting
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