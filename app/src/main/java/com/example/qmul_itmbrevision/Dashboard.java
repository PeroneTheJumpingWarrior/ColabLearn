package com.example.qmul_itmbrevision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firestore.v1.WriteResult;


import org.w3c.dom.Document;

import java.util.Calendar;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    private TextView userFName, userEMail, userTime;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    long previousTimeValue = 0;     //This Variable is for Storing the Previous Value in Firebase Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userFName = findViewById(R.id.userFullName);
        userEMail = findViewById(R.id.userEmail);
        userTime = findViewById(R.id.timeView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Getting the current User's ID
        userId = fAuth.getCurrentUser().getUid();

        //Creating a DocumentReference instance & telling it to access the document (i.e. the user) whose ID matches
        //the one who is currently online (logged in to the app)

        DocumentReference documentReference = fStore.collection("users").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                userEMail.setText(documentSnapshot.getString("email"));
                userFName.setText(documentSnapshot.getString("fullName"));

                //This was added to display the time being changed for a user (on Dashboard)
                long l = documentSnapshot.getLong("timeSpent");

                String s = Long.toString(l);

                userTime.setText(s);
                //Till Here

            }
        });

    }

    public void moduleRedirect(View view){

          startActivity(new Intent(getApplicationContext(),ModuleSelect.class));
    }

    public void leaderBoardRedirect(View view){

        startActivity(new Intent(Dashboard.this, Leaderboard.class));

        //startActivity(new Intent(getApplicationContext(),Leaderboard.class));
    }

    public void logout(View view){


        // Following Lines have been added to calculate the system time when user exits the app (To subtract it from startTime to calculate time Spent)

        long end = Calendar.getInstance().getTimeInMillis(); /*Lines Added for LB*/
        MainActivity.endTime = end;

        long result = (((MainActivity.endTime - MainActivity.startTime)/1000)/60);

        /* Here the "timeSpent" variable is being updated with the time difference calculated by "result" variable [EndTime - StartTime] above */

        DatabaseReference dbReference;
        FirebaseUser firebaseUser;

        dbReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//This is done to Extract Existing "timeSpent" value (stored as "score") in the Database & Add it with the new "result" value, putting that in Database
        dbReference.child(firebaseUser.getUid()).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override                                                       //NOTE : timeSpent was changed to (score) here to reflect ScoreData class
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long previousTime = dataSnapshot.getValue(Long.class);

                previousTimeValue = previousTime;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        result = result + previousTimeValue;  //The new result value adds previousTimeValue with the new result Value


        dbReference.child(firebaseUser.getUid()).child("score").setValue(result); //The Updated Result Value is added to FirebaseDatabase

        //previousTimeValue = 0;  //Making Previous Time Value as 0, because of Global Variable

        /* Below code shows Updating the timeSpent in Firestore Cloud Database */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth authenticate = FirebaseAuth.getInstance();

        db.collection("users").document(userId).update("timeSpent", result);

        authenticate.signOut();
        db.terminate();
        //Till Here

        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}