package com.example.bettertrialbook.statistic;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
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
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");
        ArrayList<Double> dataList = statistic.experimentData(trialDataList);
        List<String> labels = histogramInfo.getLabels(dataList);

        BarChart barChart = findViewById(R.id.Bar_Chart);

        barChartSetting(barChart, labels);
        plotTheGraph(barChart, dataList);
    }

    public void plotTheGraph(BarChart barChart, ArrayList<Double> dataList) {
        ArrayList<Integer> binFrequency = histogramInfo.collectFrequency(dataList);
        ArrayList<String> labels = histogramInfo.getLabels(dataList);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < binFrequency.size(); i++) {
            barEntries.add(new BarEntry(i, binFrequency.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Frequency");
        barDataSet.setValueTextSize(15f);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.setScaleEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setTouchEnabled(true);

        barChart.invalidate();
    }

    public void barChartSetting(BarChart barChart, List<String> labels) {
        XAxis xAxis = barChart.getXAxis();
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        float width = barChart.getWidth();
        float height = barChart.getHeight();


        barChart.setExtraBottomOffset(50);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        xAxis.setYOffset(20);
        xAxis.setTextSize(20f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        rightAxis.setEnabled(false);

        leftAxis.setTextSize(20f);
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