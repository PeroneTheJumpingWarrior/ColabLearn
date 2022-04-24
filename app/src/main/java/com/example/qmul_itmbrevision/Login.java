package com.example.qmul_itmbrevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLoginButton;
    private TextView forgotPasswordBtn, mRegisterBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.registerBtn);
        mRegisterBtn = findViewById(R.id.registerText);
        progressBar = findViewById(R.id.progressBar2);
        forgotPasswordBtn = findViewById(R.id.forgotPassword);

        //Creating Firebase Instance/Object
        fAuth = FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be more than 6 characters long");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Authenticating the User

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                            //This Was Added to Calculate the current Time of the System
                            // As the user logs on Dashboard the Time is recorded to be the Start of his session (From Login Page)
                            long start = Calendar.getInstance().getTimeInMillis(); /*Line Added for LB*/
                            MainActivity.startTime = start;
                            //Till Above Here

                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        }
                        else{

                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //This is done below in case User wants to reset Password as we dont want him to create new account &
        //lose His/Her progress

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email Address to receive your reset Link : ");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Extract the Email & send the Reset Link

                        String email = resetMail.getText().toString();

                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Reset Link has been sent to Your Email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset Link is Not Sent " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }  //In the above methods if the User entered correct email, which is also registered in the
                    //Firebase database "under Authentication" then reset link is sent & user is informed

                    //addOnFailure() states what will happen if not such entered email is found on firebase
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Close the Dialog Box, As User Denied entering the email
                    }
                });

                passwordResetDialog.create().show();  //Displaying the Password Reset Dialog, otherwise wont be shown
            }
        });

    }
}