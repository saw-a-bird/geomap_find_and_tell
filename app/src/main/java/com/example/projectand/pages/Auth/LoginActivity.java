package com.example.projectand.pages.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.User;
import com.example.projectand.pages.Main.MapsActivity;
import com.example.projectand.utils.InternetConnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView goRegister; // its form is a text

    EditText email, pass;
    String emailForm, passwordForm;

    public static User currentUser;

    public static FirebaseUserHandler firebaseUserHandler;
//    private SharedPreferences localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.make_marker_loc);
        pass = findViewById(R.id.password_loginp);

        loginBtn =findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        goRegister = findViewById(R.id.register_btn);
        goRegister.setOnClickListener(this);

        firebaseUserHandler = new FirebaseUserHandler();
//        localStorage = getSharedPreferences("com.example.projectand", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_btn) {
            emailForm = email.getText().toString();
            passwordForm = pass.getText().toString();

            if (InternetConnection.checkConnection(this)) {
                if (!emailForm.isEmpty() && !passwordForm.isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailForm).matches()) {
                        email.setError("Make sure the email is valid");
                        //  Toast.makeText(this, "Make sure the email is valid", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseUserHandler.login(emailForm, passwordForm).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().getUser().isEmailVerified()) {
                                    currentUser = User.getInstance(this);
                                    if (currentUser == null) {
                                        Toast.makeText(this, "Authenticating...", Toast.LENGTH_SHORT).show();
                                        firebaseUserHandler.getCurrentUser().addOnSuccessListener(result -> {
                                            currentUser = new User(result);
                                            User.localizeInstance(this, new User(result));
                                            Toast.makeText(this, "Successfully connected!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        });
                                    }
                                } else {
                                    Toast.makeText(this, "This email is not verified yet!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                email.setError("Invalid email or password.");
                                pass.setError("Invalid email or password.");
                        //        Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Please fill in all fields in the form!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please connect to the internet first!", Toast.LENGTH_SHORT).show();
            }
        }

        if(view.getId()==R.id.register_btn){
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }

    }
}