package com.example.projectand.database;

import com.example.projectand.models.MapMarker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.powerspinner.IconSpinnerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCategoryHandler {
    private DatabaseReference dataBase;
    private static final String TABLE_CATEGORY = "category";

    public FirebaseCategoryHandler() {
        this.dataBase = FirebaseDatabase.getInstance().getReference();
    }

    /* CRUD */
//    public void createNew(MapMarker mapMarker) {
//        String key = dataBase.child(TABLE_CATEGORY).push().getKey();
//        mapMarker.setId(key);
//
//        Map<String, Object> postValues = mapMarker.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//
//        childUpdates.put("/"+ TABLE_MARKERS +"/" + mapMarker.getCategoryId() + "/" + key, postValues);
//        childUpdates.put("/"+ TABLE_USER_MARKERS + "/" + mapMarker.getCreatorId() + "/" + key, postValues);
//
//        dataBase.updateChildren(childUpdates);
//    }

    public DatabaseReference getAll() {  // TODO: listeners
        return dataBase.child(TABLE_CATEGORY);
    }

    public List<IconSpinnerItem> getAllTemporarily() {
        List<IconSpinnerItem> list = new ArrayList<>();
        list.add(new IconSpinnerItem("Sugar"));
        list.add(new IconSpinnerItem("Fuel"));
        list.add(new IconSpinnerItem("Veggies"));
        return list;
    }
}
