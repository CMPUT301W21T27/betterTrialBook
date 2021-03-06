package com.example.bettertrialbook.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.User;

import java.util.ArrayList;

/**
    A CustomList to display the search results
*/
public class ExperimentList extends ArrayAdapter<ExperimentInfo> {
    private Context context;
    private ArrayList<ExperimentInfo> trials;

    public ExperimentList(Context context, ArrayList<ExperimentInfo> trials) {
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

        String pStatus = experiment.getPublishStatus();
        String aStatus = experiment.getActiveStatus();
        if (pStatus != null && pStatus.equals("Unpublish")) {
            status.setText(pStatus+"ed");

        } else if (aStatus != null) {
            status.setText(aStatus);
        }


        // get owner name
        if (experiment.getOwnerId() != null) {
            UserDAL userDAL = new UserDAL();
            userDAL.findUserByID(experiment.getOwnerId(), new UserDAL.FindUserByIDCallback() {
                @Override
                public void onCallback(User user) {
                    if (user != null) {
                        if (!user.getUsername().equals("")) {
                            ownerName.setText("Owner: " + user.getUsername());

                        } else {
                            ownerName.setText("Owner: " + user.getID().substring(0, 8));
                        }
                    } else {
                        ownerName.setText("Owner: " + experiment.getOwnerId().substring(0, 8));
                    }
                }
            });
        }

        return view;
    }
}
