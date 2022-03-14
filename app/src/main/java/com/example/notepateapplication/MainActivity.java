package com.example.notepateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEt,passwEt;
    private TextView signTv;
    private Button logbttnEt;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        emailEt = findViewById(R.id.emailEt);
        passwEt = findViewById(R.id.passwordEt);
        signTv = findViewById(R.id.signTv);
        logbttnEt = findViewById(R.id.loginbttnEt);
        bar = findViewById(R.id.progEt);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();



        if (user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,NoteActivity.class));

        }




        signTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });



        logbttnEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString().trim();
                String password = passwEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();

                }else {
                    // firebase login

                    bar.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(emailEt.getText().toString(),passwEt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        checkEmailVerification();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Account Doesn't Exist ", Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            });
                }
            }
        });

    }

    private void checkEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()==true){
            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NoteActivity.class));
        }else {
            bar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Verify Your Email First", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }
    }
}