package com.architectica.rental05.thevendorsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    TextView name;
    TextView mobileNumber,email,aadharNumber,address;
    DatabaseReference profileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextView)findViewById(R.id.name);
        mobileNumber = (TextView)findViewById(R.id.mobileNumber);
        email = (TextView)findViewById(R.id.email);
        aadharNumber = (TextView)findViewById(R.id.aadharNumber);
        address = (TextView)findViewById(R.id.address);
        //logOut = (TextView)findViewById(R.id.logOutTextView);

        profileReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid);

        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.child("Name").getValue(String.class));
                mobileNumber.setText(dataSnapshot.child("PhoneNumber").getValue(String.class));
                email.setText(dataSnapshot.child("Email").getValue(String.class));
                aadharNumber.setText(dataSnapshot.child("AadharNumber").getValue(String.class));
                address.setText(dataSnapshot.child("VendorAddress").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
