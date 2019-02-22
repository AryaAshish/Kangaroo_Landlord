package com.architectica.rental05.thevendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HotelActivity_1 extends AppCompatActivity {

    TextView VendorName,VehicleName,ParkingAddress,NoOfVehiclesBooked,VehicleCity,OrderId,TxnAmount,BankTxnId,TxnId,BankName,BookingHours,BookingUserName,PickUp,Delivery;
    ImageView VehicleImage;
    TextView bookingDate,balanceAmount,totalAmount;
    RelativeLayout bookingDetailsLayout;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_1);

        VendorName = (TextView)findViewById(R.id.vendorName);
        VehicleName = (TextView)findViewById(R.id.vehiclesName);
        ParkingAddress = (TextView)findViewById(R.id.vendorsParkingAddress);
        NoOfVehiclesBooked = (TextView)findViewById(R.id.NoOfVehiclesWithThisVendor);
        VehicleCity = (TextView)findViewById(R.id.vehicleCity);
        OrderId = (TextView)findViewById(R.id.orderId);
        TxnAmount = (TextView)findViewById(R.id.transactionAmount);
        BankTxnId = (TextView)findViewById(R.id.bankTxnId);
        TxnId = (TextView)findViewById(R.id.transactionId);
        BankName = (TextView)findViewById(R.id.bankName);
        BookingHours = (TextView)findViewById(R.id.bookingHours);
        BookingUserName = (TextView)findViewById(R.id.userName);
        PickUp = (TextView)findViewById(R.id.PickUp);
        Delivery = (TextView)findViewById(R.id.Delivery);
        VehicleImage = (ImageView)findViewById(R.id.vehiclesImage);
        bookingDetailsLayout = (RelativeLayout)findViewById(R.id.bookingDetailsLayout);
        balanceAmount = (TextView)findViewById(R.id.balanceAmount);
        totalAmount = (TextView)findViewById(R.id.totalAmount);
        bookingDate = (TextView)findViewById(R.id.bookingDate);
        pd = new ProgressDialog(HotelActivity_1.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        if (HotelAdapter.content == "BookedVehicles"){

            DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("Bookings/" + HotelAdapter.bookingUid);

            bookingReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    bookingDetailsLayout.setVisibility(View.VISIBLE);

                    Picasso.get().load(dataSnapshot.child("VehiclePhoto").getValue(String.class)).into(VehicleImage);
                    VendorName.setText(dataSnapshot.child("Vendor").getValue(String.class));
                    VehicleName.setText(dataSnapshot.child("VehicleName").getValue(String.class));
                    ParkingAddress.setText(dataSnapshot.child("ParkingAddress").getValue(String.class));
                    NoOfVehiclesBooked.setText(dataSnapshot.child("BookedNoOfVehicles").getValue(String.class));
                    VehicleCity.setText(dataSnapshot.child("City").getValue(String.class));
                    OrderId.setText(dataSnapshot.child("OrderId").getValue(String.class));
                    TxnAmount.setText(dataSnapshot.child("TxnAmount").getValue(String.class));
                    BankTxnId.setText(dataSnapshot.child("BankTxnId").getValue(String.class));
                    TxnId.setText(dataSnapshot.child("TxnId").getValue(String.class));
                    BankName.setText(dataSnapshot.child("BankName").getValue(String.class));
                    BookingHours.setText(dataSnapshot.child("BookedInterval").getValue(String.class) + " Hours");
                    BookingUserName.setText(dataSnapshot.child("UserName").getValue(String.class));
                    PickUp.setText(dataSnapshot.child("PickUp").getValue(String.class));
                    Delivery.setText(dataSnapshot.child("Delivery").getValue(String.class));
                    bookingDate.setText(dataSnapshot.child("BookingDate").getValue(String.class));
                    totalAmount.setText(dataSnapshot.child("TotalAmount").getValue(String.class));
                    balanceAmount.setText(dataSnapshot.child("BalanceAmount").getValue(String.class));

                    pd.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                }
            });

        }
        else {

            bookingDetailsLayout.setVisibility(View.GONE);

            Picasso.get().load(HotelAdapter.image).into(VehicleImage);
            VendorName.setText(FirstRunSecondActivity.vendorName);
            VehicleName.setText(HotelAdapter.name);
            ParkingAddress.setText(HotelAdapter.address);
            VehicleCity.setText(HotelAdapter.city);

            DatabaseReference detailReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

            detailReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if (HotelAdapter.city.equals(snapshot.child("City").getValue(String.class)) && HotelAdapter.type.equals(snapshot.child("VehicleType").getValue(String.class)) && HotelAdapter.name.equals(snapshot.child("VehicleName").getValue(String.class)) && HotelAdapter.address.equals(snapshot.child("ParkingAddress").getValue(String.class))){

                            Log.i("status","entered");
                            NoOfVehiclesBooked.setText(snapshot.child("NoOfVehicles").getValue(String.class));
                            pd.dismiss();

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void displayImages(View view){

        Intent intent1 = new Intent(HotelActivity_1.this,DisplayImages.class);
        startActivity(intent1);

    }

}
