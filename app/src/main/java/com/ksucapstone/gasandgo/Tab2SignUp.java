package com.ksucapstone.gasandgo;

/**
 * Created by luke on 3/21/18.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class Tab2SignUp extends Fragment implements View.OnClickListener {

    TabbedLogin callback;
    private EditText emailText_SignUp;
    private EditText passwordText_SignUp;
    private EditText passwordConfirmText_SignUp;
    private EditText postalCodeText_SignUp;
    private Button button_SignUp;

    @Override
    public void onAttach(Activity activity) {
        callback = (TabbedLogin)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2signup, container, false);
        //for sign up
        emailText_SignUp = rootView.findViewById(R.id.emailText_SignUp);
        passwordText_SignUp = rootView.findViewById(R.id.passwordText_SignUp);
        passwordConfirmText_SignUp = rootView.findViewById(R.id.passwordConfirmText_SignUp);
        button_SignUp = rootView.findViewById(R.id.button_SignUp);
        postalCodeText_SignUp = rootView.findViewById(R.id.postalCodeText_SignUp);
        button_SignUp.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {
        if(view == button_SignUp) {
            String email = emailText_SignUp.getText().toString().trim();
            String password = passwordText_SignUp.getText().toString().trim();
            String passwordConfirm = passwordConfirmText_SignUp.getText().toString().trim();
            String postalCode = postalCodeText_SignUp.getText().toString().trim();
            callback.registerUser(email, password, passwordConfirm, postalCode);
        }
    }
}