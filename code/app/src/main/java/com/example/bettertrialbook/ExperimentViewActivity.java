package com.example.bettertrialbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.dal.ExperimentDAL;

public class ExperimentViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        Button unpublishButton = findViewById(R.id.unpublish_button);

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get id of currently selected experiment
                String id = "3DPU8vG4aHcAJd7iHR7M";


                if (unpublishButton.getText().equals("Unpublish")) {
                    unpublishButton.setText("Publish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.unpublishExperiment(id);
                } else {
                    unpublishButton.setText("Unpublish");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    experimentDAL.publishExperiment(id);
                }
            }
        });
    }
}