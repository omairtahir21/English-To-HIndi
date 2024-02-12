package com.sn.aichat.englishtohindi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            DBManager1 dbObj1 = new DBManager1(this);
            try {
                dbObj1.createDataBase();
                dbObj1.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnDictionary = findViewById(R.id.btnDictionary);
        Button btnFAvourte = findViewById(R.id.btnFavorite);
        Button btnHistory = findViewById(R.id.btnHistory);
        btnDictionary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Dictionary.class);
            startActivity(intent);
        });
        btnFAvourte.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
    }
}