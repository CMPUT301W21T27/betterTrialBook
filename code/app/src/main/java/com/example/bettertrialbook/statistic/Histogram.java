package com.example.bettertrialbook.statistic;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;

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
import java.util.List;

public class Histogram extends AppCompatActivity {
    private ArrayList<Trial> trialDataList;
    private final Statistic statistic = new Statistic();
    private final HistogramInfo histogramInfo = new HistogramInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_histogram);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<String> labels;
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

        BarChart barChart = findViewById(R.id.Bar_Chart);
        // Histogram Graph Plot and Setting
        if (trialDataList.size() > 0 && trialDataList != null) {
            String type = trialDataList.get(0).getTrialType();
            if (type.equals(Extras.COUNT_TYPE)) {
                labels = histogramInfo.getLabelsCountTrial(trialDataList);
            }
            else {
                ArrayList<Double> dataList = statistic.experimentData(trialDataList);
                labels = histogramInfo.getLabels(dataList);
            }

            // Histogram Graph plot and setting
            barChartSetting(barChart, labels);
            plotTheGraph(barChart, trialDataList);
        }
    }

    // This method is used to plot the data of the histogram
    public void plotTheGraph(BarChart barChart, ArrayList<Trial> trials) {
        ArrayList<Integer> binFrequency;
        if (!trialDataList.get(0).getTrialType().equals(Extras.COUNT_TYPE)) {
            ArrayList<Double> dataList = statistic.experimentData(trialDataList);
            binFrequency = histogramInfo.collectFrequency(dataList);
        } else {
            binFrequency = histogramInfo.collectFrequencyCountTrial(trials);
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < binFrequency.size(); i++) {
            barEntries.add(new BarEntry(i, binFrequency.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Frequency");
        barDataSet.setValueTextSize(15f);
        barDataSet.setColor(Color.RED);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.invalidate();
    }

    // This method is used to change the setting of the histogram
    public void barChartSetting(BarChart barChart, List<String> labels) {
        XAxis xAxis = barChart.getXAxis();
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setExtraBottomOffset(60);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        xAxis.setYOffset(25);
        xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        rightAxis.setEnabled(false);

        leftAxis.setTextSize(15f);
        leftAxis.setGridLineWidth(1);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setAxisMinimum(0);
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