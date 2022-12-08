package com.example.projectand.database;

import com.example.projectand.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserHandler {
    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;
    private static final String TABLE_USERS = "users";

    public FirebaseUserHandler() {
        this.mAuth = FirebaseAuth.getInstance();
        this.dataBase = FirebaseDatabase.getInstance().getReference();
    }

    /* LOGIN METHODS */
    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Boolean isAuthenticated() {
        return mAuth.getCurrentUser() != null;
    }

    public void signOut() {
        mAuth.signOut();
    }

    /* REGISTER METHODS */
    public Task<AuthResult> createUserAuth(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<SignInMethodQueryResult> existsEmail(String email) {
        return mAuth.fetchSignInMethodsForEmail(email);
    }

    public Task<Void> sendVerificationEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.sendEmailVerification();
    }


    /* CRUD */
    public void createUser(User user) {
        // TODO test: dataBase.child(TABLE_USERS).child(user.getId()).setValue(user)

        Map<String, Object> postValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/"+ TABLE_USERS +"/" + user.getId(), postValues);

        dataBase.updateChildren(childUpdates);
    }

    public void saveLocation(LatLng location) {
         dataBase.child(TABLE_USERS)
            .child(mAuth.getCurrentUser().getUid())
            .child("last_location").setValue(location);
    }

    public void saveCategory(Integer category) {
        dataBase.child(TABLE_USERS)
                .child(mAuth.getCurrentUser().getUid())
                .child("favouriteCategory").setValue(category);
    }

    public Task<DataSnapshot> getCurrentUser() {
        return dataBase.child(TABLE_USERS)
            .child(mAuth.getCurrentUser().getUid())
            .get();
    }

    public Task<DataSnapshot> getUser(String uid) {
        return dataBase.child(TABLE_USERS)
                .child(mAuth.getCurrentUser().getUid())
                .get();
    }
}
