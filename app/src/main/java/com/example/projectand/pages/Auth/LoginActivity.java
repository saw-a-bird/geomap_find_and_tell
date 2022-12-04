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
import com.example.projectand.pages.Main.MapsActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView goRegister; // its form is a text

    EditText email, pass;
    String emailForm, passwordForm;

    private FirebaseUserHandler firebaseUserHandler;
//    private SharedPreferences localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.email_loginp);
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
        if(view.getId()==R.id.login_btn){
            emailForm = email.getText().toString();
            passwordForm = pass.getText().toString();

            if(!emailForm.isEmpty() && !passwordForm.isEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailForm).matches()) {
                    firebaseUserHandler.login(emailForm, passwordForm).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().getUser().isEmailVerified()) {
                                Toast.makeText(this, "Successfully connected!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "This email is not verified yet!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            email.setError("Invalid email or password.");
                            pass.setError("Invalid email or password.");
                            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    email.setError("Make sure the email is valid");
                    Toast.makeText(this, "Make sure the email is valid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all fields in the form!", Toast.LENGTH_SHORT).show();
            }
        }

        if(view.getId()==R.id.register_btn){
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }

    }
}