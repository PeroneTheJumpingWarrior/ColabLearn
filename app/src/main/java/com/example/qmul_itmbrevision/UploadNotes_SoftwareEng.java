package com.example.qmul_itmbrevision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadNotes_SoftwareEng extends AppCompatActivity {

        EditText editPDFName;
        Button uploadBtn, viewFilesBtn;

        StorageReference storageReference;
        DatabaseReference databaseReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_notes_software_eng);

            editPDFName = findViewById(R.id.editPDFName);
            uploadBtn = findViewById(R.id.uploadBtn);

            //Creates a reference in the Firebase Realtime Database (if not created already) under which files
            // can be uploaded
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("softwareEngineering");

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectPDFFile();
                }
            });
        }

        public void selectPDFFile(){

            Intent intent = new Intent();
            intent.setType("application/pdf");    //Specifying the type of File to be selected which is to be uploaded
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Your PDF File"),1);
        }


        //This overridden method will perform the main functionality
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            //IF statement states to check the requestCode & if the data entered is not null
            if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){

                uploadPDFFile(data.getData());

            }
        }

        private void uploadPDFFile(Uri data) {

            //On RHS we are using the storage reference we created above "uploadOne" to a new storage reference
            //which is being used to upload the PDF File to Firebase Realtime Database

            StorageReference reference = storageReference.child("softwareEngineering/"+System.currentTimeMillis()+".pdf");

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();

            //putFile method puts the file to the Firebase Database

            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //After File is uploaded in the realtime database under child "uploadOne" then this method
                            // will be called.
                            //In this method we are storing the filename & url to the Realtime Database

                            // We are passing the URl (Link) of the File which has been uploaded to the Firebase
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();

                            while(!uri.isComplete());

                            Uri url = uri.getResult();

                         /*We obtained the Link of the File stored in Storage so that we can save the URL (Link Name)
                          in the Firebase Realtime Database.

                          Basically the Realtime Database is structured to store A File's "Name" & "URL" (See PDFFile Class)

                          in PDFFile Class we defined this structure.

                          While the actual file the User stores is in Firebase Storage. So we retrieved the File's URL

                          so that the Link of the file can be stored in the Realtime Database

                          */

                            //Here we created an Object of Put PDF Class & passed the Name & the extracted URL (link)
                            //of the file Stored in the Firebase Storage
                            PDFFile pdfFileObject = new PDFFile(editPDFName.getText().toString(), url.toString());

                            //Pushing the putPDFObject created into the Realtime Database
                            databaseReference.child(databaseReference.push().getKey()).setValue(pdfFileObject);

                            Toast.makeText(UploadNotes_SoftwareEng.this,"File Uploaded",Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            //This method is used to inform us about the progress of our file upload (how much uploaded, how
                            //much remaining)

                            //Calculating Progress Value by a calculation & storing it in a variable
                            double progress = (100*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                            progressDialog.setMessage("Uploading: "+(int)progress+"%");


                        }
                    });
        }

        public void btn_action(View view){

            //startActivity(new Intent(getApplicationContext(), View_PDF_1_Files.class));
        }
    }