package com.example.moneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public
class RegistrationActivity extends AppCompatActivity {
    private EditText memail,mpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
         mAuth = FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);
        registration();
    }
    private void registration(){
        memail=findViewById(R.id.email_reg);
        mpassword=findViewById(R.id.password_reg);
        Button btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(v -> {
            String email= memail.getText().toString().trim();
            String password= mpassword.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                memail.setError("Email Required..");
            }
            if(TextUtils.isEmpty(password)){
                mpassword.setError("Password Reqired..");
            }
            mDialog.setMessage("Processsing..");
         mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
             if (task.isSuccessful()) {
                 mDialog.dismiss();
                 Toast.makeText(getApplicationContext(), "Registration Completed!", Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(getApplicationContext(), HomeActivity.class));
             }
             else {
                 mDialog.dismiss();
                 Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
             }
         });
        });
    }
}