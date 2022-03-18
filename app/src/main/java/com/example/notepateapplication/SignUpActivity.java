package com.example.notepateapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);


                String email = emailsEt.getText().toString().trim();
                String password = passwoEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else if (password.length()<5){
                    Toast.makeText(SignUpActivity.this, "Password Should Greater then 5 digits", Toast.LENGTH_SHORT).show();
                }else {
                    // firebase

                    auth.createUserWithEmailAndPassword(emailsEt.getText().toString(),passwoEt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if (task.isSuccessful()){

                                String notes = auth.getCurrentUser().getUid();
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