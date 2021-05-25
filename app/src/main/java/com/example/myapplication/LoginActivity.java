package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.login_emailEditText);
        passwordInput = findViewById(R.id.login_passwordEditText);
    }

    public void onLoginClick(View view) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                "You must fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    LoginActivity.this.finish();
                } else {
                    String errorMsg = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this,
                            "Login failed: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            });
    }

    public void onRegistrationClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        this.finish();
    }
}
