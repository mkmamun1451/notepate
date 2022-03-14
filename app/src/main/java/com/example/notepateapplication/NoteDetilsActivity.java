package com.example.notepateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetilsActivity extends AppCompatActivity {
    private TextView titleDet, contentDet;
    private FloatingActionButton detflobttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detils);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleDet = findViewById(R.id.detittitl);
        contentDet = findViewById(R.id.contentEtdt);
        detflobttn = findViewById(R.id.detfloabttn);
        Toolbar toolbar = findViewById(R.id.dttoolfb);
        setSupportActionBar(toolbar);

        Intent data = getIntent();

        detflobttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),EditNoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);

            }
        });

        contentDet.setText(data.getStringExtra("content"));
        titleDet.setText(data.getStringExtra("title"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}