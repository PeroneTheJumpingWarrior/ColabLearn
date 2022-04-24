package com.example.qmul_itmbrevision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModuleSelect extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_select);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button1);
        button3 = findViewById(R.id.button1);
        button4 = findViewById(R.id.button1);
    }

    public void buttonOneListener(View v){

        Intent intent = new Intent(this, UploadReadChoice_SoftwareEng.class);

        startActivity(intent);
    }

    public void buttonTwoListener(View v){

        Intent intent = new Intent(this, UploadReadChoice_InternetProt.class);

        startActivity(intent);

    }

    public void buttonThreeListener(View v){

        Intent intent = new Intent(this, UploadReadChoice_DatabaseSys.class);

        startActivity(intent);

    }
    public void buttonFourListener(View v){

        Intent intent = new Intent(this, UploadReadChoice_BusinessModel.class);

        startActivity(intent);
    }
}