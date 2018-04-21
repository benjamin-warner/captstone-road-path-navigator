package com.ksucapstone.gasandgo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.ksucapstone.gasandgo.Helpers.DataSnapshotHelper;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Repositories.DatabaseWrapper;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private String destination = "";
    private String origin = "";

    private ArrayList<CarModel> userCars = new ArrayList<>();
    private Spinner carSpinner;
    private ArrayAdapter<CarModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();



        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Tab1Login.class)); //change may cause error, if so change back to 'TabbedLogin'
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail());
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
        findViewById(R.id.buttonAddCar).setOnClickListener(this);
        findViewById(R.id.buttonDoItForMe).setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place.getName().toString();
            }
            @Override
            public void onError(Status status) {}
        });

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                origin = place.getName().toString();
            }
            @Override
            public void onError(Status status) {}
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() + "/AvailableCars";
        DatabaseWrapper db = new DatabaseWrapper(new DatabaseWrapper.DataSnapshotReceiver() {
            @Override
            public void onSnapshotReceived(DataSnapshot snapshot) {
                DataSnapshotHelper<Integer> helper = new DataSnapshotHelper<>();
                ArrayList<Integer> userCarIds = helper.getObjectListFromSnapshot(snapshot, Integer.class);
                getUserCarList(userCarIds);
            }
        });
        db.queryOnceForSingleObject(userId);

        carSpinner = findViewById(R.id.userCars);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, userCars);
        carSpinner.setAdapter(adapter);
    }

    private void getUserCarList(ArrayList<Integer> carIds){
        for(int id : carIds){
            DatabaseWrapper db = new DatabaseWrapper(new DatabaseWrapper.DataSnapshotReceiver() {
                @Override
                public void onSnapshotReceived(DataSnapshot snapshot) {
                    DataSnapshotHelper<CarModel> helper = new DataSnapshotHelper<>();
                    CarModel car = helper.getSnapshotValue(snapshot, CarModel.class);
                    userCars.add(car);
                    adapter.notifyDataSetChanged();
                }
            });
            db.queryOnceForSingleObject("Cars/" + String.valueOf(id));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonLogout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, TabbedLogin.class));
                finish();
                break;
            case R.id.buttonAddCar:
                AddCarDialogFragment addCarPopup = new AddCarDialogFragment();
                addCarPopup.show(getFragmentManager(), AddCarDialogFragment.TAG);
                break;
            case R.id.buttonDoItForMe:
                hideKeyboard();
                if(origin.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(this, "Pick both a source and destination!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent doItForMe = new Intent(this, SlowMapsActivity.class);
                    doItForMe.putExtra("origin", origin);
                    doItForMe.putExtra("destination", destination);
                    CarModel car = (CarModel) carSpinner.getSelectedItem();
                    doItForMe.putExtra("car", car);
                    startActivity(doItForMe);
                }
                break;
        }
    }

    public void addAnotherCar(int car){
        ArrayList<Integer> carIdContainer = new ArrayList<>();
        carIdContainer.add(car);
        getUserCarList(carIdContainer);
    }

    private void hideKeyboard() {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
