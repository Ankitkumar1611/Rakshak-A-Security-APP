package com.example.anand.womensecurity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity{


    EditText et1, et3, et4, et5;
    Button b;
    FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String uid=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        et1 = (EditText) findViewById(R.id.name);
        et3 = (EditText) findViewById(R.id.email);
        et4 = (EditText) findViewById(R.id.pass);
        et5 = (EditText) findViewById(R.id.pno);
        b = (Button) findViewById(R.id.bsignup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name=et1.getText().toString().trim();
                String email=et3.getText().toString().trim();
                String password=et4.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    et3.setError("Email is requires");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    et4.setError("password is required");
                    return ;
                }
                if(password.length()<6){
                    et4.setError("password must be greater than 6 character");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            currUser=mAuth.getCurrentUser();
                            uid=currUser.getUid();

                            UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            currUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),login.class));
                                    }else{
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        }
                });
            }
        });
    }
}
