package com.ksucapstone.gasandgo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.ksucapstone.gasandgo.Helpers.DataSnapshotHelper;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Repositories.DatabaseWrapper;
import com.ksucapstone.gasandgo.Repositories.MemoryCache;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AddCarDialogFragment extends DialogFragment implements View.OnClickListener, DatabaseWrapper.DataSnapshotReceiver {

    public static final String TAG = "ADD_CAR_FRAGMENT";
    private Spinner makeSpinner;
    private Spinner modelSpinner;

    private DatabaseWrapper databaseWrapper = new DatabaseWrapper(this);
    private ArrayList<CarModel> cars = new ArrayList<>();
    private ArrayList<String> makes;
    private ArrayList<String> models;
    private int currentIndex;
    private ProfileActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_car, container, false);

        makeSpinner = rootView.findViewById(R.id.spinner_make);
        modelSpinner = rootView.findViewById(R.id.spinner_model);

        rootView.findViewById(R.id.button_add_car).setOnClickListener(this);

        if(MemoryCache.GetInstance().get("Cars") == null) {
            Log.d("Car Cache", "Cars not in cache, building.");
            databaseWrapper.queryOnceForSingleObject("Cars/");
        }
        else {
            Log.d("Car Cache",  "Makes in cache.");
            cars = (ArrayList<CarModel>) MemoryCache.GetInstance().get("Cars");
            populateMakes();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        activity = (ProfileActivity)ctx;
    }
    private void populateMakes(){
        if(MemoryCache.GetInstance().get("Makes") != null){
            makes = (ArrayList<String>)MemoryCache.GetInstance().get("Makes");
            Log.d("Car Cache",  "Makes in cache.");
        }
        else{
            Log.d("Car Cache",  "Makes not in cache, building.");
            makes = new ArrayList<>();
            for(CarModel car : cars){
                if(!makes.contains(car.Make))
                    makes.add(car.Make);
            }
            MemoryCache.GetInstance().put("Makes", makes);
        }
        String[] makeArr = makes.toArray(new String[makes.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, makeArr);
        makeSpinner.setAdapter(adapter);
        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                populateModels(makes.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void populateModels(String make){
        if(MemoryCache.GetInstance().get(make) != null){
            models = (ArrayList<String>)MemoryCache.GetInstance().get(make);
            Log.d("Car Cache", make + " found in cache");
        }
        else{
            Log.d("Car Cache", make + " not in cache, building.");
            models = new ArrayList<>();
            for(CarModel car : cars){
                if(car.Make.equals(make) && !models.contains(car.Model)){
                    models.add(car.Model);
                }
                MemoryCache.GetInstance().put(make, models);
            }
        }
        String[] modelArr = models.toArray(new String[models.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, modelArr);
        modelSpinner.setAdapter(adapter);
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                queryForCar(models.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void queryForCar(String modelName) {
        for(int i = 0; i < cars.size(); i++){
            if(cars.get(i).Model.equals(modelName)){
                currentIndex = i;
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_add_car:
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child(user).child("AvailableCars").push().setValue(currentIndex);
                activity.addAnotherCar(currentIndex);
                dismiss();
                break;
        }
    }

    @Override
    public void onSnapshotReceived(DataSnapshot snapshot) {
        DataSnapshotHelper<CarModel> dataHelper = new DataSnapshotHelper<>();
        cars = dataHelper.getObjectListFromSnapshot(snapshot, CarModel.class);
        populateMakes();
    }
}
