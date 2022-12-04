package com.example.projectand.pages.Auth.RegisterTabs;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectand.R;
import com.example.projectand.pages.Auth.RegistrationActivity;

public class RegistrationPage2 extends Fragment implements View.OnClickListener {
    EditText email, password, repassword;
    Button finish, goback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_p2, container, false );

        email =view.findViewById(R.id.email_form);
        password = view. findViewById(R.id.password_form);
        repassword = view.findViewById(R.id.repassword_form);

        goback = view.findViewById(R.id.go_back);
        goback.setOnClickListener(this);

        finish= view.findViewById(R.id.finish_btn);
        finish.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.finish_btn) {
            String emailForm = email.getText().toString();
            String passwordForm = password.getText().toString();
            String repasswordForm = repassword.getText().toString();
            ((RegistrationActivity) getActivity()).secondPage(emailForm, passwordForm, repasswordForm);

        } else if (view.getId()==R.id.go_back) {
            getActivity().onBackPressed();
        }
    }

    public void formError(int form_id, String message) {
        EditText formItem = getView().findViewById(form_id);
        if (formItem != null) {
            formItem.setError(message);
            formItem.requestFocus();
        }
    }

    // other events
}