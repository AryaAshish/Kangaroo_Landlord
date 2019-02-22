package com.architectica.rental05.thevendorsapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class FirstActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    GridView gridView;
    int images[] = {R.drawable.logo1,R.drawable.a,R.drawable.b,R.drawable.c};
    Typeface custom_font;
    Calendar c;
    int timeOfDay;
    private Toolbar toolbar;
    public static TextView mwelcomeMessageTxtView;
    String welcomeMessage;

    String[] iconNames = {"Upload Rooms","Bookings","My Services","Chats","Profile","LogOut"};
    int[] iconImages = {R.drawable.ic_location_city_black_24dp,R.drawable.bookingicon,R.drawable.servicesicon,R.drawable.chaticon,R.drawable.profile,R.drawable.logouticon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

        toolbar.inflateMenu(R.menu.menu_items);

        c = Calendar.getInstance();
        timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        mwelcomeMessageTxtView = (TextView)findViewById(R.id.welcome_msg) ;

        custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/anagram.ttf");

        if(timeOfDay >= 0 && timeOfDay < 12){
            welcomeMessage = "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            welcomeMessage = "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            welcomeMessage = "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            welcomeMessage = "Good Night";
        }

        mwelcomeMessageTxtView.setText(welcomeMessage+", "+ FirstRunSecondActivity.vendorName);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        for (int image : images){

            flipImages(image);

        }

        gridView = (GridView) findViewById(R.id.gridview);

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(),fruitNames[i],Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(getApplicationContext(),GridItemActivity.class);
                intent.putExtra("name",fruitNames[i]);
                intent.putExtra("image",fruitImages[i]);
                startActivity(intent);*/

               if (iconNames[i] == "Upload Rooms"){

                   uploadVehicle();
               }
               else if (iconNames[i] == "Bookings"){

                   bookedVehicles();
               }
               else if (iconNames[i] == "My Services"){

                   vehiclesForRent();
               }
               else if (iconNames[i] == "Chats") {

                   chatsButton();
               }
               else if (iconNames[i] == "Profile"){
                  showTodaysBookings();


               }
               else if (iconNames[i] == "LogOut"){

                   //logout the user
                   //remove the static values

                   getSharedPreferences("User",MODE_PRIVATE)
                           .edit()
                           .putString("UserUid",null)
                           .apply();

                   getSharedPreferences("VendorName",MODE_PRIVATE)
                           .edit()
                           .putString("Name",null)
                           .apply();

                   FirebaseAuth.getInstance().signOut();
                   Intent intent = new Intent(FirstActivity.this,FirstRunSecondActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);

               }

            }
        });

        final TextView callDealerSupport = (TextView)findViewById(R.id.callDealerSupport);

        callDealerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + callDealerSupport.getText().toString()));
                startActivity(intent);

            }
        });
        final TextView callDealerSupport1 = (TextView)findViewById(R.id.callDealerSupport1);

        callDealerSupport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + callDealerSupport.getText().toString()));
                startActivity(intent);

            }
        });

    }

    public void flipImages(int image){

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return iconImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.grid_icons,null);
            //getting view in row_data
            TextView name = (TextView) view1.findViewById(R.id.iconName);
            ImageView image = (ImageView) view1.findViewById(R.id.iconImage);

            name.setText(iconNames[i]);

            try {
                image.setImageResource(iconImages[i]);
            }
            catch (Exception e){

                Toast.makeText(FirstActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();

            }

            return view1;

        }
    }

    public void uploadVehicle(){

        //upload a vehicle
        Intent intent = new Intent(this,UploadVehicleActivity.class);
        startActivity(intent);

    }

    public void bookedVehicles(){

        //view booked vehicles of the current vendor
        Intent intent = new Intent(this,BookedVehiclesActivity.class);
        startActivity(intent);

    }

    public void vehiclesForRent(){

        //vehicles uploaded by the vendor till now including booked vehicles
        Intent intent = new Intent(this,VehiclesForRentActivity.class);
        startActivity(intent);

    }

    public void chatsButton(){

        //view chats
        Intent intent = new Intent(this,chatsList.class);
        startActivity(intent);

    }

    public void showTodaysBookings(){

        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_items,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.about){

            //about description
         //   Intent intent = new Intent(FirstActivity.this,AboutActivity.class);
           // startActivity(intent);

        }
        else if (item.getItemId() == R.id.profile){

            //view your profile
            Intent intent = new Intent(this,UserProfile.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
