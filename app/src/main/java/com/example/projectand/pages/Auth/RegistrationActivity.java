package com.example.projectand.pages.Auth;

import android.content.Intent;
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
import com.example.projectand.database.ResultCodes;
import com.example.projectand.models.User;
import com.example.projectand.pages.Auth.RegisterTabs.RegistrationPage;
import com.example.projectand.pages.Auth.RegisterTabs.RegistrationPage2;
import com.example.projectand.pages.Main.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    String name = null;
    String familyname = null;
    String cityandcountry = null;
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

    public void firstPage(String nameF, String familynameF, String cityandcountryF) {
        if (!(nameF.isEmpty() && familynameF.isEmpty() && cityandcountryF.isEmpty())) {
            if (cityandcountryF.split(",").length == 2) {
                name = nameF;
                familyname = familynameF;
                cityandcountry = cityandcountryF;

                goPage(page2);

            } else {
                formError(page1, R.id.countryandcity_form, "Cette format est invalid!");
            }
        } else {
            Toast.makeText(this, "Please fill in all fields in the form!", Toast.LENGTH_SHORT).show();
        }
    }

    public void secondPage(String emailF, String passwordF, String repasswordF) {
        if (!emailF.isEmpty() && !passwordF.isEmpty() && !repasswordF.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailF).matches()) {
                if (passwordF.length() >= 6) {
                    if (repasswordF.matches(passwordF)) {

                        email = emailF;
                        password = passwordF;
                        User user = new User(name, familyname, cityandcountry, email, password);
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
    }

    public void formError(Fragment page, int form_id, String message) {
        if (page instanceof RegistrationPage) {
            ((RegistrationPage) page).formError(form_id, message);
        } else {
            ((RegistrationPage2) page).formError(form_id, message);
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void test() {
        goPage(page2);
        User user = new User("test", "test", "aa", "gamezrookie@gmail.com", "qsdqsdqsd23");
        createUser(user);
    }

    public void createUser(User user) {
        firebaseUserHandler.existsEmail(user.getEmail()).addOnSuccessListener(task -> {
            if (task.getSignInMethods().isEmpty()) {
                firebaseUserHandler.createUserAuth(user.getEmail(), user.getPassword()).addOnSuccessListener(task2 -> {
                    user.setUId(task2.getUser().getUid());
                    firebaseUserHandler.saveUser(user);
                    firebaseUserHandler.sendVerificationEmail().addOnCompleteListener(task3 -> {
                        if (task3.isSuccessful()) {
                            firebaseUserHandler.signOut();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                           Toast.makeText(this, "Emailer service ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            } else {
                formError(page2, R.id.email_form, "This email already exists!");
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