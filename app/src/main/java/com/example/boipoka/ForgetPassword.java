package com.example.boipoka;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static android.util.Patterns.EMAIL_ADDRESS;

import androidx.annotation.NonNull;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText emailE;
    private Button reSetButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailE = findViewById( R.id.emailEditText );
        reSetButton = findViewById( R.id.resetPassword );
        progressBar = findViewById( R.id.progressBar );
        auth = FirebaseAuth.getInstance();
        reSetButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetPassword();
            }
        } );
    }
    private void reSetPassword(){
        String email = emailE.getText().toString().trim();
        if (email.isEmpty()){
            emailE.setError( "Email is Required" );
            emailE.requestFocus();
            return;
        }
        if(!EMAIL_ADDRESS.matcher( email ).matches()){
            emailE.setError( "Enter a valid email" );
            emailE.requestFocus();
            return;
        }
        progressBar.setVisibility( View.VISIBLE );
        auth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility( View.INVISIBLE );
                if (task.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Check your email to reset your password",Toast.LENGTH_LONG ).show();
                    Intent intent = new Intent(ForgetPassword.this,LogIn.class);
                    startActivity( intent );
                }
                else {
                    Toast.makeText( getApplicationContext(),"Try again! Something wrong is happened!",Toast.LENGTH_LONG ).show();
                }
            }
        } );


    }
}