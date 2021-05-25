package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends Activity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserService userService = UserService.getInstance();

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;

    private Toast registrationFailedToast;
    private Toast registrationSuccessfulToast;
    private Toast missingFieldsToast;

    public void onSubmitClick(View view) {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            register(name, email, password);
        } else {
            missingFieldsToast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameInput = findViewById(R.id.registration_nameEditText);
        emailInput = findViewById(R.id.registration_emailEditText);
        passwordInput = findViewById(R.id.registration_passwordEditText);

        registrationSuccessfulToast = Toast.makeText(this, "Registration was successful", Toast.LENGTH_SHORT);
        registrationFailedToast = Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT);
        missingFieldsToast = Toast.makeText(this, "You must fill all the fields", Toast.LENGTH_LONG);
    }

    private void register(String name, String email, String password) {
        Toast.makeText(this, "Registering user...", Toast.LENGTH_LONG).show();
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    createAppUser(name, email);
                } else {
                    registrationFailedToast.show();
                }
            });
    }

    private void createAppUser(String name, String email) {
        userService.createAppUser(name, email)
            .addOnSuccessListener(appUser -> {
                if (appUser != null) {
                    registrationSuccessfulToast.show();
                    RegistrationActivity.this.finish();
                } else {
                    registrationFailedToast.show();
                }
            });
    }
}
