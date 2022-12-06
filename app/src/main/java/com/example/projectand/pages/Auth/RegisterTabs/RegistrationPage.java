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

public class RegistrationPage extends Fragment implements View.OnClickListener {
    EditText name,familyname;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_p1, container, false );

        name = view.findViewById(R.id.name_form);
        familyname= view.findViewById(R.id.familyname_form);

        next = view.findViewById(R.id.next_tab_btn);
        next.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.next_tab_btn){
            String nameForm = name.getText().toString();
            String familynameForm = familyname.getText().toString();

            ((RegistrationActivity) getActivity()).firstPage(nameForm, familynameForm);
        }
    }


    public void formError(int form_id, String message) {
        EditText formItem = getView().findViewById(form_id);
        if (formItem != null) {
            formItem.setError(message);
            formItem.requestFocus();
        }
    }

    public Button getNextBtn() {
        return next;
    }
    // other events
}