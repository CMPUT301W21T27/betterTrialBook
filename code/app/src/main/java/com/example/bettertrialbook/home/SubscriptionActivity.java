/*
 * Subscription List for the user that has subscribed
 * This subscriptionActivity cannot classify if the experiment is your own (auto subscription)
 * And the ones you subscribe to others
 */

package com.example.bettertrialbook.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.experiment.ExperimentViewActivity;
import com.example.bettertrialbook.models.ExperimentInfo;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SubscriptionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String userID;
    private Boolean aBoolean;
    UserDAL uDAL = new UserDAL();
    private ArrayList<ExperimentInfo> trialInfoList;
    private ArrayAdapter<ExperimentInfo> trialInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Intent intent = getIntent();
        userID = intent.getStringExtra("UserID");
        aBoolean = intent.getBooleanExtra("aBoolean", true);


        ListView resultList = findViewById(R.id.Result_ListView);

        trialInfoList = new ArrayList<>();
        trialInfoAdapter = new ExperimentList(this, trialInfoList);
        resultList.setAdapter(trialInfoAdapter);
        resultList.setOnItemClickListener(this);

        // Display all of the subscribed experiments
        if (userID != null) {
            uDAL.getSubscribed(userID, new UserDAL.GetSubscribedCallback() {
                @Override
                public void onCallback(List<String> subscribed) {
                    if (subscribed != null) {
                        for (int i = 0; i < subscribed.size(); i++) {
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            experimentDAL.findExperimentByID(subscribed.get(i), new ExperimentDAL.FindExperimentByIDCallback() {
                               @Override
                               public void onCallback(ExperimentInfo experimentInfo) {
                                   if (experimentInfo != null &&
                                           (experimentInfo.getPublishStatus().equals("Publish") ||
                                                   experimentInfo.getOwnerId().equals(You.getUser().getID()))) {
                                       trialInfoList.add(experimentInfo);
                                       Collections.sort(trialInfoList);
                                       trialInfoAdapter.notifyDataSetChanged();
                                   }
                               }
                            });
                        }
                    }
                }
            });
        }
    }

    // Goes to experiment's page if clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boolean isOwner = ((trialInfoAdapter.getItem(position).getOwnerId()).equals(You.getUser().getID()));
        String experimentId = trialInfoAdapter.getItem(position).getId();

        ExperimentDAL experimentDAL = new ExperimentDAL();
        experimentDAL.findExperimentByID(experimentId, new ExperimentDAL.FindExperimentByIDCallback() {
            @Override
            public void onCallback(ExperimentInfo experimentInfo) {
                Intent myIntent = new Intent(view.getContext(), ExperimentViewActivity.class);
                myIntent.putExtra("IsOwner", isOwner);
                myIntent.putExtra("NewExperiment", false);
                myIntent.putExtra("ExperimentInfo", experimentInfo);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    // MAY-DO: I was thinking to change the structure
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (aBoolean) {
            trialInfoList.clear();
            if (userID != null) {
                uDAL.getSubscribed(userID, new UserDAL.GetSubscribedCallback() {
                    @Override
                    public void onCallback(List<String> subscribed) {
                        Log.d("TEST2", String.valueOf(subscribed));
                        if (subscribed != null) {
                            if (subscribed.size() == 0) {
                                trialInfoAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < subscribed.size(); i++) {
                                    ExperimentDAL experimentDAL = new ExperimentDAL();
                                    experimentDAL.findExperimentByID(subscribed.get(i), new ExperimentDAL.FindExperimentByIDCallback() {
                                        @Override
                                        public void onCallback(ExperimentInfo experimentInfo) {
                                            if (experimentInfo != null &&
                                                    (experimentInfo.getPublishStatus().equals("Publish") ||
                                                            experimentInfo.getOwnerId().equals(You.getUser().getID()))) {
                                                trialInfoList.add(experimentInfo);
                                                trialInfoAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}