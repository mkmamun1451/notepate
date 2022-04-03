package com.example.notepateapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        titleDet = findViewById(R.id.detittitl);
        contentDet = findViewById(R.id.contentEtdt);
        detflobttn = findViewById(R.id.detfloabttn);
        Toolbar toolbar = findViewById(R.id.dttoolfb);
        setSupportActionBar(toolbar);

        Intent data = getIntent();

        detflobttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(view.getContext(),EditNoteActivity.class);*/

               /* view.getContext().startActivity(intent);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetilsActivity.this);
                builder.setTitle("Edit Notes");
                builder.setMessage("Are you sure you want to Edit Your Notes?");
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(NoteDetilsActivity.this,EditNoteActivity.class);
                                intent.putExtra("placeId",1);
                                intent.putExtra("title",data.getStringExtra("title"));
                                intent.putExtra("content",data.getStringExtra("content"));
                                intent.putExtra("noteId",data.getStringExtra("noteId"));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
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