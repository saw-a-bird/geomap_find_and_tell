package com.example.projectand.pages.modals;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.pages.Main.MapsActivity;
import com.example.projectand.utils.AddressGetter;
import com.example.projectand.utils.InternetConnection;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CountryDialogFragment extends DialogFragment implements AddressGetter.OnAddressGetterFinished {
    Geocoder geoCoder;
    String current_address;

    Button nextBtn, cancelBtn;
    EditText addressForm;
    Context context;

    public CountryDialogFragment() {

    }

    public CountryDialogFragment(Context context, Geocoder geoCoder) {
        this.context = context;
        this.geoCoder = geoCoder;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DialogListener {
        public void onDialogValidated(DialogFragment dialog, String locationAddress, Address address);
    }



    // Use this instance of the interface to deliver action events
    DialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement CountryDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.modal_country, null);
        addressForm = (EditText) v.findViewById(R.id.address_edit);
        nextBtn = (Button) v.findViewById(R.id.btn_next);
        cancelBtn = (Button) v.findViewById(R.id.btn_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v);

        nextBtn.setOnClickListener(view -> {
            String full_address = addressForm.getText().toString();
            if (!full_address.isEmpty()) {
                disableBtns();
                new AddressGetter(1, geoCoder, this).execute(full_address);
            } else {
                addressForm.setError("Cannot be empty!");
            }
        });

        cancelBtn.setOnClickListener(view -> {
            this.dismiss();
        });

        return builder.create();
    }

    @Override
    public void onAddressGetterFinished(Integer code, String result, Address address) {
        if (address != null) {
            this.listener.onDialogValidated(this, result, address);
            this.dismiss();
        } else {
            addressForm.setError(result);
            enableBtns();
        }
    }

    void enableBtns() {
        nextBtn.setEnabled(true);
        cancelBtn.setEnabled(true);

        nextBtn.setTextColor(ContextCompat.getColor(this.context,
                R.color.submit_btn));
        cancelBtn.setTextColor(ContextCompat.getColor(this.context,
                R.color.cancel_btn));
    }

    void disableBtns() {
        nextBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        nextBtn.setTextColor(ContextCompat.getColor(this.context,
                R.color.submit_btn_disabled));
        cancelBtn.setTextColor(ContextCompat.getColor(this.context,
                R.color.submit_btn_disabled));
    }
}