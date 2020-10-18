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

public class login extends AppCompatActivity {
    EditText et1, et2;
    Button blgn, btn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        et1 = (EditText) findViewById(R.id.email);
        et2 = (EditText) findViewById(R.id.pass);
        blgn = (Button) findViewById(R.id.btnlgn);
        btn = (Button) findViewById(R.id.btn);
        blgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=et1.getText().toString().trim();
                String password=et2.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    et1.setError("Email Can't be blank");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    et2.setError("Please insert a valid E-mail");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "login sucsessful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this,help.class));
                        }else{
                            Toast.makeText(login.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }


                });


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,MainActivity.class));

            }
        });
    }


}
