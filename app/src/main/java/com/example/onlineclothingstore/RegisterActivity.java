package com.example.onlineclothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.UserModel;
import com.example.onlineclothingstore.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String uid, email, username, address, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //back to main activity
        binding.goToLogin.setOnClickListener(view -> startActivity(
                new Intent(RegisterActivity.this, LoginActivity.class)));
        //signup btn
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldReady())
                    resisterUser();
            }
        });

    }
    //validation for registering
    private boolean areFieldReady() {
        username = binding.signUpName.getText().toString().trim();
        email = binding.signUpEmail.getText().toString().trim();
        address = binding.signUpAddress.getText().toString().trim();
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
        } else if (address.isEmpty()) {
                binding.signUpAddress.setError("Field is required");
                flag = true;
                requestView = binding.signUpAddress;
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCES);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    UserModel user = new UserModel(username, email, address,password);
                    databaseReference.child(firebaseAuth.getUid())
                            .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    });
                }
            }
        });
    }
}