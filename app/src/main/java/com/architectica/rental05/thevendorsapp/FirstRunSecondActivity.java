package com.architectica.rental05.thevendorsapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;


public class FirstRunSecondActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button mOneTapLogin;
    RecyclerView spinner_recyclerView;
    List<String> countryNames;
    List<String> code;
    static TypedArray imageArray;
    public static Dialog dialog;
    RecyclerView recyclerView;
    SpinnerRecyclerViewAdapter adapter;
    public static TextView mCountryCode;
    public EditText mPhoneNumber;
    public static ImageView mFlagImage;
    LinearLayout mUserInput;
    RelativeLayout mLayout;
    public String PhoneNumber;
    private FirebaseAuth fbAuth;
    String message;
    String fullNumber;
    String registeredNumber;
    boolean isUserRegistered,isUserCustomer;
    String isUserVerified;
    protected String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    public static final String EXTRA_MESSAGE =
            "com.aryan.android.oyo.extra.MESSAGE";
    public static String vendorName;
    int ref;
    DatabaseReference reference;
    ProgressDialog pd;
    static String userUid;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            //back arrow pressed

            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(R.anim.activit_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run_second);

        MainActivity.isMainActivity = false;

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mOneTapLogin = (Button) findViewById(R.id.btn_one_tap_login);
        mCountryCode = (TextView) findViewById(R.id.country_code);
        mFlagImage = (ImageView) findViewById(R.id.flag_img);
        mUserInput = (LinearLayout)findViewById(R.id.user_input_in_frsa) ;
        mPhoneNumber = (EditText) findViewById(R.id.editText_phone) ;
        mLayout = (RelativeLayout) findViewById(R.id.first_run_second_activity) ;
        registeredNumber = null;
        isUserVerified = "false";
        fbAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Vendors/");

        countryNames = Arrays.asList(getResources().getStringArray(R.array.countries_list));
        code = Arrays.asList(getResources().getStringArray(R.array.countries_code_list));
        imageArray= getResources().obtainTypedArray(R.array.random_imgs);

        //setting the country names recycler view
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_recycler_view);
        dialog.setCanceledOnTouchOutside(true);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_spinner);
        adapter = new SpinnerRecyclerViewAdapter(this, countryNames, code);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void getDialog(View view) {
        dialog.show();
    }

    public void changeBackground(View view) {
        mUserInput.setBackgroundResource(R.drawable.linear_activity_in_activity_first_run_second_background_on_focus);
    }

    public void verify(View view) {

        //verify button is clicked

        InputMethodManager mImMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(mPhoneNumber.getWindowToken(), 0);

        PhoneNumber = mPhoneNumber.getText().toString();

        message = mCountryCode.getText() + " " + mPhoneNumber.getText();
        fullNumber = mCountryCode.getText().toString() + mPhoneNumber.getText().toString();

        int i=0;


        if(PhoneNumber.length() == 0){

            //length of phone number is 0

            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter phone number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else if(PhoneNumber.length() != 10) {

            //length of phone number is not equal to 10

            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter valid Phone Number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else {

            //signup the user
            pd = new ProgressDialog(FirstRunSecondActivity.this);
            pd.setMessage("Please wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            //register the user

            SignIn();
        }

    }
    public void SignIn() {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //check if user is registered

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        isUserRegistered = false;
                        isUserVerified = "false";

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String phoneNum = ds.child("PhoneNumber").getValue(String.class);

                            if (fullNumber.equals(phoneNum)) {

                                //user is registered

                                isUserRegistered = true;

                                if ("Verified".equals(ds.child("Status").getValue(String.class))){

                                    //user is registered and verified

                                    isUserVerified = "true";
                                    vendorName = ds.child("Name").getValue(String.class);
                                    break;

                                }
                                else {

                                    //user is registered but not verified

                                    isUserVerified = "false";
                                    break;
                                }

                            }
                            else {

                                //user is not registered

                                isUserRegistered = false;

                            }

                        }

                        if(isUserRegistered){

                            //ask the user to login instead of registering as a new user

                            pd.dismiss();

                            Toast.makeText(getApplicationContext(), "user is already registered", Toast.LENGTH_SHORT).show();

                        } else {

                            //register the new user

                            pd.dismiss();

                            Intent intent = new Intent(getApplicationContext(), FirstRunThirdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(EXTRA_MESSAGE, message);
                            intent.putExtra("number",PhoneNumber);
                            intent.putExtra("countryCodeMobNumber",fullNumber);
                            intent.putExtra("verificationIdSent",phoneVerificationId);
                            intent.putExtra("reSendToken",resendingToken);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(FirstRunSecondActivity.this, "database error", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        },5000);

    }

    public void loginButton(View v){

        //login button is clicked

        InputMethodManager mImMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(mPhoneNumber.getWindowToken(), 0);

        PhoneNumber = mPhoneNumber.getText().toString();

        message = mCountryCode.getText() + " " + mPhoneNumber.getText();
        fullNumber = mCountryCode.getText().toString() + mPhoneNumber.getText().toString();
        int i=0;

        if(PhoneNumber.length() == 0){

            //length of phone number is 0

            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter phone number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else if(PhoneNumber.length() != 10) {

            //length of phone number is not 10

            mUserInput.setBackground(getResources().getDrawable(R.drawable.error_phone_number));
            Snackbar snackbar= Snackbar.make(mLayout,"Enter valid Phone Number",Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.errorSnackBarColor));
            TextView mSnackBarTextView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            mSnackBarTextView.setTextAlignment(snackBarView.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
        else{

            //login the user
            pd = new ProgressDialog(FirstRunSecondActivity.this);
            pd.setMessage("Logging in...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            LogIn();

        }

    }

    public void LogIn() {

        //check if the user is a new user

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        isUserRegistered = false;
                        isUserVerified = "false";

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String phoneNum = ds.child("PhoneNumber").getValue(String.class);

                            if (fullNumber.equals(phoneNum)) {

                                //user is registered

                                isUserRegistered = true;
                                vendorName = ds.child("Name").getValue(String.class);

                                if ("Verified".equals(ds.child("Status").getValue(String.class))){

                                    //user is registered and verified

                                    isUserVerified = "true";
                                    break;

                                }
                                else {

                                    //user is registered but not verified

                                    isUserVerified = "false";
                                    break;
                                }

                            }
                            else {

                                //user is not registered

                                isUserRegistered = false;

                            }

                        }


                        if (isUserRegistered) {

                            //user is registered
                            //get users uid

                            getUserUid();

                        }
                        else {

                            //ask the user to register

                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Landlord not registered", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(FirstRunSecondActivity.this, "database error", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        },5000);

    }

    public void goToFirstActivity(){

        //go to first activity after logging successfully
        Intent intent = new Intent(FirstRunSecondActivity.this, FirstActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    public void getUserUid(){

        //get the users uid

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Vendors");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userUid = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (fullNumber.equals(snapshot.child("PhoneNumber").getValue(String.class))){

                        userUid = snapshot.getKey();

                    }

                }

                pd.dismiss();

                if (userUid != null){

                    //save the auth state

                    getSharedPreferences("User",MODE_PRIVATE)
                            .edit()
                            .putString("UserUid",userUid)
                            .apply();

                    getSharedPreferences("VendorName",MODE_PRIVATE)
                            .edit()
                            .putString("Name",vendorName)
                            .apply();

                   // MainActivity.deviceTokenId = FirebaseInstanceId.getInstance().getToken();

                    //FirebaseDatabase.getInstance().getReference("Vendors/" + userUid).child("TokenId").setValue(MainActivity.deviceTokenId);

                }

                if (isUserVerified.equals("true")){

                    //user is verified,go to the starting activity

                    goToFirstActivity();

                }
                else {

                    //user is not verified,show the unverified users message

                    Intent i = new Intent(FirstRunSecondActivity.this, UnVerifiedUser.class);
                    startActivity(i);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
