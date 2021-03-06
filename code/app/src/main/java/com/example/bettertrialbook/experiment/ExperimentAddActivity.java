package com.example.bettertrialbook.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.ExperimentInfo;

/**
 * Activity for adding new experiment. Uses the ExperimentDAL to write new experiment to database.
 */
public class ExperimentAddActivity extends AppCompatActivity {
    String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_add);

        // Spinner based on:
        // Android Developers, 2020-11-18, Apache 2.0, https://developer.android.com/guide/topics/ui/controls/spinner
        Spinner typeSpinner = findViewById(R.id.type_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.experimentTypes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

        ownerId = getIntent().getStringExtra("OwnerId");

        EditText descriptionEdit = findViewById(R.id.description_editText);
        EditText minTrialsEdit = findViewById(R.id.mintrials_editText);
        EditText regionEdit = findViewById(R.id.region_editText);

        Button geoLocationButton = findViewById(R.id.geolocation_button);

        geoLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (geoLocationButton.getText().equals("No")) {
                    geoLocationButton.setText("Yes");
                } else {
                    geoLocationButton.setText("No");
                }
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button publishButton = findViewById(R.id.publish_button);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the input
                if (descriptionEdit.getText().length() > 0 && minTrialsEdit.getText().length() > 0 && typeSpinner.getSelectedItem().toString().length() > 0) {
                    boolean geoLocation;
                    geoLocation = geoLocationButton.getText().equals("Yes");
                    ExperimentDAL experimentDAL = new ExperimentDAL();
                    ExperimentInfo experimentInfo = new ExperimentInfo(
                            descriptionEdit.getText().toString(),
                            ownerId,
                            "Publish",
                            "Active",
                            "",
                            typeSpinner.getSelectedItem().toString(),
                            geoLocation,
                            Integer.parseInt(minTrialsEdit.getText().toString()),
                            regionEdit.getText().toString()
                    );
                    experimentDAL.addExperiment(experimentInfo, s -> openExperimentViewActivity(s));

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    /**
     * Return to the experiment view activity
     */
    public void openExperimentViewActivity(ExperimentInfo experimentInfo) {
        UserDAL userDAL = new UserDAL();
        userDAL.subscribeExperiment(experimentInfo.getId(), experimentInfo.getOwnerId());

        Boolean isOwner = true;

        Intent myIntent = new Intent(ExperimentAddActivity.this, ExperimentViewActivity.class);
        myIntent.putExtra("IsOwner", isOwner);
        myIntent.putExtra("NewExperiment", true);
        myIntent.putExtra("ExperimentInfo", experimentInfo);
        startActivity(myIntent);
    }
}
