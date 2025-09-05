package com.example.projectand.pages.modals;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.models.MapMarker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class CreateMarkerFragment extends DialogFragment implements OnSpinnerItemSelectedListener {
    Context context;
    Address address;
    List<IconSpinnerItem> categoryList;
    Integer selectedCategoryId = 0;
    HashMap<Integer, String> categoryMap;

    public CreateMarkerFragment() {

    }

    public CreateMarkerFragment(Context context, Address address, Integer currentCategory, List<IconSpinnerItem> categoryList, HashMap<Integer, String> categoryMap) {
        this.context = context;
        this.address = address;
        this.categoryList = categoryList;
        this.selectedCategoryId = currentCategory;
        this.categoryMap = categoryMap;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OnMarkerAccepted {
        public void onMarkerAccepted(DialogFragment dialog, MapMarker mapMarker, Integer selectedCategoryId);
    }



    // Use this instance of the interface to deliver action events
    OnMarkerAccepted listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (OnMarkerAccepted) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement CreateMarkerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.modal_item, null);

        EditText locForm = (EditText) v.findViewById(R.id.make_marker_loc);
        locForm.setText(address.getCountryName() + (address.getLocality() != null? " - "+address.getLocality() : ""));

        PowerSpinnerView categForm = (PowerSpinnerView) v.findViewById(R.id.make_marker_categ);
        EditText remarkForm = (EditText) v.findViewById(R.id.make_marker_remark);

        Toast.makeText(
                getContext(),
                String.valueOf(categoryList.toArray().length),
                Toast.LENGTH_SHORT
        ).show();

        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(categForm);
        categForm.setSpinnerAdapter(iconSpinnerAdapter);
        categForm.setItems(categoryList);
        categForm.selectItemByIndex(selectedCategoryId);
        categForm.setLifecycleOwner(this);
        categForm.setOnSpinnerItemSelectedListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v);

        Button submitBtn = (Button) v.findViewById(R.id.btn_submit);
        Button cancelBtn = (Button) v.findViewById(R.id.btn_cancel);

        submitBtn.setOnClickListener(view -> {
            MapMarker mapMarker = new MapMarker(FirebaseAuth.getInstance().getUid(),
                    Instant.now(),
                    new LatLng(address.getLatitude(), address.getLongitude()),
                    address.getCountryName(),
                    categoryMap.get(selectedCategoryId),
                    remarkForm.getText().toString());

            listener.onMarkerAccepted(this, mapMarker, selectedCategoryId);
            this.dismiss();
        });

        cancelBtn.setOnClickListener(view -> this.dismiss());

        return builder.create();
    }

    @Override
    public void onItemSelected(int oldItem, @Nullable Object o, int newItem, Object t1) {
        selectedCategoryId = newItem;
    }
}
