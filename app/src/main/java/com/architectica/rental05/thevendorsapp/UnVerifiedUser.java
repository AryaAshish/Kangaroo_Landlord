package com.architectica.rental05.thevendorsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.architectica.rental05.thevendorsapp.FirstRunSecondActivity.userUid;
import static com.architectica.rental05.thevendorsapp.FirstRunSecondActivity.vendorName;

public class UnVerifiedUser extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this,FirstRunSecondActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            //overridePendingTransition(R.anim.activit_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_verified_user);

        MainActivity.isMainActivity = false;

        toolbar = (Toolbar) findViewById(R.id.unverifiedToolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Log.i("check",userUid);

        DatabaseReference nameReference = FirebaseDatabase.getInstance().getReference("Vendors/" + userUid );

        nameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vendorName = dataSnapshot.child("Name").getValue(String.class);

                getSharedPreferences("VendorName",MODE_PRIVATE)
                        .edit()
                        .putString("Name",vendorName)
                        .apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference("Vendors/" + userUid + "/Status");
        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("ref",dataSnapshot.getRef().toString());

                if ("Verified".equals(dataSnapshot.getValue(String.class))) {

                    //vendor verified

                    Toast.makeText(UnVerifiedUser.this, "Vendor verified", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UnVerifiedUser.this,FirstActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else {

                    //vendor not verified
                    //do nothing

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
