package com.example.notepateapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditNoteActivity extends AppCompatActivity {

    private Intent data;
    private EditText Edtitle,Edcontent;
    private FloatingActionButton fabttn;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Edtitle = findViewById(R.id.edittetit);
        Edcontent = findViewById(R.id.editcontent);
        fabttn = findViewById(R.id.etfabttun);

        data = getIntent();

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.edtoolb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fabttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditNoteActivity.this,NoteActivity.class);
                finish();
                startActivity(intent);
                
                //Toast.makeText(EditNoteActivity.this, "SaveButton Click", Toast.LENGTH_SHORT).show();
                
                String newtitle = Edtitle.getText().toString();
                String newcontent = Edcontent.getText().toString();
                
                
                if (newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(EditNoteActivity.this, "Something is Empty", Toast.LENGTH_SHORT).show();
                    return;

                }else {



                    DocumentReference reference = firestore.collection("notes").document(user.getUid()).collection("myNotes").document();

                    HashMap<String,Object> note = new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    reference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(EditNoteActivity.this, "Note is Update", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(EditNoteActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        String title = data.getStringExtra("title");
        String content = data.getStringExtra("content");
        Edcontent.setText(content);
        Edtitle.setText(title);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}