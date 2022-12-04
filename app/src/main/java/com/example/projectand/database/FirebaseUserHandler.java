package com.example.projectand.database;

import com.example.projectand.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserHandler {
    private FirebaseAuth mAuth;

    public FirebaseUserHandler() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    /* LOGIN METHODS */
    public Task<AuthResult> login(String email, String password) {
//        if (mAuth.signInWithEmailAndPassword(email, password).isSuccessful()) { // creates auth
//            return mAuth.getCurrentUser().getUid();
//        } else {
//            return null;
//        }
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Boolean check() {
        return mAuth.getCurrentUser() != null;
    }

    public Task<DataSnapshot> getCurrentUser() {
        // adds user to table "Users"
        if (this.check()) {
            return FirebaseDatabase.getInstance().getReference("Users")
                    .child(mAuth.getCurrentUser().getUid())
                    .get();
        } else {
            return null;
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    /* REGISTER METHODS */
    public Task<AuthResult> createUserAuth(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void saveUser(User user) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);
    }

    public Task<SignInMethodQueryResult> existsEmail(String email) {
        return mAuth.fetchSignInMethodsForEmail(email);
    }

    public Task<Void> sendVerificationEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser.sendEmailVerification();
    }
}
