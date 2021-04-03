package com.example.bettertrialbook.statistic;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.view.MenuInflater;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class Histogram extends AppCompatActivity {
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

        ArrayList<Trial> trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");
        ArrayList<Double> dataList = statistic.experimentData(trialDataList);

        BarChart barChart = findViewById(R.id.Graph);
        TextView meanResult = findViewById(R.id.Mean_Result);
        TextView medianResult = findViewById(R.id.Median_Result);
        TextView stdDevResult = findViewById(R.id.StdDev_Result);
        TextView quartile1Result = findViewById(R.id.FirstQuartile_Result);
        TextView quartile3Result = findViewById(R.id.ThirdQuartile_Result);

        // Calculate the information
        double mean = statistic.Mean(trialDataList);
        double median = statistic.Median(trialDataList);
        double stdDev = statistic.StdDev(trialDataList, mean);
        double[] quartiles = statistic.Quartiles(trialDataList);

        // Display the Statistic Information to the User
        meanResult.setText(String.valueOf(mean));
        medianResult.setText(String.valueOf(median));
        stdDevResult.setText(String.valueOf(stdDev));
        quartile1Result.setText(String.valueOf(quartiles[0]));
        quartile3Result.setText(String.valueOf(quartiles[1]));

        // Plot the Histogram
        ArrayList<Integer> binFrequency = histogramInfo.collectFrequency(dataList);
        ArrayList<String> labels = histogramInfo.getLabels(dataList);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextSize(15f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setAxisMinValue(0);
        leftAxis.setAxisMaxValue(4);

        barChart.setDescription("");

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < binFrequency.size(); i++) {
            barEntries.add(new BarEntry(binFrequency.get(i), i));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Frequency");

        BarData theData = new BarData(labels, barDataSet);
        barChart.setData(theData);


        barChart.setScaleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistic_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ExperimentOverView) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}