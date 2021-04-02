package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.bettertrialbook.models.Trial;

import java.util.ArrayList;

/* HomePage is an empty page for testing
 * Remarks Tests:
 *  1) Count Fish
 *  2) Length of the Worm
 *  3) Count Stars
 *  4) Coin Flip
 */
public class StatisticHomePage extends AppCompatActivity {

    private String trialType;
    private ArrayList<Trial> trialDataList;
    private Statistic statistic = new Statistic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_home_page);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();

        trialType = intent.getStringExtra("TrialType");
        trialDataList = (ArrayList<Trial>) bundle.getSerializable("Trials");
        Toolbar toolbar = findViewById(R.id.ToolBar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.Histogram:
                Intent intent = new Intent(this, Histogram.class);
                intent.putExtra("Mean", statistic.Mean(trialDataList));
                intent.putExtra("Median", statistic.Median(trialDataList));
                intent.putExtra("StdDev", statistic.StdDev(trialDataList, statistic.Mean(trialDataList)));
                intent.putExtra("Quartile", statistic.Quartiles(trialDataList));
                startActivity(intent);
                return true;
            case R.id.ExperimentOverView:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}