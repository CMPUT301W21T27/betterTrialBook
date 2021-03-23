package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class StatisticHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_home_page);

        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
    }
}