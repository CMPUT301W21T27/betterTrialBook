/*
    Initial Screen
    Current Version: V1
 */

package com.example.bettertrialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<ExperimentInfo> trialInfoList;
    private ArrayAdapter<ExperimentInfo> trialInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.ScanQR_Button);               // Button to go to the scan QR page
        Spinner spinner = findViewById(R.id.Option_Display);
        Button createQR = findViewById(R.id.CreateQR_Button);           // Button to go to the create QR page
        ListView resultList = findViewById(R.id.Result_ListView);
        SearchView searchItem = findViewById(R.id.SearchItem);

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new CustomList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);

        // Choose the option for the sorting method
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Connection to the FireStore FireBase
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("Experiments");
        
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Empty Body: May add-on new features
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reference.addSnapshotListener((queryDocumentSnapshots,error) -> {
                    trialInfoList.clear();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Only search for the key words in the description
                        // Further search method will be refined after I have figured out what kind of search we are gonna do
                        if (checkforKeyWords((String) doc.getData().get("Description"), newText)) {
                            // MinTrials hasn't done yet. Want to wait for further production and then decide.
                            // GeoLocationRequired hasn't done yet. Want to wait for further production and then decide.
                            String id = doc.getId();
                            String region = (String) doc.getData().get("Region");
                            String status = (String) doc.getData().get("Status");
                            String trialType = (String) doc.getData().get("TrialType");
                            String description = (String) doc.getData().get("Description");
                            trialInfoList.add(new ExperimentInfo(id, description,status, trialType,false,0,region));
                        }
                    }
                    trialInfoAdapter.notifyDataSetChanged();
                });
                return false;
            }
        });
    }

    // Check for the keywords in the description of the document in the FireBase FireStore
    public boolean checkforKeyWords(String word, String key) {
        return word.toLowerCase().contains(key.toLowerCase());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Empty Body
    }
}
