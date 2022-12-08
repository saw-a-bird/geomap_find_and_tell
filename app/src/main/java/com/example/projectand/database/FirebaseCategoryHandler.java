package com.example.projectand.database;

import com.example.projectand.models.Category;
import com.example.projectand.models.MapMarker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.powerspinner.IconSpinnerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCategoryHandler {
    private DatabaseReference dataBase;
    private static List<Category> allCategories;
    private static final String TABLE_CATEGORY = "categories";

    public FirebaseCategoryHandler() {
        this.dataBase = FirebaseDatabase.getInstance().getReference();
    }

    /* CRUD */
    public void createNew(String name) {
        String key = dataBase.child(TABLE_CATEGORY).push().getKey();
        Category category = new Category(key, name);
        Map<String, Object> postValues = category.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/"+ TABLE_CATEGORY + "/" + key, postValues);

        dataBase.updateChildren(childUpdates);
    }

    public void deleteCategory(Category category) {
        dataBase.child(TABLE_CATEGORY)
                .child(category.getId())
                .removeValue();
    }

    public DatabaseReference getAllRef() {
        return dataBase.child(TABLE_CATEGORY);
    }

    public Task<DataSnapshot> getAll() {
        return dataBase.child(TABLE_CATEGORY).get();
    }

    public List<IconSpinnerItem> getAllTemporarily() {
        List<IconSpinnerItem> list = new ArrayList<>();
        list.add(new IconSpinnerItem("Sugar"));
        list.add(new IconSpinnerItem("Fuel"));
        list.add(new IconSpinnerItem("Veggies"));
        return list;
    }
}
