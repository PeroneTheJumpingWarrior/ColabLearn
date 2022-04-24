package com.example.qmul_itmbrevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;

    List<ScoreData> list;
    ScoreAdapter adapter;
    DatabaseReference reference;

    Task<QuerySnapshot> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.leaderboard_recycler);
        progressBar = findViewById(R.id.leaderboardProgress);

        reference = FirebaseDatabase.getInstance().getReference().child("UserInformation");

        list = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this);

        manager.setReverseLayout(true); //Reversing the Values obtained from orderByChild() method (from ascending to descending)
        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        //Below tells how should the data be extracted from the Firebase (& ordered in which Format) - Gives values in Ascending order

        reference.orderByChild("score").addValueEventListener(new ValueEventListener() { //Even though the Data in Firebase is timeSpent,
            // but since ScoreData has variable  "score" thus this we did it
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   /*
                    StringScoreData temp = snapshot.getValue(StringScoreData.class);

                    long convertedLongValue = Long.parseLong(temp.getScore());

                    ScoreData data = new ScoreData(temp.getName(),convertedLongValue);
                    */

                    ScoreData data = snapshot.getValue(ScoreData.class);
                    list.add(data);
                }

                adapter = new ScoreAdapter(list, Leaderboard.this);

                recyclerView.setAdapter(adapter);  //The New instance of the ScoreAdapter which we created, we are setting it as the adapter of the
                //Recycler View

                progressBar.setVisibility(View.GONE);  //Not Showing the Progress Bar (So making it disappear)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);

                Toast.makeText(Leaderboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
/*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth authenticate = FirebaseAuth.getInstance();
        ScoreData sData [] = new ScoreData[100];

        task = db.collection("users").get();

        //List<DocumentSnapshot> docList = task.getDocuments();

        recyclerView = findViewById(R.id.leaderboard_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //CustomAdapter c = new CustomAdapter(arr);

      //  recyclerView.setAdapter(c);
*/

    }
}