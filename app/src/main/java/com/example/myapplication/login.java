package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        auth = FirebaseAuth.getInstance();
    }

    private void showMessage(String title, String msg){
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msg)
                .show();
    }


    public void go1(View view){
        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage("Success","User created");
                        } else {
                            showMessage("Error", task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    public void go2(View view){
        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage("Success","User sign in successfully");
                            // Get user id
                            String userId = auth.getUid();
                            System.out.println(userId);

//                          go to other activity
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                            // Put user id into the Intent
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                        } else {
                            showMessage("Error", task.getException().getLocalizedMessage());
                        }
                    }
                });
    }


}