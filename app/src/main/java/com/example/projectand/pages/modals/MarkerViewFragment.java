package com.example.projectand.pages.modals;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.MapMarker;
import com.example.projectand.models.User;
import com.example.projectand.utils.AddressGetter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MarkerViewFragment extends DialogFragment {
    Context context;
    MapMarker mapMarker;
    List<IconSpinnerItem> categoryList;
    EditText ownerForm;
    Integer selectedCategoryId;

    public MarkerViewFragment() {

    }

    public MarkerViewFragment(Context context, MapMarker mapMarker, List<IconSpinnerItem> categoryList, Integer selectedCategoryId) {
        this.context = context;
        this.mapMarker = mapMarker;
        this.categoryList = categoryList;
        this.selectedCategoryId = selectedCategoryId;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.modal_item_view, null);

        ownerForm = (EditText) v.findViewById(R.id.make_marker_creator);
        new FirebaseUserHandler().getUser(mapMarker.getCreatorId()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot result =  task.getResult();
                ownerForm.setText(result.child("name").getValue() + " " + result.child("family_name").getValue());
            } else {
                // error
                ownerForm.setError("Internet request failed... We couldn't get the owner's name. ");
            }
        });

        EditText categForm = (EditText) v.findViewById(R.id.make_marker_categ_2);
        categForm.setText(categoryList.get(selectedCategoryId).getText());

        EditText timeLeftForm = (EditText) v.findViewById(R.id.make_marker_timeleft);
        Duration duration = Duration.between(Instant.now(), mapMarker.getTimeCreation().plus(5, ChronoUnit.HOURS));
        timeLeftForm.setText(duration.toMinutes()+" minutes remaining...");

        EditText remarkForm = (EditText) v.findViewById(R.id.make_marker_remark);
        remarkForm.setText(mapMarker.getRemark());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v);

        v.findViewById(R.id.btn_exit).setOnClickListener(view -> this.dismiss());

        return builder.create();
    }
}
