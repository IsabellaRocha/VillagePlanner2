package com.example.villageplanner2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private EditText firstName, lastName, email, password, confirmPassword, profilePhoto;
    FirebaseDatabase users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance();
    }

    public void createUser(View view) {
        firstName = findViewById(R.id.firstName);
        String firstNameString = firstName.getText().toString().trim();
        lastName = findViewById(R.id.lastName);
        String lastNameString = lastName.getText().toString().trim();
        email = findViewById(R.id.email);
        String emailString = email.getText().toString().trim();
        password = findViewById(R.id.password);
        String passwordString = password.getText().toString().trim();
        confirmPassword = findViewById(R.id.confirmPassword);
        String confirmPasswordString = confirmPassword.getText().toString().trim();
        profilePhoto = findViewById(R.id.profilePhoto);
        String profilePhotoString = profilePhoto.getText().toString().trim();

        if (TextUtils.isEmpty(firstNameString)) {
            Toast.makeText(SignInActivity.this, "First name cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(lastNameString)) {
            Toast.makeText(SignInActivity.this, "Last name cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(emailString)) {
            Toast.makeText(SignInActivity.this, "Email cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(emailString)) {
            Toast.makeText(SignInActivity.this, "Email cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(SignInActivity.this, "Password cannot be left blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordString.length() < 6) {
            Toast.makeText(SignInActivity.this, "Please ensure your password is at least 6 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!passwordString.equals(confirmPasswordString)) {
            Toast.makeText(SignInActivity.this, "Please make sure you confirm your password correctly", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserActivity user = new UserActivity(firstNameString, lastNameString, emailString, profilePhotoString);
                    users.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "Signed up successfully! You may now log in.", Toast.LENGTH_LONG).show();
                                Intent loginIntent = new Intent(SignInActivity.this, LogInActivity.class);
                                startActivity(loginIntent);
                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Sign up was unsuccessful. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignInActivity.this, "Sign up was unsuccessful. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}