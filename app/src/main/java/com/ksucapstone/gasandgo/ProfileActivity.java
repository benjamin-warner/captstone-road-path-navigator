package com.ksucapstone.gasandgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;

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
        findViewById(R.id.buttonGetRoute).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.buttonLogout:
                firebaseAuth.signOut();
                startActivity(new Intent(this, TabbedLogin.class));
                finish();
                break;
            case R.id.buttonGetRoute:
                Intent intent = new Intent(this, MapsActivity.class);
                String origin = ((EditText)findViewById(R.id.origin)).getText().toString();
                intent.putExtra("origin", origin);
                String dest = ((EditText)findViewById(R.id.destination)).getText().toString();
                intent.putExtra("destination", dest);
                startActivity(intent);
                break;
        }
    }
}
