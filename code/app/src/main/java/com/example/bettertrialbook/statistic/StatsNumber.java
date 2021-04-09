package com.example.bettertrialbook.statistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.Trial;

import java.util.ArrayList;

public class StatsNumber extends AppCompatActivity {
    String experimentType;
    private ArrayList<Trial> trialDataList;
    private final Statistic statistic = new Statistic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_number);
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        experimentType = intent.getStringExtra("TrialType");
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");

        TextView meanResult = findViewById(R.id.Mean_Result);
        TextView medianResult = findViewById(R.id.Median_Result);
        TextView stdDevResult = findViewById(R.id.StdDev_Result);
        TextView quartile1Result = findViewById(R.id.FirstQuartile_Result);
        TextView quartile3Result = findViewById(R.id.ThirdQuartile_Result);

        // Obtain the Statistic Information of the Experiment
        double mean = statistic.Mean(trialDataList);
        double median = statistic.Median(trialDataList);
        double stdDev = statistic.StdDev(trialDataList, mean);
        double[] quartiles = statistic.Quartiles(trialDataList);

        // Display Statistic Information to the User
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
        switch(item.getItemId()) {
            case R.id.ExperimentOverView:
                // Clicking the ExperimentOverView image will go back to ExperimentViewActivity
                finish();
                return true;
            case R.id.Histogram:
                // Clicking the Histogram image will go to Histogram Activity
                Intent intent2 = new Intent(this, Histogram.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Trials", trialDataList);
                intent2.putExtras(bundle2);
                intent2.putExtra("Type", experimentType);
                startActivity(intent2);
                return true;
            case R.id.PlotOverTime:
                // Clicking the PlotOverTime will go to LineGraph Activity
                Intent intent3 = new Intent(this, LineGraph.class);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("Trials", trialDataList);
                intent3.putExtras(bundle3);
                intent3.putExtra("Type", experimentType);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}