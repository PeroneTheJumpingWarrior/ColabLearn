package com.example.qmul_itmbrevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadReadChoice_DatabaseSys extends AppCompatActivity {

    Button uploadNotesBtn;
    Button readNotesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_read_choice_database_sys);

        uploadNotesBtn = findViewById(R.id.uploadNotesBtn);
        readNotesBtn = findViewById(R.id.readNotesBtn);

        uploadNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), UploadNotes_DatabaseSys.class));
            }
        });

        readNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ReadNotes_DatabaseSys.class));
            }
        });
    }
}
