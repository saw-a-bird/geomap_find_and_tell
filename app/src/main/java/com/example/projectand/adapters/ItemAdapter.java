package com.example.projectand.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.models.Category;
import com.example.projectand.models.MapMarker;
import com.example.projectand.pages.modals.CreateMarkerFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Category> {
    Context context;
    List<Category> data;
    List<Category> bData;
    String filteringBy = "";

    // Use this instance of the interface to deliver action events
    OnDelete listener;


    public ItemAdapter(@NonNull Context context, List<Category> data) {
        super(context, R.layout.category_item_list, data);
        listener = (OnDelete) context;

        this.context = context;
        this.data = data;
        Log.e("item i", String.valueOf(data.size()));

        this.bData = new ArrayList<Category>();
        this.bData.addAll(data);
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OnDelete {
        public void OnDelete(ItemAdapter itemAdapter, Integer index, Category category);
    }


    @Override
    public View getView(int position, View view, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.category_item_list, container, false);

        TextView nomView = view.findViewById(R.id.category_name);
        nomView.setText(data.get(position).getName());

        view.findViewById(R.id.delete_btn).setOnClickListener(btn -> {
            bData.remove(position);
            notifyDataSetChanged();
            listener.OnDelete(this, position, data.get(position));
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        Log.e("item f", String.valueOf(data.size()));
        filteringBy = charText.toLowerCase(Locale.getDefault());
        data.clear();

        if (filteringBy.length() == 0) {
            this.data.addAll(bData);

        } else {
            for (Category category : bData) {
                if (category.getName().toLowerCase(Locale.getDefault()).contains(filteringBy)) {
                   this.data.add(category);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void addCategory(Category category) {
        Log.e("item add", String.valueOf(data.size()));
        bData.add(category);
        filter(filteringBy);
    }

    public void removeCategory(String name) {
        bData.removeIf(category -> category.getName().equals(name));
        filter(filteringBy);
    }
}