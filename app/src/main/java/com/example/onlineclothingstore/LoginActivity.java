package com.example.onlineclothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlineclothingstore.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //go to register
        binding.txtRegister.setOnClickListener(view -> startActivity(
                new Intent(LoginActivity.this, RegisterActivity.class)));

        binding.goToAdminLogin.setOnClickListener(view -> startActivity(
                new Intent(this, AdminLogin.class)));
        
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsAreReady()){
                    login();
                }
            }
        });
    }

    private boolean fieldsAreReady() {
        email = binding.loginEmail.getText().toString().trim();
        password = binding.loginPassword.getText().toString().trim();

        boolean flag = false;
        View requestView = null;

        if (email.isEmpty()) {
            binding.loginEmail.setError("Field is required");
            flag = true;
            requestView = binding.loginEmail;
        } else if (password.isEmpty()) {
            binding.loginPassword.setError("Field is required");
            flag = true;
            requestView = binding.loginPassword;
        } else if (password.length() < 8) {
            binding.loginPassword.setError("Minimum 8 characters");
            flag = true;
            requestView = binding.loginPassword;
        }

        if (flag) {
            requestView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void login() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}