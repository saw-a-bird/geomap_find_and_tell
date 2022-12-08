package com.example.projectand.pages.modals;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseCategoryHandler;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.Category;
import com.example.projectand.models.MapMarker;
import com.google.firebase.database.DataSnapshot;
import com.skydoves.powerspinner.IconSpinnerItem;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryCreateFragment extends DialogFragment {
    Context context;
    FirebaseCategoryHandler firebaseCategoryHandler;
    List<Category> categoryList;

    public CategoryCreateFragment() {

    }

    public CategoryCreateFragment(Context context, FirebaseCategoryHandler firebaseCategoryHandler, List<Category> categoryList) {
        this.context = context;
        this.firebaseCategoryHandler = firebaseCategoryHandler;
        this.categoryList = categoryList;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_item, null);

        EditText nameForm = (EditText) v.findViewById(R.id.item_name_form);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v);

        v.findViewById(R.id.btn_submit).setOnClickListener(view -> {
            String name = nameForm.getText().toString();
            if (!name.isEmpty()) {
                Boolean found = false;
                for (Category item : categoryList) {
                   if (Objects.equals(item.getName(), name)) {
                       nameForm.setError("This category already exists!");
                       found = true;
                       break;
                   }
                };

                if (!found) {
                    firebaseCategoryHandler.createNew(name);
                    this.dismiss();
                }
            }
        });

        v.findViewById(R.id.btn_cancel).setOnClickListener(view -> this.dismiss());
        return builder.create();
    }
}
