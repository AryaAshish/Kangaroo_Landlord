package com.architectica.rental05.thevendorsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayImages extends AppCompatActivity {

    List<String> vehicleImageViews;
    DatabaseReference imageReference;
    ImageAdapter imageAdapter;
    private TextView mToolText;
    private Toolbar toolbar;
    RecyclerView imagesRecyclerView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(R.anim.activit_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        vehicleImageViews = new ArrayList<String>();
        mToolText = (TextView) findViewById(R.id.text);
        mToolText.setText("Extra Images");
        toolbar = (Toolbar) findViewById(R.id.activity_images_toolbar);
        imagesRecyclerView = (RecyclerView)findViewById(R.id.images_recycler_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

        imageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vehicleImageViews.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (HotelAdapter.city.equals(snapshot.child("City").getValue(String.class)) && HotelAdapter.type.equals(snapshot.child("VehicleType").getValue(String.class)) && HotelAdapter.name.equals(snapshot.child("VehicleName").getValue(String.class)) && HotelAdapter.address.equals(snapshot.child("ParkingAddress").getValue(String.class))){

                        DataSnapshot snapshot1 = snapshot.child("ExtraImages");

                        for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){

                            for (DataSnapshot snapshot2 : dataSnapshot1.getChildren()) {
                                vehicleImageViews.add(snapshot2.getValue(String.class));
                            }

                        }

                    }

                }

                imageAdapter = new ImageAdapter(DisplayImages.this,vehicleImageViews);
                imagesRecyclerView.setAdapter(imageAdapter);
                imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                imagesRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

