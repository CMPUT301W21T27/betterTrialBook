package com.example.bettertrialbook;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.view.MenuInflater;

public class Histogram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_histogram);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        double mean = intent.getDoubleExtra("Mean",0.00);
        double median = intent.getDoubleExtra("Median", 0.00);
        double stdDev = intent.getDoubleExtra("StdDev", 0.00);
        double[] quartiles = intent.getDoubleArrayExtra("Quartile");


        TextView meanResult = findViewById(R.id.Mean_Result);
        TextView medianResult = findViewById(R.id.Median_Result);
        TextView stdDevResult = findViewById(R.id.StdDev_Result);
        TextView quartile1Result = findViewById(R.id.FirstQuartile_Result);
        TextView quartile3Result = findViewById(R.id.ThirdQuartile_Result);

        meanResult.setText(String.valueOf(mean));
        medianResult.setText(String.valueOf(median));
        stdDevResult.setText(String.valueOf(stdDev));
        quartile1Result.setText(String.valueOf(quartiles[0]));
        quartile3Result.setText(String.valueOf(quartiles[1]));
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