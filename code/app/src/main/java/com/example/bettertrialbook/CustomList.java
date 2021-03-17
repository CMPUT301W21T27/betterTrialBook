/*
    The CustomList to display the search result is still in progress
    Current Version: V1
*/

package com.example.bettertrialbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.User;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<ExperimentInfo> {

    private Context context;
    private ArrayList<ExperimentInfo> trials;

    public CustomList(Context context, ArrayList<ExperimentInfo> trials) {
        super(context, 0, trials);
        this.trials = trials;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        ExperimentInfo experiment = trials.get(position);

        TextView description = view.findViewById(R.id.Description_Text);
        TextView ownerName = view.findViewById(R.id.username_Text);
        TextView trialType = view.findViewById(R.id.TrialType_Text);
        TextView status = view.findViewById(R.id.Status_Text);

        description.setText(experiment.getDescription());
        trialType.setText("Type: " + experiment.getTrialType());
        status.setText(experiment.getStatus());

        // get owner name
        if (experiment.getOwnerId() != null) {
            UserDAL userDAL = new UserDAL();
            userDAL.findUserByID(experiment.getOwnerId(), new UserDAL.FindUserByIDCallback() {
                @Override
                public void onCallback(User user) {
                    ownerName.setText("Owner: " + user.getUsername());
                }
            });
        }

        return view;
    }
}
