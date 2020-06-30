package com.sanjaydevtech.firechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sanjaydevtech.firechat.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailTxt.getText().toString().trim();
                String pwd = binding.pwdTxt.getText().toString().trim();
                if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    binding.loginBtn.setEnabled(false);
                    binding.registerBtn.setEnabled(false);
                    loginWithCredentials(email, pwd);
                }


            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailTxt.getText().toString().trim();
                String pwd = binding.pwdTxt.getText().toString().trim();
                if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    binding.loginBtn.setEnabled(false);
                    binding.registerBtn.setEnabled(false);
                    registerWithCredentials(email, pwd);
                }
            }
        });
    }

    private void registerWithCredentials(String email, String pwd) {
        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Register operation failed", Toast.LENGTH_SHORT).show();
                    }
                })
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                binding.loginBtn.setEnabled(true);
                binding.registerBtn.setEnabled(true);
            }
        });
    }

    private void loginWithCredentials(String email, String pwd) {
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Login operation failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.loginBtn.setEnabled(true);
                        binding.registerBtn.setEnabled(true);
                    }
                });
    }
}