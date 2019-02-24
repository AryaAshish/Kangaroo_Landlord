package com.architectica.rental05.thevendorsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.app.AlertDialog.THEME_HOLO_DARK;

/**
 * Created by admin1 on 10/3/18.
 */

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelItemViewHolder> {


    private List<Integer> mDelete;
    private List<Integer> mBlock;
   // List<String> mRoomIds;
    List<String> mHotelNames;
    List<String> mHotelLocations;
    List<String> mOriginalRates;
    List<String> mReducedCosts;
    List<String> mHotelImages;
    List<String> VendorNames;
    //List<String> noOfVehicles;
    List<String> orderId;
    List<String> txnAmount;
    List<String> bankTxnId;
    List<String> txnId;
    List<String> bankName;
    private LayoutInflater mInflater;
    private Context context;
    public int row_index;
    List<String> mCity;
    List<String> vehicleType;
    private int width;
    String NoOfVehicles;
    static String content;
    static String bookingUid;
    static String name,type,address,city,image;

    public static final String EXTRA_IMAGE_ID = "IMAGE_ID";
    public static final String EXTRA_ROOM_ID = "ROOM_ID";
    public static final String EXTRA_ORIGINAL_COST = "ORIGINAL_COST";
    public static final String EXTRA_REDUCED_COST = "REDUCED_COST";
    public static final String EXTRA_HOTEL_NAME = "HOTEL_NAME";
    public static final String EXTRA_HOTEL_LOCATION = "HOTEL_LOCATION";
    public static final String EXTRA_CITY = "CITY";
    public static final String EXTRA_VEHICLE = "VEHICLE";
    public static final String EXTRA_VENDOR = "VENDOR";
    public static final String EXTRA_NOOFVEHICLES = "0";
    public static final String EXTRA_ORDERID = "ORDERID";
    public static final String EXTRA_TXNAMOUNT = "TXNAMOUNT";
    public static final String EXTRA_BANKTXNID = "BANKTXNID";
    public static final String EXTRA_TXNID = "TXNID";
    public static final String EXTRA_BANKNAME = "BANKNAME";

    public class HotelItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView hotelImageView;
        public final ImageView favouriteSymbol;
        public final ImageView blockSymbol;
        final HotelAdapter mAdapter;
        public final TextView mHotelName;
        public final TextView mHotelCity;
        public final TextView mOriginalCost;
        public final TextView mReducedCost;
        public final TextView mHotelLocation;
        public final TextView mRoomStatus;
        public final LinearLayout mHotelDetailsLayout;

        public HotelItemViewHolder(View itemView,HotelAdapter adapter) {
            super(itemView);
            hotelImageView = (ImageView) itemView.findViewById(R.id.vehicleImage);
            favouriteSymbol = (ImageButton) itemView.findViewById(R.id.favourite_symbol1);
            mHotelName = (TextView) itemView.findViewById(R.id.vehicleName) ;
            mAdapter = adapter;
            mHotelCity = (TextView) itemView.findViewById(R.id.city) ;
            mOriginalCost = (TextView) itemView.findViewById(R.id.pricePerHour);
            mReducedCost = (TextView) itemView.findViewById(R.id.pricePerDay);
            mHotelLocation = (TextView) itemView.findViewById(R.id.parkingAddress);
            mHotelDetailsLayout = (LinearLayout) itemView.findViewById(R.id.hotel_details);
            blockSymbol = (ImageView)itemView.findViewById(R.id.block_symbol1);
            mRoomStatus = (TextView)itemView.findViewById(R.id.roomStatus);

        }
    }

    public HotelAdapter(Context context,List<String> mHotelImages,List<String> VendorNames,List<String> mCity,List<String> vehicleType,List<String> mHotelNames,
                        List<String> mOriginalRates,  List<String> mReducedCosts,List<String> mHotelLocations,List<String> orderId,List<String> txnAmount,List<String> bankTxnId,
                        List<String> txnId,List<String> bankName,List<Integer> mDelete,List<Integer> mBlock) {
        mInflater = LayoutInflater.from(context);
        this.mHotelImages = mHotelImages;
        this.VendorNames = VendorNames;
        this.context = context;
        this.mCity = mCity;
        this.vehicleType = vehicleType;
        this.mHotelNames = mHotelNames;
        this.mOriginalRates = mOriginalRates;
        this.mReducedCosts = mReducedCosts;
        this.mHotelLocations = mHotelLocations;
        this.width = width;
        this.orderId = orderId;
        this.txnAmount = txnAmount;
        this.bankTxnId = bankTxnId;
        this.txnId = txnId;
        this.bankName = bankName;
        this.mDelete = mDelete;
        this.mBlock = mBlock;

    }
    @Override
    public HotelAdapter.HotelItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.vehicles_list_item, parent, false);
        return new HotelAdapter.HotelItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final HotelAdapter.HotelItemViewHolder holder, final int position) {

        final String mCurentHotelImage = mHotelImages.get(position);
        final String mCurrentHotelName = mHotelNames.get(position) ;
        final String mCurrentOriginalCost = mOriginalRates.get(position);
        final String mCurrentReducedCost = mReducedCosts.get(position);
        final String mCurrentHotelLocation = mHotelLocations.get(position);
        final String mCurrentCity = mCity.get(position);
        final String mCurrentVehType = vehicleType.get(position);
        final String mCurrentOrderId = orderId.get(position);
        final String mCurrentTxnAmount = txnAmount.get(position);
        final String mCurrentBankTxnId = bankTxnId.get(position);
        final String mCurrentTxnId = txnId.get(position);
        final String mCurrentBankName = bankName.get(position);
        final int mCurrentDelete = mDelete.get(position);
        final int mCurrentBlock = mBlock.get(position);

        Picasso.get().load(mCurentHotelImage).into(holder.hotelImageView);
        holder.mHotelCity.setText(mCurrentCity);
        holder.mHotelName.setText(mCurrentHotelName);
        holder.mOriginalCost.setText(mCurrentOriginalCost);
        holder.mReducedCost.setText(mCurrentReducedCost);
        holder.mHotelLocation.setText(mCurrentHotelLocation);

        //set the option to delete vehicles only for unbooked vehicles.

        DatabaseReference vehicleBooking = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");
        vehicleBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check if the current vehicle is booked or not

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if (mCurrentHotelName.equals(dataSnapshot1.child("VehicleName").getValue(String.class)) && "true".equals(dataSnapshot1.child("isVehicleBooked").getValue(String.class)) && mCurrentVehType.equals(dataSnapshot1.child("VehicleType").getValue(String.class)) && mCurrentCity.equals(dataSnapshot1.child("City").getValue(String.class)) && mCurrentHotelLocation.equals(dataSnapshot1.child("ParkingAddress").getValue(String.class))){

                        //current vehicle is booked
                        //disable the add to favourites button
                        holder.favouriteSymbol.setVisibility(View.GONE);
                        holder.blockSymbol.setVisibility(View.GONE);
                        holder.mRoomStatus.setVisibility(View.GONE);

                    }
                    else if(mCurrentHotelName.equals(dataSnapshot1.child("VehicleName").getValue(String.class)) && "false".equals(dataSnapshot1.child("isVehicleBooked").getValue(String.class)) && mCurrentVehType.equals(dataSnapshot1.child("VehicleType").getValue(String.class)) && mCurrentCity.equals(dataSnapshot1.child("City").getValue(String.class)) && mCurrentHotelLocation.equals(dataSnapshot1.child("ParkingAddress").getValue(String.class))){

                        //current vehicle is not booked
                        //give the option to delete the vehicle

                        holder.favouriteSymbol.setVisibility(View.GONE);
                        holder.favouriteSymbol.setImageResource(mCurrentDelete);

                        holder.blockSymbol.setVisibility(View.VISIBLE);
                        //holder.blockSymbol.setImageResource(mCurrentBlock);
                        holder.blockSymbol.setBackgroundResource(mCurrentBlock);

                        holder.mRoomStatus.setVisibility(View.VISIBLE);
                        holder.mRoomStatus.setText(VehiclesForRentActivity.roomStatuses.get(position));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.favouriteSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //delete button is clicked
                //show an alert dilog and confirm the deletion

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                builder1.setMessage("Are you sure you want to delete this uploaded room?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //clicked yes
                                //delete the vehicle

                                //delete from the vendors uploaded vehicles
                                DatabaseReference deleteVehicleReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

                                deleteVehicleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                DatabaseReference deleteRef = snapshot.getRef();
                                                deleteRef.removeValue();

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //delete from the vehicles for rent in that city

                                DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String noOfVehicles = dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class);

                                        if (noOfVehicles.equals("0")) {

                                            //currently no matching vehicles.the admin might deleted the vehicle

                                        } else {

                                            //vehicle present
                                            //delete the vehicle

                                            //update the no of vehicles available

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (snapshot1.child("Address").getValue(String.class).equals(mCurrentHotelLocation) && snapshot1.child("VendorName").getValue(String.class).equals(FirstRunSecondActivity.vendorName)) {

                                                    NoOfVehicles = snapshot1.child("NoOfVehicles").getValue(String.class);

                                                }

                                            }

                                            String updatedNoOfVehicles = Integer.toString(Integer.parseInt(noOfVehicles) - Integer.parseInt(NoOfVehicles));

                                            DatabaseReference referenceToUpdate = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);


                                            if (updatedNoOfVehicles.equals("0")) {

                                                //delete the vehicle
                                                referenceToUpdate.removeValue();

                                            } else {

                                                //remove the current vendor from the vendors offering the current vehicle

                                                referenceToUpdate.child("NoOfVehiclesAvailable").setValue(updatedNoOfVehicles);

                                                referenceToUpdate.child("ParkingAddress").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                            if (snapshot.child("Address").getValue(String.class).equals(mCurrentHotelLocation) && snapshot.child("VendorName").getValue(String.class).equals(FirstRunSecondActivity.vendorName)) {
                                                                snapshot.getRef().removeValue();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //delete the location of the selected vehicle to delete
                                UploadVehicleActivity.locationLongitude = null;
                                UploadVehicleActivity.locationLatitude = null;

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //cancelled deleting the vehicle

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }});

        holder.blockSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBlock.get(position) == R.drawable.ic_block_black_24dp) {

                    //block the vehicle

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                    builder1.setMessage("Are you sure you want to block this room?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DatabaseReference blockReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

                                    blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                    snapshot.child("isVehicleBlocked").getRef().setValue("true");

                                                }

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                    deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && FirstRunSecondActivity.vendorName.equals(snapshot1.child("VendorName").getValue(String.class))) {

                                                    int noOfVeh = Integer.parseInt(snapshot1.child("NoOfVehicles").getValue(String.class));
                                                    int noOfVehAvailable = Integer.parseInt(dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class));

                                                    int update = noOfVehAvailable - noOfVeh;

                                                    if (update>=0){

                                                        dataSnapshot.child("NoOfVehiclesAvailable").getRef().setValue(Integer.toString(update));

                                                    }

                                                    snapshot1.child("isVehicleBlocked").getRef().setValue("true");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    Toast.makeText(context, "Vehicle Blocked", Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //cancelled deleting the vehicle

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else if(mBlock.get(position) == R.drawable.ic_lock_open_black_24dp){


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                    builder1.setMessage("Are you sure you want to unblock this room?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DatabaseReference blockReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

                                    blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                    snapshot.child("isVehicleBlocked").getRef().setValue("false");

                                                }

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                    deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && FirstRunSecondActivity.vendorName.equals(snapshot1.child("VendorName").getValue(String.class))) {

                                                    int noOfVeh = Integer.parseInt(snapshot1.child("NoOfVehicles").getValue(String.class));
                                                    int noOfVehAvailable = Integer.parseInt(dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class));

                                                    int update = noOfVehAvailable + noOfVeh;

                                                    if (update>=0){

                                                        dataSnapshot.child("NoOfVehiclesAvailable").getRef().setValue(Integer.toString(update));

                                                    }

                                                    snapshot1.child("isVehicleBlocked").getRef().setValue("false");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    Toast.makeText(context, "Vehicle Unblocked", Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //cancelled deleting the vehicle

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else if (mBlock.get(position) == R.drawable.ic_lock_black_24dp){

                    Toast.makeText(context, "This room is blocked by the admin.please contact the admin", Toast.LENGTH_SHORT).show();

                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mCurrentHotelName;
                type = mCurrentVehType;
                address = mCurrentHotelLocation;
                city = mCurrentCity;
                image = mCurentHotelImage;

                if (content == "BookedVehicles"){

                    DatabaseReference bookings = FirebaseDatabase.getInstance().getReference("Bookings");

                    bookings.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && FirstRunSecondActivity.userUid.equals(snapshot.child("VendorUid").getValue(String.class))){

                                    bookingUid = snapshot.getKey();

                                }


                            }

                            Intent intent = new Intent(context,HotelActivity_1.class);
                            context.startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    Intent intent = new Intent(context,HotelActivity_1.class);
                    context.startActivity(intent);

                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return mHotelImages.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}

