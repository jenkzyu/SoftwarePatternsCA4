package com.example.onlineclothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlineclothingstore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnLogin.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, LoginActivity.class)));

        binding.btnSignUp.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, RegisterActivity.class)));
    }
}