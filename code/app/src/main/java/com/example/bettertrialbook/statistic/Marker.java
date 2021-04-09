// Credit: Mar 31, 2021, PhilJay MPAndroidChart, Apache 2.0.
package com.example.bettertrialbook.statistic;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.Trial;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Marker extends MarkerView {
    private MPPointF mOffset;
    private SimpleDateFormat sdf;
    private String experimentType;
    private ArrayList<Double> data;
    private ArrayList<Trial> trials;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public Marker(Context context, int layoutResource, @NonNull ArrayList<Trial> trials, ArrayList<Double> data, String type) {
        super(context, layoutResource);
        this.data = data;
        this.trials = trials;
        this.experimentType = type;
        this.sdf = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int number = (int) e.getX();
        TextView label = findViewById(R.id.Label);
        TextView value = findViewById(R.id.DisplayValue);
        TextView display = findViewById(R.id.DisplayTime);

        if (!experimentType.equals(Extras.COUNT_TYPE)) {
            // Display the timeStamp for the point
            if (number > 0 && trials.get(number - 1) != null) {
                Date date = trials.get(number - 1).getTimestamp();
                display.setText(sdf.format(date));
                display.setTextSize(15f);
            } else {
                label.setText("");
                display.setText("");
            }

            // Display the value (result) for the point
            if (number > 0 && data.get(number - 1) != null) {
                value.setText(String.valueOf(data.get(number - 1)));
                value.setTextSize(15f);
            } else {
                label.setText("");
                display.setText("");
            }
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
