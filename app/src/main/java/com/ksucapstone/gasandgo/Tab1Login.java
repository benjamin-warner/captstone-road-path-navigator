package com.ksucapstone.gasandgo;

/**
 * Created by luke on 3/21/18.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Tab1Login extends Fragment implements View.OnClickListener {

    TabbedLogin callback;
    private Button button_Login;
    private EditText emailText_Login;
    private EditText passwordText_Login;

    @Override
    public void onAttach(Activity activity) {
        callback = (TabbedLogin)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1login, container, false);
        //for login
        emailText_Login = rootView.findViewById(R.id.emailText_Login);
        passwordText_Login = rootView.findViewById(R.id.passwordText_Login);
        button_Login = rootView.findViewById(R.id.button_Login);
        button_Login.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {
        if(view == button_Login) {
            String email = emailText_Login.getText().toString().trim();
            String password = passwordText_Login.getText().toString().trim();
            callback.userLogin(email, password);
        }
    }
}
