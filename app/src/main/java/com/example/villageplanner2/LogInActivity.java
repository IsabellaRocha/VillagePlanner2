package com.example.villageplanner2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public void LogIn(View view) {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        String emailString  = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailString)) {
            Toast.makeText(getApplicationContext(), "Email cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(getApplicationContext(), "Password cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            Intent mapIntent = new Intent(LogInActivity.this, MapActivity.class);
                            startActivity(mapIntent);
                        }
                        else {
                            Toast.makeText(LogInActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
