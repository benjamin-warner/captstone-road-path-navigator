package com.ksucapstone.gasandgo.ArrayAdapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ksucapstone.gasandgo.Models.GasStationModel;
import com.ksucapstone.gasandgo.R;

import java.util.ArrayList;

public class GasStationAdapter extends ArrayAdapter<GasStationModel> {

    View.OnClickListener listener;

    public GasStationAdapter(Context context, int resource, ArrayList<GasStationModel> objects, View.OnClickListener listener) {
        super(context, resource, objects);
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {
        final GasStationModel stationModel = getItem(position);

        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.gas_list_item, null);
        }

        TextView stationName = itemView.findViewById(R.id.checkbox_gas);
        stationName.setText(stationModel.address);
        TextView price = itemView.findViewById(R.id.gas_price);
        price.setText(String.valueOf(stationModel.price));

        itemView.setOnClickListener(listener);

        return itemView;
    }

}