package com.example.notepateapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        progress = findViewById(R.id.prog1);


        progress.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        },4000);
    }
}