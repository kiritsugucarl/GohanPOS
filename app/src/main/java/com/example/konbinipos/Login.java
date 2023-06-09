package com.example.konbinipos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private TextInputEditText editEmail, editPassword;
    private TextView clickToRegister;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageSliderAdapter adapter;
    private ViewPager2 viewPager;
    private List<Integer> images;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and go to the right activity right away.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), OrderType.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting firebase instance
        mAuth = FirebaseAuth.getInstance();

        // component fetching
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        clickToRegister = findViewById(R.id.registerNow);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewPager);

        images = new ArrayList<>();
        images.add(R.drawable.banner1);
        images.add(R.drawable.banner2);
        images.add(R.drawable.banner3);

        adapter = new ImageSliderAdapter(images);
        viewPager.setAdapter(adapter);

        String text = "Not registered? Click to register";
        String firstPortion = "Not registered?";

        SpannableString spannableString = new SpannableString(text);

        // Set the color for the first portion ("Not registered?")
        ForegroundColorSpan firstColorSpan = new ForegroundColorSpan(Color.parseColor("#767171")); // Change the color as needed
        spannableString.setSpan(firstColorSpan, 0, firstPortion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the color for the second portion ("Click to register")
        ForegroundColorSpan secondColorSpan = new ForegroundColorSpan(Color.parseColor("#B1464A")); // Change the color as needed
        spannableString.setSpan(secondColorSpan, firstPortion.length() + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        clickToRegister.setText(spannableString);

        // Go to Register Activity
        clickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        // login button clicked
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //show loading bar
                progressBar.setVisibility(View.VISIBLE);

                String email, password;

                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

                // Empty fields checker
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase API for Sign In
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), OrderType.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Login.this, "Authentication failed, check your email and password",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}