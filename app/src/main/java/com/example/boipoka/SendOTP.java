package com.example.boipoka;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final TextView inputMobile = findViewById( R.id.inputMobile );
        Button buttonGetOTP = findViewById( R.id.buttonGetOTP );
        final ProgressBar progressBar = findViewById( R.id.progressBar );
        Bundle bundle = getIntent().getExtras();
        String value  = bundle.getString( "phone" );

        if (bundle!= null) {
            inputMobile.setText( value);
        }
        buttonGetOTP.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText( SendOTP.this, "Enter Mobile", Toast.LENGTH_SHORT ).show();
                    return;
                }

                progressBar.setVisibility( View.VISIBLE );
                buttonGetOTP.setVisibility( View.INVISIBLE );

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+880"+inputMobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        SendOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility( View.GONE );
                                buttonGetOTP.setVisibility( View.VISIBLE );
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility( View.GONE );
                                buttonGetOTP.setVisibility( View.VISIBLE );
                                Toast.makeText( SendOTP.this,e.getMessage(),Toast.LENGTH_LONG ).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId , @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility( View.GONE );
                                buttonGetOTP.setVisibility( View.VISIBLE );
                                Intent intent = new Intent(getApplicationContext(),VerifyOTP.class);
                                intent.putExtra("mobile", inputMobile.getText().toString());
                                intent.putExtra("verificationId", verificationId );
                                startActivity( intent );
                            }
                        }
                );







            }
        } );


    }
}