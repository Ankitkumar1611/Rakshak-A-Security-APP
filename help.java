package com.example.anand.womensecurity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class help extends AppCompatActivity {
    EditText et1,et2,et3,et4,et5;
    Button b,logoutBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private boolean isContactSaved=false;
    private String stringIntent="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        if (mUser==null){
            Intent intent=new Intent(help.this,login.class);
            startActivity(intent);
            finish();
        }

        mRef= FirebaseDatabase.getInstance().getReference().child("users");
        stringIntent=getIntent().getStringExtra("from_danger");
        System.out.println("String Intent :"+stringIntent);

        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        et4 = (EditText)findViewById(R.id.et4);
        et5 = (EditText)findViewById(R.id.et5);

        b = (Button)findViewById(R.id.save);
        logoutBtn=findViewById(R.id.logout_btn);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(help.this,login.class));
                finish();
            }
        });

/*

        String uid=mUser.getUid();
        mRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("isContactSaved").exists()){
                    String data=dataSnapshot.child("isContactSaved").getValue().toString();

                    startActivity(new Intent(help.this,DangerFound.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        if (!"yes".equals(stringIntent)){
            String uid=mUser.getUid();
            mRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("isContactSaved").exists()){
                        String data=dataSnapshot.child("isContactSaved").getValue().toString();

                        startActivity(new Intent(help.this,DangerFound.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            stringIntent="no";

        }


    }

    private void getDetails() {
        String no1=et1.getText().toString().trim();
        String no2=et2.getText().toString().trim();
        String no3=et3.getText().toString().trim();
        String no4=et4.getText().toString().trim();
        String no5=et5.getText().toString().trim();

        if (no1.isEmpty()){
            et1.setError("This number is mandatory!");
            et1.requestFocus();
            return;
        }
        if (no1.length() !=10 ){
            et1.setError("Wrong number!");
            et1.requestFocus();
            return;
        }

        if (no2.isEmpty()){
            et2.setError("This number is mandatory!");
            et2.requestFocus();
            return;
        }
        if (no2.length() !=10 ){
            et2.setError("Wrong number!");
            et2.requestFocus();
            return;
        }
        if (no3.isEmpty()){
            et3.setError("This number is mandatory!");
            et3.requestFocus();
            return;
        }

        if (no3.length() !=10 ){
            et3.setError("Wrong number!");
            et3.requestFocus();
            return;
        }

        if ((no4.length() < 10 && no4.length() >0) || no4.length() >10){
            et4.setError("Wrong number!");
            et4.requestFocus();
            return;
        }
        if ((no5.length() < 10 && no5.length() >0) || no5.length() >10){
            et5.setError("Wrong number!");
            et5.requestFocus();
            return;
        }

        saveDetails(no1,no2,no3,no4,no5);
    }

    private void saveDetails(String no1, String no2, String no3, String no4, String no5) {

        isContactSaved=true;

        HashMap<String,String> map=new HashMap<>();
        map.put("number1",no1);
        map.put("number2",no2);
        map.put("number3",no3);
        map.put("number4",no4);
        map.put("number5",no5);
        map.put("isContactSaved",isContactSaved+"");

        String uid=mUser.getUid();
        mRef.child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(help.this, "Contacts Saved Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(help.this,DangerFound.class));
                }else{
                    Toast.makeText(help.this, task.getException().getMessage()+"Error found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}

