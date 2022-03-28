package com.example.onlineclothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.onlineclothingstore.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String email, username, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //back to main activity
        binding.goToLogin.setOnClickListener(view -> onBackPressed());
        //signup btn
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldReady())
                    resisterUser();
            }
        });

    }

    private boolean areFieldReady() {
        username = binding.signUpName.getText().toString().trim();
        email = binding.signUpEmail.getText().toString().trim();
        password = binding.signUpPassword.getText().toString().trim();
        password2 = binding.signUpPassword2.getText().toString().trim();

        boolean flag = false;
        View requestView = null;

        if (username.isEmpty()) {
            binding.signUpName.setError("Field is required");
            flag = true;
            requestView = binding.signUpName;
        } else if (email.isEmpty()) {
            binding.signUpEmail.setError("Field is required");
            flag = true;
            requestView = binding.signUpEmail;
        } else if (password.isEmpty()) {
            binding.signUpPassword.setError("Field is required");
            flag = true;
            requestView = binding.signUpPassword;
        } else if (password2.isEmpty()) {
            binding.signUpPassword2.setError("Field is required");
            flag = true;
            requestView = binding.signUpPassword2;
        } else if (password.length() < 8) {
            binding.signUpPassword.setError("Minimum 8 characters");
            flag = true;
            requestView = binding.signUpPassword;
        } else if (!password2.equals(password)) {
            binding.signUpPassword2.setError("Password does not match!");
            flag = true;
            requestView = binding.signUpPassword2;
        }

        if (flag) {
            requestView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void resisterUser() {
    }
}