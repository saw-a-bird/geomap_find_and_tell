package com.example.projectand.database;

import com.example.projectand.models.MapMarker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMapMarkerHandler {
    private DatabaseReference dataBase;
    private static final String TABLE_MARKERS = "markers";
    private static final String TABLE_USER_MARKERS = "user-markers";

    public FirebaseMapMarkerHandler() {
        this.dataBase = FirebaseDatabase.getInstance().getReference();
    }

    /* CRUD */
    public void createNew(MapMarker mapMarker) {
        String key = dataBase.child(TABLE_MARKERS).push().getKey();
        mapMarker.setId(key);

        Map<String, Object> postValues = mapMarker.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/"+ TABLE_MARKERS +"/" + mapMarker.getCountry() + "/" + mapMarker.getCategoryId() + "/" + key, postValues);
        childUpdates.put("/"+ TABLE_USER_MARKERS + "/" + mapMarker.getCreatorId() + "/" + key, postValues);

        dataBase.updateChildren(childUpdates);
    }

    public void deleteMarker(MapMarker mapMarker) {
         dataBase.child(TABLE_MARKERS)
                .child(mapMarker.getCountry())
                .child(String.valueOf(mapMarker.getCategoryId()))
                .child(mapMarker.getId()).removeValue();
    }

    public DatabaseReference getAll(String country, String categoryId) {  // TODO: listeners
        return dataBase.child(TABLE_MARKERS)
                .child(country)
                .child(String.valueOf(categoryId));
    }
}
