/* Credit details can be also be found in README.md
 * Credit: Mar 29, 2021, PhilJay MPAndroidChart, Apache 2.0.
 * https://github.com/PhilJay/MPAndroidChart
 * Used the library for the histogram plotting
 */
package com.example.bettertrialbook.statistic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity for displaying the histogram feature
 */
public class Histogram extends AppCompatActivity {
    String experimentType;
    private HistogramInfo histogramInfo;
    private ArrayList<Trial> trialDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_histogram);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        experimentType = intent.getStringExtra("Type");
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

        // Variables
        histogramInfo = new HistogramInfo(trialDataList, experimentType);
        ArrayList<String> labels = histogramInfo.getLabels();

        // Layout Variables
        BarChart barChart = findViewById(R.id.Bar_Chart);
        TextView binDisplay = findViewById(R.id.Bin_Display);

        // Layout setting
        if (experimentType.equals(Extras.COUNT_TYPE)) {
            binDisplay.setVisibility(View.INVISIBLE);
        }
        else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
            binDisplay.setVisibility(View.INVISIBLE);
        }

        // Histogram Graph plot and setting
        if (trialDataList.size() > 0) {
            barChartSetting(barChart, labels);
            plotTheGraph(barChart);
        }
    }

    // This method is used to handle the plotting the data of the histogram
    public void plotTheGraph(BarChart barChart) {
        ArrayList<Integer> binFrequency;
        binFrequency = histogramInfo.collectFrequency();

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < binFrequency.size(); i++) {
            barEntries.add(new BarEntry(i, binFrequency.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Frequency");
        barDataSet.setValueTextSize(20f);
        barDataSet.setColor(Color.RED);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    // This method is used to handle the setting of the histogram
    public void barChartSetting(BarChart barChart, List<String> labels) {
        XAxis xAxis = barChart.getXAxis();
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        double max = Collections.max(histogramInfo.collectFrequency());

        // BarChart Setting
        barChart.setDragEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.setExtraBottomOffset(60);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        // x-axis setting
        xAxis.setYOffset(25);
        if (experimentType.equals(Extras.COUNT_TYPE) || experimentType.equals(Extras.BINOMIAL_TYPE)) {
            xAxis.setTextSize(25f);
        } else {
            xAxis.setTextSize(11f);
        }
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Right side y-axis setting
        rightAxis.setEnabled(false);

        // Left side y-axis setting
        leftAxis.setTextSize(15f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setGridLineWidth(1);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount((int) max);
    }

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