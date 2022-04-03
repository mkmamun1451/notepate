package com.example.notepateapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEt,phoneEt,emailsEt,passwoEt;
    private Button signbttn;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailsEt = findViewById(R.id.emailsEt);
        passwoEt = findViewById(R.id.passEt);
        signbttn = findViewById(R.id.signupbttnEt);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        signbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);*/
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Signing Up");
                builder.setMessage("Are you sure you Are you sure your Email & Password Correct??");
                builder.setIcon(R.drawable.ic_turned_in_24);
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


                String email = emailsEt.getText().toString().trim();
                String password = passwoEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else if (password.length()<=6){
                    Toast.makeText(SignUpActivity.this, "Password Should Greater then 6 digits", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                    intent.putExtra("placeId",1);
                    startActivity(intent);
                    // firebase

                    auth.createUserWithEmailAndPassword(emailsEt.getText().toString(),passwoEt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if (task.isSuccessful()){

                                String notes = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                DatabaseReference dataRef = reference.child("notes");
                                HashMap<String,Object> notesdetails = new HashMap<>();

                                notesdetails.put("name",nameEt.getText().toString());
                                notesdetails.put("phone",phoneEt.getText().toString());
                                notesdetails.put("email",emailsEt.getText().toString());
                                notesdetails.put("notes",notes);

                                dataRef.child(notes).setValue(notesdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "Registration is Successful", Toast.LENGTH_SHORT).show();
                                            sendEmailVerification();

                                        }else {
                                            Toast.makeText(SignUpActivity.this, "Registration is Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUpActivity.this, "Verification Email is Send, Verify Email", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    finish();
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));

                }
            });
        }else {
            Toast.makeText(SignUpActivity.this, "Field To Send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}