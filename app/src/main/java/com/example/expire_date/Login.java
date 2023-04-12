package com.example.expire_date;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextInputEditText email, password;
    Button login;
    TextView tv_reg;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if (mauth.getCurrentUser() != null) {
            startActivity(new Intent(this, Home.class));
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        login.setOnClickListener(v -> {
            String mail, pass;
            mail = email.getText().toString();
            pass = password.getText().toString();
            mauth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                }
            });
        });

        tv_reg.setOnClickListener(v -> {
            startActivity(new Intent(this, Register.class));
            finish();
        });
    }

    private void init() {
        email = findViewById(R.id.Email1);
        password = findViewById(R.id.Password1);

        login = findViewById(R.id.Btn_login);

        tv_reg = findViewById(R.id.TV_REGISTER_LOGIN);

        mauth = FirebaseAuth.getInstance();
    }
}