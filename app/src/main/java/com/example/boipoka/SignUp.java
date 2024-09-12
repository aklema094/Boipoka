package com.example.boipoka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    TextView signin;
    FirebaseAuth auth;
    EditText name,email,password,confirmPassword,phone;
    Button signUp;
    ProgressBar progressBar;
    FirebaseUser user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        signin = findViewById(R.id.signIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
            }
        });

        // initializing
        signUp = findViewById(R.id.signUpButton);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        progressBar = findViewById(R.id.progressBar);
        // signup
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.GONE);
                String emailS,passwordS,nameS,phoneS,confirmPass;
                emailS = email.getText().toString().trim();
                passwordS = password.getText().toString().trim();
                nameS = name.getText().toString().trim();
                phoneS = phone.getText().toString().trim();
                confirmPass = confirmPassword.getText().toString().trim();
                if (!isValidInfo(emailS,nameS,phoneS,passwordS,confirmPass)){
                    Toast.makeText(SignUp.this, "fill with correct information", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //FireBase Authentication
                    auth.createUserWithEmailAndPassword(emailS, passwordS)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    signUp.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        // Sign up success
                                        Toast.makeText(SignUp.this, "Successfully account created", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUp.this, "Failed to create account", Toast.LENGTH_SHORT).show();

                                    }
                                }


                            });
                }

                //FireBase Authentication


            }//
        });// end of signup

    }
    // check is all information is valid or not
    public boolean isValidInfo(String emailT,String nameT,String phoneT,String pass,String confirmPass){

        if (nameT.isEmpty()){
            name.setError("name required");
            return false;
        }

        if ( emailT.isEmpty()) {
            email.setError("email required");
            return false;
        }
        if(!validEmailFormat(emailT)){
            email.setError("enter a valid email");
            return false;
        }
        if (phoneT.isEmpty()){
            phone.setError("phone number required");
            return false;
        }
        if (!isValidPhoneNumber(phoneT)){
            phone.setError("enter a valid phone number");
        }
        if (pass.isEmpty()){
            password.setError("password required");
            return false;
        }
        if (confirmPass.isEmpty()){
            confirmPassword.setError("confirm password required");
            return false;
        }
        if (!confirmPass.equals(pass)){
            confirmPassword.setError("confirm password should be match with password");
            return false;
        }
        return true;

    }

    // check is given email format is valid email or not
    public boolean validEmailFormat(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // check is given phone number is valid email or not
    public boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
}