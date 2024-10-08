package com.example.boipoka;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class VerifyOTP extends AppCompatActivity {
    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            TextView textMobile = findViewById( R.id.textMobile );
            textMobile.setText(String.format(
                    "+880-%s",getIntent().getStringExtra( "mobile" )
            ));

            inputCode1= findViewById( R.id.inputCode1 );
            inputCode2= findViewById( R.id.inputCode2 );
            inputCode3= findViewById( R.id.inputCode3 );
            inputCode4= findViewById( R.id.inputCode4 );
            inputCode5= findViewById( R.id.inputCode5 );
            inputCode6= findViewById( R.id.inputCode6 );


            setupOTPInputs();
            final ProgressBar progressBar = findViewById( R.id.progressBar );
            final Button buttonVerify = findViewById( R.id.buttonVerify );

            verificationId = getIntent().getStringExtra( "verificationId" );

            buttonVerify.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputCode1.getText().toString().trim().isEmpty()
                            ||inputCode2.getText().toString().trim().isEmpty()
                            ||inputCode3.getText().toString().trim().isEmpty()
                            ||inputCode4.getText().toString().trim().isEmpty()
                            ||inputCode5.getText().toString().trim().isEmpty()
                            ||inputCode6.getText().toString().trim().isEmpty()){
                        Toast.makeText( VerifyOTP.this, "Please enter valid code", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                    String code =
                            inputCode1.getText().toString() +
                                    inputCode2.getText().toString() +
                                    inputCode3.getText().toString() +
                                    inputCode4.getText().toString() +
                                    inputCode5.getText().toString() +
                                    inputCode6.getText().toString() ;

                    if (verificationId != null){
                        progressBar.setVisibility( View.VISIBLE );
                        buttonVerify.setVisibility( View.INVISIBLE );
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential( verificationId,code );
                        FirebaseAuth.getInstance().signInWithCredential( phoneAuthCredential )
                                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility( View.GONE );
                                        buttonVerify.setVisibility( View.VISIBLE );
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(),LogIn.class );
                                            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                            startActivity( intent );
                                        }
                                        else {
                                            Toast.makeText( VerifyOTP.this, "The verification code entered was invalid", Toast.LENGTH_LONG  ).show();
                                        }
                                    }
                                } );
                    }
                }
            } );
            findViewById( R.id.textResendOTP ).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+88"+ getIntent().getStringExtra( "mobile" ),
                            60,
                            TimeUnit.SECONDS,
                            VerifyOTP.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText( VerifyOTP.this,e.getMessage(),Toast.LENGTH_SHORT ).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newVerificationId , @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    verificationId = newVerificationId;
                                    Toast.makeText( VerifyOTP.this, "OTP Sent",Toast.LENGTH_SHORT ).show();
                                }
                            }
                    );
                }
            } );


        }

    private void setupOTPInputs(){
        inputCode1.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode2.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode3.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode4.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
        inputCode5.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );
    }
    }
