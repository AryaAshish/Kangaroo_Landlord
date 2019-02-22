package com.architectica.rental05.thevendorsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin1 on 10/3/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageItemViewHolder> {


    List<String> mHotelImages;
    /*private List<Integer> mFavouriteImages;
    List<String> mRoomIds;
    List<String> mHotelNames;
    List<String> mHotelLocations;
    List<String> mOriginalRates;
    List<String> mReducedCosts;

    List<String> VendorNames;
    List<String> noOfVehicles;
    List<String> orderId;
    List<String> txnAmount;
    List<String> bankTxnId;
    List<String> txnId;
     List<String> mCity;
    List<String> vehicleType;
    List<String> bankName;*/
    private LayoutInflater mInflater;
    private Context context;
    public int row_index;
    private int width;


    public class ImageItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        final ImageAdapter mAdapter;
        public final LinearLayout mDetailsLayout;

        /*public final ImageView favouriteSymbol;

        public final TextView mHotelName;
        public final TextView mHotelCity;
        public final TextView mRoomId;
        public final TextView mOriginalCost;
        public final TextView mReducedCost;
        //public final TextView mDiscount;
        //public final TextView mHotelRating;
        //public final TextView mDescription;
        //public final TextView mRating;
        public final TextView mHotelLocation;
        public final TextView noOfVehicles;
        public final TextView noOfVehiclesAvailableTextView;*/


        public ImageItemViewHolder(View itemView,ImageAdapter adapter) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mAdapter = adapter;
            mDetailsLayout = (LinearLayout) itemView.findViewById(R.id.imageView_details);
           /* favouriteSymbol = (ImageButton) itemView.findViewById(R.id.favourite_symbol1);
            mHotelName = (TextView) itemView.findViewById(R.id.vehicleName) ;

            mHotelCity = (TextView) itemView.findViewById(R.id.city) ;
            mRoomId = (TextView) itemView.findViewById(R.id.vehicleNumber);
            mOriginalCost = (TextView) itemView.findViewById(R.id.pricePerHour);
            mReducedCost = (TextView) itemView.findViewById(R.id.pricePerDay);
            //mDiscount = (TextView) itemView.findViewById(R.id.discount);
            //mHotelRating = (TextView) itemView.findViewById(R.id.hotel_rating);
            //mDescription = (TextView) itemView.findViewById(R.id.hotel_description);
            //mRating = (TextView) itemView.findViewById(R.id.rating_count);
            mHotelLocation = (TextView) itemView.findViewById(R.id.parkingAddress);
            noOfVehicles = (TextView)itemView.findViewById(R.id.noOfVehiclesAvailable);
            noOfVehiclesAvailableTextView = (TextView)itemView.findViewById(R.id.noOfVehiclesAvailableTextView);*/
        }
    }

    public ImageAdapter(Context context,List<String> mHotelImages) {
        mInflater = LayoutInflater.from(context);
        this.mHotelImages = mHotelImages;
        this.context = context;
        /*this.VendorNames = VendorNames;

        this.mFavouriteImages = mFavouriteImages;
        this.mCity = mCity;
        this.vehicleType = vehicleType;
        this.mRoomIds = mRommIds;
        this.mHotelNames = mHotelNames;
        this.mOriginalRates = mOriginalRates;
        this.mReducedCosts = mReducedCosts;
        //this.mDiscounts = mDiscounts;
        //this.mHotelRatings = mHotelRatings;
        //this.mDescriptions = mDescriptions;
        //this.mRatings = mRatings;
        this.mHotelLocations = mHotelLocations;
        this.width = width;
        this.noOfVehicles = noOfVehicles;
        this.orderId = orderId;
        this.txnAmount = txnAmount;
        this.bankTxnId = bankTxnId;
        this.txnId = txnId;
        this.bankName = bankName;*/
    }
    @Override
    public ImageAdapter.ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_image_view, parent, false);
        return new ImageAdapter.ImageItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ImageItemViewHolder holder, final int position) {

        //final int mCurentHotelImage = mHotelImages.getResourceId(position,-1);
        // Log.d("Image id",mCurentHotelImage+"");
        final String mCurentHotelImage = mHotelImages.get(position);
       /* final String vendorName = VendorNames.get(position);
        final int mCurrentFavouriteImage = mFavouriteImages.get(position);
        final String mCurrentRoomId = mRoomIds.get(position);
        final String mCurrentHotelName = mHotelNames.get(position) ;
        final String mCurrentOriginalCost = mOriginalRates.get(position);
        final String mCurrentReducedCost = mReducedCosts.get(position);
        //  final String mCurrentDiscount = "" + mDiscounts.get(position) + "% OFF";
        //final String mCurrentHotelRating = "" + mHotelRatings.get(position);
        //final String mCurrentDescription = "" + mDescriptions.get(position);
        //final String mCurrentRating = "" + mRatings.get(position);
        final String mCurrentHotelLocation = mHotelLocations.get(position);
        final String mCurrentNoOfVehicles = noOfVehicles.get(position);
        final String mCurrentCity = mCity.get(position);
        final String mCurrentVehType = vehicleType.get(position);
        final String mCurrentOrderId = orderId.get(position);
        final String mCurrentTxnAmount = txnAmount.get(position);
        final String mCurrentBankTxnId = bankTxnId.get(position);
        final String mCurrentTxnId = txnId.get(position);
        final String mCurrentBankName = bankName.get(position);*/

        //ViewGroup.LayoutParams params=holder.mHotelDetailsLayout.getLayoutParams();

        // holder.hotelImageView.setImageResource(mCurentHotelImage);
        Picasso.get().load(mCurentHotelImage).into(holder.imageView);
       /* holder.mHotelCity.setText(mCurrentCity);
        holder.mRoomId.setText(mCurrentRoomId);
        holder.mHotelName.setText(mCurrentHotelName);
        holder.mOriginalCost.setText(mCurrentOriginalCost);
        holder.mReducedCost.setText(mCurrentReducedCost);
        holder.noOfVehicles.setText(mCurrentNoOfVehicles);
        // holder.mDiscount.setText(mCurrentDiscount);
        //holder.mHotelRating.setText(mCurrentHotelRating);
        //holder.mDescription.setText(mCurrentDescription);
        //holder.mRating.setText(mCurrentRating);
        holder.mHotelLocation.setText(mCurrentHotelLocation);
        holder.favouriteSymbol.setImageResource(mCurrentFavouriteImage);
        if (mCurrentOrderId != null){
            holder.noOfVehiclesAvailableTextView.setVisibility(View.INVISIBLE);
        }*/

        /*DatabaseReference favouritesReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/Favourites");

        favouritesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String isAddedToFavourites = "false";

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if (dataSnapshot1.child("VehicleName").getValue(String.class).equals(mCurrentHotelName)){

                        isAddedToFavourites = "true";
                        break;

                    }
                    else {
                        isAddedToFavourites = "false";
                    }

                }

                if (isAddedToFavourites.equals("true")){

                    holder.favouriteSymbol.setImageResource(R.drawable.ic_favorite_red_24dp);
                    //mFavouriteImages.set(position, R.drawable.ic_favorite_red_24dp);

                }
                else if (isAddedToFavourites.equals("false")){

                    holder.favouriteSymbol.setImageResource(R.drawable.ic_favorite_white_24dp);
                    //mFavouriteImages.set(position, R.drawable.ic_favorite_white_24dp) ;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        /*if(width == 0) {
            holder.mHotelDetailsLayout.setPadding(0,0,0,0);
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        } else {
            holder.mHotelDetailsLayout.setPadding(10,0,10,0);
            params.width = width;
        }
        holder.mHotelDetailsLayout.setLayoutParams(params);*/

    }

    @Override
    public int getItemCount() {
        return mHotelImages.size();
    }
    /*public void setOnClick(OnItemClicked onClick)
     {
         this.onClick=onClick;
     }*/
    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}

