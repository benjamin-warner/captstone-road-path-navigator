package com.ksucapstone.gasandgo.ArrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ksucapstone.gasandgo.Helpers.StringHelpers;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.R;

import java.util.List;

public class DirectionsAdapter extends ArrayAdapter<Step> {

    public DirectionsAdapter(Context context, int resource, List<Step> steps) {
        super(context, resource, steps);
    }

    @Override
    @NonNull
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        final Step step = getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.leg_info, null);
        }

        TextView directionText = view.findViewById(R.id.direction_text);
        directionText.setText(StringHelpers.RemoveHtmlTags(step.html_instructions));

        TextView directionDistance = view.findViewById(R.id.direction_distance);
        directionDistance.setText(step.distance.text);

        TextView directionTime = view.findViewById(R.id.direction_time);
        directionTime.setText(step.duration.text);

        return view;
    }

}