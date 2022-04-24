package com.example.qmul_itmbrevision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadNotes_InternetProt extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseReference;
    List<PDFFile> uploadPDFList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_notes_internet_prot);

        listView = findViewById(R.id.myListView);
        uploadPDFList = new ArrayList<>();

        viewAllFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PDFFile putPDF = uploadPDFList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(putPDF.getUrl()));
                startActivity(intent);


            }
        });
    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("internetProtocols");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //This For Loop Selects accesses all the files under "uploadOne"
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    //Creating an instance of the Helper Class putPDF & add it to the ArrayList.
                    PDFFile putPDFs = postSnapshot.getValue(com.example.qmul_itmbrevision.PDFFile.class);
                    uploadPDFList.add(putPDFs);

                    String[] uploads = new String[uploadPDFList.size()];

                    for (int i = 0; i < uploads.length; i++) {

                        uploads[i] = uploadPDFList.get(i).getName();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads) {


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);

                            TextView myText = (TextView) view.findViewById(android.R.id.text1);
                            myText.setTextColor(Color.BLACK);

                            return view;
                        }
                    };
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
