package com.example.qmul_itmbrevision;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//These 3 were added below for LB
import java.util.Calendar;
import java.util.Date;
import java.time.ZonedDateTime;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText mFullName, mEmail, mPassword;
    private TextView mLoginBtn;
    private Button mRegsiterButton;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore fStore; //Creating an instance of the Firestore Database
    private String userID;             //We have to create A variable of UserID as it is our identifier.

    //This Was Added
    public static long userTime = 0;
    public static long startTime = 0;
    public static long endTime = 0;
    //Till Here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFullName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegsiterButton = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();  //Instantiating the Firebase Authentication Service

        fStore = FirebaseFirestore.getInstance(); //Instantiating the Firebase Cloud Firestore Database

        //If user is currently logged in the Application, then start the Dashboard Screen/Activity
        //No need to open the registration Screen, it can be skipped

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }

        //Upon Clicking the Register Button, the Fields will be checked

        mRegsiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();

                long timeSpent = 0;  //When user registers into the Application, their TimeSpent is 0

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

                //If all the conditions above are met correctly then "Registering the User in Firebase"

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  //Process of Registering the user is called as Tasks

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            /* Below Code shows User Being Registered in Firestore Database */

                            userID = fAuth.getCurrentUser().getUid();  //Getting the Current Logged in User's Unique ID

                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>(); //Storing the user's data in a HashMap

                            user.put("fullName",fullName);   //Using the put() method to enter the Data in the Hash Map
                            user.put("email",email);     //as a "key" value pair.(Because Firestore stores data as JSON)

                            //This was Added to alter the Collection to store time

                            user.put("timeSpent",timeSpent);

                            /*Till Here Data is stored in Firestore Database*/

                            //Below is how Data is registered in Firebase Database

                            /* If the creation of the User is successful then the Data is stored in the Firebase Database (Username & TimeSpent)
                             * under the Child ("UserInformation") */

                            DatabaseReference dbReference;
                            FirebaseUser firebaseUser;

                            dbReference = FirebaseDatabase.getInstance().getReference("UserInformation");

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            HashMap firebaseUserMap = new HashMap();

                            firebaseUserMap.put("name",fullName);
                            firebaseUserMap.put("score",0);

                            dbReference.child(firebaseUser.getUid()).setValue(firebaseUserMap);

                            // dbReference.child(firebaseUser.getUid()).child("name").setValue(fullName);
                            //dbReference.child(firebaseUser.getUid()).child("score").setValue(timeSpent);  //Even though it is timeSpent, still value is stored as score

                            /* Till Here the Data was being entered into the Firebase Database with (Name & timeSpent) */

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(TAG, "onSuccess: user profile is created for "+userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure : "+e.toString());
                                }
                            });

                            //This Was Added to Calculate the current Time of the System
                            // As the user logs on Dashboard the Time is recorded to be the Start of his session (From Registration Page)

                            long start = Calendar.getInstance().getTimeInMillis(); /*Line Added for LB*/
                            startTime = start;

                            //Till Above Here

                            startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        }
                        else{

                            Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                            //If Not then the user is not registered
                        }

                    }
                });
            }


        });

        //If the user has already an account & clicks on Login Button then Login Activity is started & Login Screen
        //is displayed

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}
