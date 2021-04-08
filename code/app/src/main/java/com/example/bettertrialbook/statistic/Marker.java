package com.example.bettertrialbook.statistic;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Date;

public class Marker extends MarkerView {

    private MPPointF mOffset;
    private TextView display;
    private TextView value;
    private ArrayList<Double> data;
    private ArrayList<Trial> trials;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public Marker(Context context, int layoutResource, @NonNull ArrayList<Trial> trials, ArrayList<Double> data) {
        super(context, layoutResource);
        this.data = data;
        this.trials = trials;
        value = findViewById(R.id.DisplayValue);
        display = findViewById(R.id.DisplayTime);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int number = (int) e.getX();
        // Display the timeStamp for the point
        if (number > 0 && trials.get(number - 1) != null) {
            display.setText(trials.get(number - 1).getTimestamp().toString());
        } else {
            display.setText("");
        }
        // Display the value (result) for the point
        if (number > 0 && data.get(number - 1) != null) {
            value.setText(String.valueOf(data.get(number - 1)));
            value.setTextSize(20f);
        } else {
            display.setText("");
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        // Basically set it close to the center of the circle
        if (mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
