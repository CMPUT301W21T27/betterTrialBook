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

        ExperimentInfo trial = trials.get(position);

        TextView id = view.findViewById(R.id.ID_Text);
        TextView status = view.findViewById(R.id.Status_Text);
        TextView trialType = view.findViewById(R.id.TrialType_Text);
        TextView description = view.findViewById(R.id.Description_Text);

        id.setText(trial.getId());
        status.setText(trial.getStatus());
        trialType.setText(trial.getTrialType());
        description.setText(trial.getDescription());

        return view;
    }
}
