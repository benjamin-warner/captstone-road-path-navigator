package com.ksucapstone.gasandgo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.ksucapstone.gasandgo.ArrayAdapters.GasStationAdapter;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import java.util.ArrayList;

public class StationPickerFragment extends DialogFragment implements View.OnClickListener {

        public static final String TAG = "STATIONFRAGMENT";

        private ListView mListView;
        private GasStationAdapter mAdapter;
        private ArrayList<GasStationModel> mGasStations;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_gas_picker, container, false);

            mListView = rootView.findViewById(R.id.list);
            rootView.findViewById(R.id.report_back).setOnClickListener(this);
            rootView.findViewById(R.id.report_save).setOnClickListener(this);

            ArrayList<GasStationModel> stations = (ArrayList<GasStationModel>)getArguments().getSerializable("GAS");

            mAdapter = new GasStationAdapter(getContext(), R.layout.gas_list_item, stations,this);
            mListView.setAdapter(mAdapter);

            return rootView;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.report_back:
                    dismiss();
                    break;
                case R.id.report_save:

                    break;
                default:
                    break;
            }
        }


    }
