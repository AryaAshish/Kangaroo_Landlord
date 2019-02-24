package com.architectica.rental05.thevendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VehiclesForRentActivity extends AppCompatActivity {

    int redColorOn;
    private TextView mToolText;
    List<String> mCity;
    private Toolbar toolbar;
    RecyclerView mHotelsRecyclerView;
    HotelAdapter mHotelAdapter;
    private TextView mRoomOriginalCost;
    List<String> mVehicleNames;
    List<String> mVehicleLocations;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> VendorNames;
    List<Integer> noOfParkingAddresses;
    ArrayList<Integer> mDelete = new ArrayList<Integer>(100);
    ArrayList<Integer> mBlock = new ArrayList<Integer>(100);
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference,mReference;
    List<String> vehicleType;
    List<String> parkingAddresses;
    static List<String> roomStatuses;
    String isAddedToFavourites;
    String image;
    String data;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    ProgressDialog pd;
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
        setContentView(R.layout.activity_vehicles_for_rent);

        redColorOn = 0;

        Intent intent = getIntent();
        mVehicleNames = new ArrayList<String>();
        mVehicleLocations = new ArrayList<String>();
        mPricePerHour = new ArrayList<String>();
        mPricePerDay = new ArrayList<String>();
        mVehicleImages = new ArrayList<String>();
        VendorNames = new ArrayList<String>();
        orderId = new ArrayList<String>();
        txnAmount = new ArrayList<String>();
        bankTxnId = new ArrayList<String>();
        txnId = new ArrayList<String>();
        bankName = new ArrayList<String>();
        mCity = new ArrayList<String>();
        vehicleType = new ArrayList<String>();
        parkingAddresses = new ArrayList<String>();
        noOfParkingAddresses = new ArrayList<Integer>();
        roomStatuses = new ArrayList<String>();
        HotelAdapter.content = "UploadedVehicles";

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference("Vendors/" + FirstRunSecondActivity.userUid);

        mRoomOriginalCost = (TextView) findViewById(R.id.pricePerHour) ;

        mHotelsRecyclerView = (RecyclerView) findViewById(R.id.hotels_recycler_view);

        pd = new ProgressDialog(VehiclesForRentActivity.this);
        pd.setMessage("loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("UploadedVehicles")){

                    mDatabaseReference = mReference.child("UploadedVehicles");

                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            mPricePerDay.clear();
                            mPricePerHour.clear();
                            mVehicleLocations.clear();
                            mVehicleNames.clear();
                            mVehicleImages.clear();
                            mCity.clear();
                            vehicleType.clear();
                            parkingAddresses.clear();
                            noOfParkingAddresses.clear();
                            VendorNames.clear();
                            mDelete.clear();
                            orderId.clear();
                            txnId.clear();
                            bankName.clear();
                            bankTxnId.clear();
                            txnAmount.clear();
                            mBlock.clear();
                            roomStatuses.clear();
                            int k = 0;

                            for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                                mVehicleImages.add(chidSnap.child("VehiclePhoto").getValue(String.class));
                                mVehicleNames.add(chidSnap.child("VehicleName").getValue(String.class));
                                mPricePerHour.add(chidSnap.child("PricePerHour").getValue(String.class));
                                mPricePerDay.add(chidSnap.child("PricePerDay").getValue(String.class));
                                mCity.add(chidSnap.child("City").getValue(String.class));
                                vehicleType.add(chidSnap.child("VehicleType").getValue(String.class));
                                roomStatuses.add(chidSnap.child("status").getValue(String.class));
                                mVehicleLocations.add(chidSnap.child("ParkingAddress").getValue(String.class));
                                mDelete.add(R.drawable.ic_delete_black_24dp);
                                if ("true".equals(chidSnap.child("isVehicleBlocked").getValue(String.class))){

                                    mBlock.add(R.drawable.ic_lock_open_black_24dp);

                                }
                                else if ("BlockedByAdmin".equals(chidSnap.child("isVehicleBlocked").getValue(String.class))){

                                    mBlock.add(R.drawable.ic_lock_black_24dp);

                                }
                                else {

                                    mBlock.add(R.drawable.ic_block_black_24dp);

                                }
                                orderId.add(null);
                                txnAmount.add(null);
                                bankTxnId.add(null);
                                txnId.add(null);
                                bankName.add(null);

                                k++;

                            }

                            Log.i("status","loop left");

                            pd.dismiss();

                            mHotelAdapter = new HotelAdapter(VehiclesForRentActivity.this,mVehicleImages,VendorNames,mCity,vehicleType,mVehicleNames,
                                    mPricePerHour, mPricePerDay,mVehicleLocations,orderId,txnAmount,bankTxnId,txnId,bankName,mDelete,mBlock);
                            mHotelsRecyclerView.setAdapter(mHotelAdapter);
                            mHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mHotelsRecyclerView.setMotionEventSplittingEnabled(false);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(VehiclesForRentActivity.this, "error loading data.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    pd.dismiss();
                    Toast.makeText(VehiclesForRentActivity.this, "You haven't uploaded any rooms yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
            }
        });

    }

}
