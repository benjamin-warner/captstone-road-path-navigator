package com.ksucapstone.gasandgo.ArrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ksucapstone.gasandgo.Models.Directions.Leg;
import com.ksucapstone.gasandgo.R;

import java.util.List;

public class DirectionsAdapter extends ArrayAdapter<Leg> {

    public DirectionsAdapter(Context context, int resource, List<Leg> legs) {
        super(context, resource, legs);
    }

    @Override
    @NonNull
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        final Leg leg = getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.leg_info, null);
        }



        return view;
    }

}