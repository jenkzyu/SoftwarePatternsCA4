package com.example.onlineclothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnSignUp,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, LoginActivity.class)));

        btnSignUp.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, RegisterActivity.class)));
    }
}