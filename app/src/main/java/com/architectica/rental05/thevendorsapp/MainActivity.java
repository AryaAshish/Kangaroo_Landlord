package com.architectica.rental05.thevendorsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import static com.architectica.rental05.thevendorsapp.FirstRunSecondActivity.userUid;

public class MainActivity extends AppCompatActivity {

    private ImageView mOyo;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","yes");

        mOyo = (ImageView) findViewById(R.id.oyo_main) ;
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //  Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/anagram.ttf");
        //mOyo.setTypeface(custom_font);
        //mOyo.setLetterSpacing(-0.03f);

        if (isConnected) {

            //internet connection is there

            if (isFirstRun) {

                //the app is run for the first time
                //go to login or signup page

                Intent intent = new Intent(MainActivity.this, FirstRunSecondActivity.class);
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("isFirstRun", false)
                                        .apply();

                                startActivity(new Intent(MainActivity.this, FirstRunSecondActivity.class));
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        });
                    }
                }, 3000);

            } else {

                //the app is not run for the first time
                //check if there is a saved auth state

                DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference();

                statusReference.child("Vendors").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //incase the admin deletes the user at a time when the user is currently logged in,then the user is instantly logged out

                        if (userUid != null){

                            if (!dataSnapshot.hasChild(userUid)){

                                getSharedPreferences("User",MODE_PRIVATE)
                                        .edit()
                                        .putString("UserUid",null)
                                        .apply();

                                getSharedPreferences("VendorName",MODE_PRIVATE)
                                        .edit()
                                        .putString("Name",null)
                                        .apply();

                                FirebaseAuth.getInstance().signOut();

                                Toast.makeText(MainActivity.this, "You were logged out.Please login again", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this,FirstRunSecondActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String isUserUid = getSharedPreferences("User",MODE_PRIVATE)
                        .getString("UserUid",null);

                String VendorName = getSharedPreferences("VendorName",MODE_PRIVATE)
                        .getString("Name",null);

                FirstRunSecondActivity.vendorName = VendorName;

                userUid = isUserUid;

                if (isUserUid == null) {

                    //no saved auth state
                    //show start activity

                    Intent intent = new Intent(MainActivity.this, FirstRunSecondActivity.class);
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    startActivity(new Intent(MainActivity.this, FirstRunSecondActivity.class));
                                    overridePendingTransition(0, 0);
                                    finish();
                                }
                            });
                        }
                    }, 3000);

                } else {

                    //there is a saved auth state

                    //check if the saved auth state is currently active or deleted by the admin

                    statusReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child("Vendors").hasChild(userUid)){

                                //user not deleted by admin

                                if ("Verified".equals(dataSnapshot.child("Vendors").child(userUid).child("Status").getValue(String.class))) {

                                    //user status is verified

                                    final Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                                    new Timer().schedule(new TimerTask() {
                                        public void run() {
                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    }, 3000);

                                } else {

                                    //user status is not verified

                                    final Intent intent = new Intent(getApplicationContext(), UnVerifiedUser.class);
                                    new Timer().schedule(new TimerTask() {
                                        public void run() {
                                            MainActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    }, 3000);

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
        else {

            //internet connection is not there
            Toast.makeText(this, "No internet connection.Please check your network and try again", Toast.LENGTH_SHORT).show();
        }
    }
}

