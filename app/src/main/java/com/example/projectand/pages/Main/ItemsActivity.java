package com.example.projectand.pages.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.adapters.ItemAdapter;
import com.example.projectand.database.FirebaseCategoryHandler;
import com.example.projectand.models.Category;
import com.example.projectand.pages.modals.CategoryCreateFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ItemAdapter.OnDelete {

    DialogFragment dialog;
    ListView listView;
    ItemAdapter itemAdapter;
    List<Category> categoryList;
    DatabaseReference databaseReference;
    FirebaseCategoryHandler firebaseCategoryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list);
        listView = findViewById(R.id.listView);
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        findViewById(R.id.go_back).setOnClickListener(view -> {
            Intent i = new Intent(ItemsActivity.this, MapsActivity.class);
            startActivity(i);
            finish();
        });

        firebaseCategoryHandler = new FirebaseCategoryHandler();
        databaseReference = firebaseCategoryHandler.getAllRef();
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                categoryList = new ArrayList<>();

                task.getResult().getChildren().forEach(item -> {
                    categoryList.add(new Category(item));
                });

                itemAdapter = new ItemAdapter(this, categoryList);
                listView.setAdapter(itemAdapter);

                findViewById(R.id.create_btn).setOnClickListener(view -> {
                    dialog = new CategoryCreateFragment(this, firebaseCategoryHandler, categoryList);
                    dialog.show(getSupportFragmentManager(), "country");
                });

                loadListener();
            } else {
                Toast.makeText(this, "Couldn't load categories. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadListener() {
        ChildEventListener categoryListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category = new Category(snapshot);

                boolean found = false;
                for (Category item : categoryList) {
                    if (Objects.equals(item.getId(), category.getId())) {
                        found = true;
                        break;
                    }
                };

                if (!found)
                    itemAdapter.addCategory(category);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                 itemAdapter.removeCategory((String) snapshot.child("name").getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        databaseReference.addChildEventListener(categoryListener);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null) newText = "";  // <- add this line
        itemAdapter.filter(newText);
        Log.e("search", newText);
        return false;
    }

    @Override
    public void OnDelete(ItemAdapter itemAdapter, Integer index, Category category) {
        firebaseCategoryHandler.deleteCategory(category);
    }
}