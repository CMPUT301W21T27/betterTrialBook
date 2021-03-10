package com.example.bettertrialbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.forum.ForumActivity;

public class ExperimentViewActivity extends AppCompatActivity {
    String experimentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        Button unpublishButton = findViewById(R.id.unpublish_button);

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get id of currently selected experiment
                experimentId = getIntent().getStringExtra(Intents.EXTRA_EXPERIMENT_ID);

                if (unpublishButton.getText().equals("Unpublish")) {
                    unpublishButton.setText("Publish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.unpublishExperiment(experimentId);
                } else {
                    unpublishButton.setText("Unpublish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.publishExperiment(experimentId);
                }
            }
        });
    }

    private  void openForum() {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(Intents.EXTRA_EXPERIMENT_ID, experimentId);
    }
}