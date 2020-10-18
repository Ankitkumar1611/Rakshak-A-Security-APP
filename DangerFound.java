package com.example.anand.womensecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangerFound extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private Button logoutButton, editContacts;
    private DatabaseReference mRef;
    String no1=null, no2=null, no3=null, no4=null, no5=null, lat=null, longg=null;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    private TextView latTextView;
    private TextView lonTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_found);

        latTextView = findViewById(R.id.text_lat);
        lonTextView=findViewById(R.id.text_lon);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("users");

        Button button = findViewById(R.id.danger_button);
        logoutButton = findViewById(R.id.logout_button);
        editContacts = findViewById(R.id.edit_contacts);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DangerFound.this, "Under Process!", Toast.LENGTH_SHORT).show();
                sendSms();

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(DangerFound.this, login.class));
                finish();
            }
        });

        editContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DangerFound.this,help.class);
                intent.putExtra("from_danger","yes");
                startActivity(intent);
            }
        });

        String uid=curUser.getUid();

        mRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                no1 =dataSnapshot.child("number1").getValue().toString().trim();
                no2 =dataSnapshot.child("number2").getValue().toString().trim();
                no3 =dataSnapshot.child("number3").getValue().toString().trim();
                no4 =dataSnapshot.child("number4").getValue().toString().trim();
                no5 =dataSnapshot.child("number5").getValue().toString().trim();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendSms() {

        Intent intent=new Intent(getApplicationContext(),DangerFound.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        SmsManager sms= SmsManager.getDefault();

        Toast.makeText(this, ""+no1, Toast.LENGTH_SHORT).show();

        try {
            sms.sendTextMessage(no1, null, "Please help me i am "+curUser.getDisplayName()+" currently in a danger location  http://maps.google.com/?ie=UTF8&hq=&ll="+lat+","+longg+"&z=22", pi, null);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+" Unable to send message to "+no1, Toast.LENGTH_SHORT).show();
        }

        try {
            sms.sendTextMessage(no2, null, "Please help me i am "+curUser.getDisplayName()+" currently in a danger location  http://maps.google.com/?ie=UTF8&hq=&ll="+lat+","+longg+"&z=22", pi, null);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+" Unable to send message to "+no2, Toast.LENGTH_SHORT).show();
        }
        try {
            sms.sendTextMessage(no3, null, "Please help me i am "+curUser.getDisplayName()+" currently in a danger location  http://maps.google.com/?ie=UTF8&hq=&ll="+lat+","+longg+"&z=22", pi, null);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+" Unable to send message to "+no3, Toast.LENGTH_SHORT).show();
        }
        try {

            if (no4.length()>0){
                sms.sendTextMessage(no4, null, "Please help me i am "+curUser.getDisplayName()+" currently in a danger location  http://maps.google.com/?ie=UTF8&hq=&ll="+lat+","+longg+"&z=22", pi, null);

            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+" Unable to send message to "+no4, Toast.LENGTH_SHORT).show();
        }
        try {

            if (no5.length()>0){
                sms.sendTextMessage(no5, null, "Please help me i am "+curUser.getDisplayName()+" currently in a danger location  http://maps.google.com/?ie=UTF8&hq=&ll="+lat+","+longg+"&z=22", pi, null);

            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+" Unable to send message to "+no5, Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (curUser == null) {
            Intent intent = new Intent(DangerFound.this, login.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");
                                    lat=Double.toString( location.getLatitude());
                                    longg=Double.toString( location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
}

