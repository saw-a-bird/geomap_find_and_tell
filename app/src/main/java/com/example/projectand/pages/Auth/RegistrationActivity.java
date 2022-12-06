package com.example.projectand.pages.Auth;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.User;
import com.example.projectand.pages.Auth.RegisterTabs.RegistrationPage;
import com.example.projectand.pages.Auth.RegisterTabs.RegistrationPage2;
import com.example.projectand.pages.Main.HomeActivity;
import com.example.projectand.utils.AddressGetter;
import com.example.projectand.utils.InternetConnection;

import java.io.IOException;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    String name = null;
    String familyname = null;
    String email = null;
    String password = null;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    Fragment page1, page2;

    private FirebaseUserHandler firebaseUserHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fragmentManager = getSupportFragmentManager();

        page1 = new RegistrationPage();
        page2 = new RegistrationPage2();

        goPage(page1);

        TextView goLogin = findViewById(R.id.login_text_btn);
        goLogin.setOnClickListener(this);

        firebaseUserHandler = new FirebaseUserHandler();
    }

    @Override
    public void onClick(View event) {
        if(event.getId() == R.id.login_text_btn) {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void firstPage(String nameF, String familynameF) {
        if (!(nameF.isEmpty() && familynameF.isEmpty())) {
            ((RegistrationPage) page1).getNextBtn().setEnabled(false);

            name = nameF;
            familyname = familynameF;

            goPage(page2);
        } else {
            Toast.makeText(this, "Please fill in all fields in the form!", Toast.LENGTH_SHORT).show();
        }
    }

    public void secondPage(String emailF, String passwordF, String repasswordF) {
        if (InternetConnection.checkConnection(this)) {
            if (!emailF.isEmpty() && !passwordF.isEmpty() && !repasswordF.isEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailF).matches()) {
                    if (passwordF.length() >= 6) {
                        if (repasswordF.matches(passwordF)) {

                            email = emailF;
                            password = passwordF;
                            User user = new User(name, familyname, email, password);
                            createUser(user);

                        } else {
                            formError(page2, R.id.password_form, "Make sure that the password matches!");
                        }
                    } else {
                        formError(page2, R.id.password_form, "The password length must be at least 6 characters!");
                    }
                } else {
                    formError(page2, R.id.email_form, "Make sure that the email is valid!");
                }
            } else {
                Toast.makeText(this, "Please fill in all fields in the form!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please connect to the internet first!", Toast.LENGTH_SHORT).show();
        }
    }

    public void formError(Fragment page, int form_id, String message) {
        if (page instanceof RegistrationPage) {
            ((RegistrationPage) page).formError(form_id, message);
        } else {
            ((RegistrationPage2) page).formError(form_id, message);
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void createUser(User user) {
        Toast.makeText(this, "Processing query...", Toast.LENGTH_SHORT).show();
        ((RegistrationPage2) page2).getSubmitBtn().setEnabled(false);
        firebaseUserHandler.existsEmail(user.getEmail()).addOnSuccessListener(task -> {
            if (task.getSignInMethods().isEmpty()) {
                firebaseUserHandler.createUserAuth(user.getEmail(), user.getPassword()).addOnSuccessListener(task2 -> {
                    user.setId(task2.getUser().getUid());
                    firebaseUserHandler.createUser(user);
                    firebaseUserHandler.sendVerificationEmail().addOnCompleteListener(task3 -> {
                        if (task3.isSuccessful()) {
                            firebaseUserHandler.signOut();
                            Toast.makeText(this, "Success. Verification email was send to your inbox.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                           Toast.makeText(this, "Sorry, we could not connect to the emailer service! Try again later.", Toast.LENGTH_SHORT).show();
                            ((RegistrationPage2) page2).getSubmitBtn().setEnabled(false);
                        }
                    });
                });
            } else {
                formError(page2, R.id.email_form, "This email already exists!");
                ((RegistrationPage2) page2).getSubmitBtn().setEnabled(true);
            }
        });
    }

    public void goPage(Fragment page) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, page, null)
            .commit();
    }

    @Override
    public void onBackPressed() {
        if (page2.isVisible()) {
            goPage(page1);
        } else {
            // go to home
            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}