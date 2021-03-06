package com.architectica.rental05.thevendorsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.architectica.rental05.thevendorsapp.FirstRunSecondActivity.userUid;

public class MainActivity extends AppCompatActivity {

    private ImageView mOyo;
    private FirebaseAnalytics mFirebaseAnalytics;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    static boolean isMainActivity;
    static String deviceTokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isMainActivity = true;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.d("MainActivity", "yes");

        mOyo = (ImageView) findViewById(R.id.oyo_main);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //  Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/anagram.ttf");
        //mOyo.setTypeface(custom_font);
        //mOyo.setLetterSpacing(-0.03f);

        //the app is not run for the first time
        //check if there is a saved auth state

        userUid = getSharedPreferences("User", MODE_PRIVATE)
                .getString("UserUid", null);

        FirstRunSecondActivity.vendorName = getSharedPreferences("VendorName", MODE_PRIVATE)
                .getString("Name", null);

        if (isConnected) {

            //internet connection is there

            FirebaseDatabase.getInstance().goOnline();

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

                if (userUid == null){

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

                }
                else {

                    final DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference("Vendors");

                    statusReference.keepSynced(true);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 3000ms

                            //there is a saved auth state
                            //check if admin deleted the landlord

                            //due to persistence,first the data in the cache is being used

                            statusReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //incase the admin deletes the user at a time when the user is currently logged in,then the user is instantly logged out

                                    DataSnapshot snapshot = dataSnapshot.child(userUid);

                                    Log.i("value","" + !snapshot.exists());

                                    if (snapshot.exists()) {

                                        if (isMainActivity){

                                            //Log.i("name",FirstRunSecondActivity.vendorName);
                                            //Log.i("number",FirstRunSecondActivity.fullNumber);

                                            if ("Verified".equals(snapshot.child("Status").getValue(String.class))) {

                                                //user status is verified

                                                final Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                            } else {

                                                //user status is not verified

                                                Log.i("status","user not verified");

                                                final Intent intent = new Intent(getApplicationContext(), UnVerifiedUser.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                            }


                                        }


                                    }
                                    else {

                                        //admin deleted the user

                                        Log.i("status","user deleted");

                                        getSharedPreferences("User", MODE_PRIVATE)
                                                .edit()
                                                .putString("UserUid", null)
                                                .apply();

                                        getSharedPreferences("VendorName", MODE_PRIVATE)
                                                .edit()
                                                .putString("Name", null)
                                                .apply();

                                        getSharedPreferences("PhoneNumber", MODE_PRIVATE)
                                                .edit()
                                                .putString("Number", null)
                                                .apply();

                                        FirebaseAuth.getInstance().signOut();

                                        Toast.makeText(MainActivity.this, "You were logged out.Please login again.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(MainActivity.this, FirstRunSecondActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }, 5000);

                }


            }
        } else {

            //internet connection is not there
            Toast.makeText(this, "No internet connection.Please check your network and try again", Toast.LENGTH_SHORT).show();
        }

    }
}

