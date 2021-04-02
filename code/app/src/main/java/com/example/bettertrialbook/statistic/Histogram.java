package com.example.bettertrialbook.statistic;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.view.MenuInflater;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class Histogram extends AppCompatActivity {
    private ArrayList<Trial> trialDataList;
    private Statistic statistic = new Statistic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_histogram);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

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

        // TO-DO:   Plot the histogram
        showBarChart(barChart);
    }

    public void showBarChart(BarChart barChart) {
        String title = "Title";
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        //input data
        for(int i = 0; i < 6; i++){
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
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