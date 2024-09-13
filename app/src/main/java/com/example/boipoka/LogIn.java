package com.example.boipoka;

import static android.util.Patterns.EMAIL_ADDRESS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    private EditText emailEditText,passwordEditText;
    private Button sign_in;
    private TextView signUp,  forgetPass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailEditText = findViewById( R.id.emailId );
        passwordEditText = findViewById( R.id.passwordId );
        sign_in = findViewById( R.id.signInButton );
        forgetPass = findViewById( R.id.forgetPassword );
        progressBar= findViewById( R.id.progressBar );
        mAuth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
            }
        });
        forgetPass.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this,ForgetPassword.class);
                startActivity( intent );
            }
        } );
        sign_in.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSignIn();
            }

            private void UserSignIn() {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty()){
                    emailEditText.setError( "Enter an email" );
                    emailEditText.requestFocus();
                    return;
                }
                if(!EMAIL_ADDRESS.matcher( email ).matches()){
                    emailEditText.setError( "Enter a valid email" );
                    emailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    passwordEditText.setError( "Enter a password" );
                    passwordEditText.requestFocus();
                    return;
                }
                progressBar.setVisibility( View.VISIBLE );
                sign_in.setVisibility( View.INVISIBLE );
                mAuth.signInWithEmailAndPassword( email,password )
                        .addOnCompleteListener( LogIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility( View.GONE );
                                sign_in.setVisibility( View.VISIBLE );
                                if (task.isSuccessful()){
                                    Toast.makeText( getApplicationContext(),"SignIn Successfully",Toast.LENGTH_SHORT ).show();
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String userID = currentUser.getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child( userID ).child( "Choice" );
                                    databaseReference.addValueEventListener( new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()){
                                                Intent intent = new Intent(LogIn.this,MainActivity.class);
                                                startActivity( intent );
                                            }
                                            else {
                                                Intent intent = new Intent(LogIn.this,Choicelist.class);
                                                startActivity( intent );
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    } );

                                }
                                else {


                                    Toast.makeText( getApplicationContext(),"Try again!! SignIn failed",Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } );
            }
        } );





    }
}