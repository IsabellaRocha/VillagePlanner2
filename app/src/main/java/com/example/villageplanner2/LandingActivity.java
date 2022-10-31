package com.example.villageplanner2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LandingActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void LogIn(View view) {
        Intent loginIntent = new Intent(LandingActivity.this, LogInActivity.class);
        startActivity(loginIntent);
    }

    public void SignUp(View view) {
        Intent signUpIntent = new Intent(LandingActivity.this, SignInActivity.class);
        startActivity((signUpIntent));
    }
}
