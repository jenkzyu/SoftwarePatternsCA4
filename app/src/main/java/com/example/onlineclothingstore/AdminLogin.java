package com.example.onlineclothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.databinding.ActivityAdminLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {
    private ActivityAdminLoginBinding binding;
    private String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backToCustomerLogin.setOnClickListener(view -> onBackPressed());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.loginEmail.getText().toString();
                password = binding.loginPassword.getText().toString();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminLogin.this, "Hi", Toast.LENGTH_SHORT).show();

                        }else
                            Toast.makeText(AdminLogin.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}