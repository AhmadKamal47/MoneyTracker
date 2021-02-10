package com.example.moneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public
class MainActivity extends AppCompatActivity {
    private EditText mEmail,mPass;
    private ProgressDialog mDailog;
    private FirebaseAuth mAuth;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        mDailog=new ProgressDialog(this);
        LoginDetail();
    }
    private void LoginDetail(){
        mEmail=findViewById(R.id.email_login);
        mPass=findViewById(R.id.password_login);
        TextView mSignUpHere = findViewById(R.id.signupHere);
        Button btn_SignIn = findViewById(R.id.btn_login);
        btn_SignIn.setOnClickListener(v -> {
            String email= mEmail.getText().toString().trim();
            String password= mPass.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email Required..");
                return;
            }
            if(TextUtils.isEmpty(password)){
                mPass.setError("Password Reqired..");
                return;
            }
            mDailog.setMessage("processing..");
            mDailog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public
                void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mDailog.dismiss();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mDailog.dismiss();
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        mSignUpHere.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),RegistrationActivity.class)));
    }
}