package com.example.notepateapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateNote extends AppCompatActivity {

    private EditText titleEt,contentEt;
    private FloatingActionButton flobttn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private Toolbar toolbar;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        flobttn = findViewById(R.id.flsavbttn);
        titleEt = findViewById(R.id.createtit);
        contentEt = findViewById(R.id.contentEt);
        bar = findViewById(R.id.progcreat);


        toolbar = findViewById(R.id.toobft);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();




        flobttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNote.this);
                builder.setTitle("UpDate Notes");
                builder.setMessage("Are you sure you want to Update this Notes?");
                builder.setIcon(R.drawable.ic_update_24);
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CreateNote.this,NoteActivity.class);
                        intent.putExtra("placeId",1);
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


                String title = titleEt.getText().toString();
                String content = contentEt.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(CreateNote.this, "Both Field Are Require", Toast.LENGTH_SHORT).show();
                }else {

                    bar.setVisibility(View.VISIBLE);

                    DocumentReference reference = firestore.collection("notes").document(user.getUid()).collection("myNotes").document();

                    HashMap<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    reference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(CreateNote.this, "Note Created Successfully", Toast.LENGTH_SHORT).show();
                            /*startActivity(new Intent(CreateNote.this,NoteActivity.class));*/
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNote.this, "Field to Create Note", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    });


                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

       if (item.getItemId()==android.R.id.home){
           onBackPressed();
       }

        return super.onOptionsItemSelected(item);
    }
}