package com.architectica.rental05.thevendorsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final int PICK_IMAGE_REQUEST = 71;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri filePath;
    ImageView vehicleImage;
    FirebaseStorage storage;
    StorageReference storageReference,storageRef;
    EditText vehicleName,parkingAddress,cityName,pricePerHour,pricePerDay,noOfVehiclesAvailable,id;
    DatabaseReference uploadedVehicles;
    String vehType,vehName,city;
    Bitmap bitmap;
    int noOfImages;
    int x=0,z=0;
    String roomType[];
    Uri[] uris;
    static String locationLatitude;
    static String locationLongitude;
    int k;
    int NoOfVehicles = 0;
    List<EditText> etList;
    int noOfVehicleNumbers;
    LinearLayout vehNoLayout;
    CheckBox[] cb =new CheckBox[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle);

        vehicleImage = (ImageView)findViewById(R.id.vehicleImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Vendors/" + FirstRunSecondActivity.vendorName);
        vehicleName = (EditText)findViewById(R.id.vehicleName);
        //vehicleNumber = (EditText)findViewById(R.id.vehicleNumber);
        parkingAddress = (EditText)findViewById(R.id.parkingAddress);
        cityName = (EditText)findViewById(R.id.cityName);
        pricePerHour = (EditText)findViewById(R.id.pricePerHour);
        pricePerDay = (EditText)findViewById(R.id.pricePerDay);
        noOfVehiclesAvailable = (EditText)findViewById(R.id.noOfVehicles);
        id = (EditText)findViewById(R.id.id);

        storageRef = storage.getReference();
        cb[0]=(CheckBox)findViewById(R.id.cb1);
        cb[1]=(CheckBox)findViewById(R.id.cb2);
        cb[2]=(CheckBox)findViewById(R.id.cb3);
        cb[3]=(CheckBox)findViewById(R.id.cb4);
        cb[4]=(CheckBox)findViewById(R.id.cb5);
        cb[5]=(CheckBox)findViewById(R.id.cb6);


        etList = new ArrayList<EditText>();
        locationLatitude = null;
        locationLongitude = null;
        // final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        vehNoLayout = (LinearLayout)findViewById(R.id.vehicleNoLayout);

        //  spinner.setOnItemSelectedListener(this);

        List<String> vehicleType = new ArrayList<String>();
        vehicleType.add("Bikes");
        vehicleType.add("Self drive cars");
        vehicleType.add("Cars with drivers");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehicleType);

        // spinner.setAdapter(dataAdapter);
/*
        noOfVehiclesAvailable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {

                    noOfVehicleNumbers = Integer.parseInt(noOfVehiclesAvailable.getText().toString());

                    vehNoLayout.removeAllViews();
                    etList.clear();

                    for (int i=1;i<=noOfVehicleNumbers;i++){

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.vehicle_number_layout, null);

                        EditText editText = (EditText)rowView.findViewById(R.id.editText);
                        editText.setId(i);

                        etList.add(editText);

                        vehNoLayout.addView(rowView, vehNoLayout.getChildCount() - 1);

                    }

                }
                catch (Exception e){

                    if (noOfVehiclesAvailable.getText().length() != 0){

                        Toast.makeText(UploadVehicleActivity.this, "enter the no of vehicles correctly", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        vehNoLayout.removeAllViews();

                    }

                }

            }
        });
*/
    }

    public void markLocation(View view){

        //to mark location on map
        Intent intent = new Intent(UploadVehicleActivity.this,MapsActivity.class);
        startActivity(intent);

    }

    public void uploadVehiclePhoto(View view){

        if (Build.VERSION.SDK_INT < 23) {

            //go to gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);


        } else {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
            else {

                //go to gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //go to gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            } else {
                // Permission Denied
                Toast.makeText(UploadVehicleActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //images selected

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null)
        {

            if (data.getClipData() != null){

                noOfImages = data.getClipData().getItemCount();
                filePath = data.getClipData().getItemAt(0).getUri();
                uris = new Uri[noOfImages];
                Log.i("number","" + noOfImages);

                for (int i=0;i<noOfImages;i++){

                    uris[i] = data.getClipData().getItemAt(i).getUri();

                }

            }
            else if (data.getData() != null){

                Log.i("no","1");
                noOfImages = 1;
                filePath = data.getData();
                uris = new Uri[1];
                uris[0] = filePath;
            }

            //filePath = data.getData();

            try {
                Toast.makeText(this, noOfImages + " images selected", Toast.LENGTH_SHORT).show();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                vehicleImage.setImageBitmap(bitmap);
                vehicleImage.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*   public void checkUpload(){
           for (;x<6;x++) {
               if (cb[x].isChecked()) {

                   if (locationLatitude != null && locationLongitude != null) {

                       uploadVehicle(cb[x].getText().toString());
                       break;

                   } else {

                       Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                   }

               }
           }

       }

   */
    public void submitVehicle(View view){

        //upload vehicle button is clicked

        try {

            NoOfVehicles  = Integer.parseInt(noOfVehiclesAvailable.getText().toString());
            // x=0;
            if (NoOfVehicles != 0) {

                if (vehicleName.getText().toString().length() != 0 && pricePerHour.getText().toString().length() != 0 && pricePerDay.getText().toString().length() != 0 && parkingAddress.getText().toString().length() != 0 && cityName.getText().toString().length() != 0 && bitmap != null) {

                    if (locationLatitude != null && locationLongitude != null) {

                        if (vehicleName.getText().toString().contains("$") || vehicleName.getText().toString().contains("#") || vehicleName.getText().toString().contains("[") || vehicleName.getText().toString().contains("]") || cityName.getText().toString().contains("$") || cityName.getText().toString().contains("#") || cityName.getText().toString().contains("[") || cityName.getText().toString().contains("]")){

                            Toast.makeText(this, "Special characters " +"(#,$,[,])" + " are not allowed.", Toast.LENGTH_SHORT).show();

                        }
                        else if (vehicleName.getText().toString().contains(".") || cityName.getText().toString().contains(".")){

                            if (cityName.getText().toString().contains(".")){

                                Toast.makeText(this, "Special characters " +"(.,#,$,[,])" + " are not allowed in city name.", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                vehName = vehicleName.getText().toString();
                                city = cityName.getText().toString();

                                vehName = vehName.replace(".", " ");

                                uploadVehicle("room");

                            }

                        }
                        else {

                            vehName = vehicleName.getText().toString();
                            city = cityName.getText().toString();

                            uploadVehicle("room");

                        }

                    } else {

                        Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                    }

                    // checkUpload();
                /*   if (cb[2].isChecked()) {

                       if (locationLatitude != null && locationLongitude != null) {
                           uploadVehicle(cb[2].getText().toString());

                       } else {

                           Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                       }

                   }

                   if (cb[3].isChecked()){

                       if (locationLatitude != null && locationLongitude != null) {
                           uploadVehicle(cb[3].getText().toString());

                       } else {

                           Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                       }

                   }

                   if (cb[4].isChecked()){

                       if (locationLatitude != null && locationLongitude != null) {
                           uploadVehicle(cb[4].getText().toString());

                       } else {

                           Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                       }

                   }

                   if (cb[5].isChecked()){

                       if (locationLatitude != null && locationLongitude != null) {
                           uploadVehicle(cb[5].getText().toString());

                       } else {

                           Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                       }

                   }

                   if (cb[6].isChecked()){

                       if (locationLatitude != null && locationLongitude != null) {
                           uploadVehicle(cb[6].getText().toString());

                       } else {

                           Toast.makeText(this, "please mark the parking address on the map", Toast.LENGTH_SHORT).show();

                       }

                   }
*/
                } else {

                    Toast.makeText(this, "Please specify all the fields", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch(NumberFormatException nfe) {
            System.out.println("please enter the field of no of rooms correctly");
        }
    }

    public void uploadVehicle (final String Veh){

        //upload vehicle to  firebase

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            storageReference.child(vehName).child("VehiclePhoto").putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadVehicleActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                storageRef.child("Vendors/" + FirstRunSecondActivity.vendorName + "/" + vehName + "/VehiclePhoto").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, String> vehicleDetails = new HashMap<String, String>();
                                        vehicleDetails.put("VehicleType", Veh);
                                        vehicleDetails.put("VehicleName", vehName);
                                        //vehicleDetails.put("VehicleNumber", vehicleNumber.getText().toString());
                                        vehicleDetails.put("ParkingAddress", parkingAddress.getText().toString());
                                        vehicleDetails.put("NoOfVehicles", noOfVehiclesAvailable.getText().toString());
                                        for(int i=0;i<6;i++){
                                            if(cb[i].isChecked()) {
                                                vehicleDetails.put("Tag" + i, cb[i].getText().toString());
                                            }

                                        }
                                        vehicleDetails.put("City", city);
                                        vehicleDetails.put("LocationLatitude", locationLatitude);
                                        vehicleDetails.put("LocationLongitude", locationLongitude);
                                        vehicleDetails.put("VehiclePhoto", uri.toString());
                                        vehicleDetails.put("isVehicleBooked", "false");
                                        vehicleDetails.put("PricePerHour", pricePerHour.getText().toString());
                                        vehicleDetails.put("PricePerDay", pricePerDay.getText().toString());
                                        vehicleDetails.put("isVehicleBlocked", "false");
                                        vehicleDetails.put("status","Pending");
                                        if (id.getText().toString().length() != 0){

                                            vehicleDetails.put("AgentId",id.getText().toString());

                                        }
                                        else {

                                            vehicleDetails.put("AgentId","MainAdmin");

                                        }

                                        uploadedVehicles = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

                                        String uid = uploadedVehicles.push().getKey();

                                        uploadedVehicles.child(uid).setValue(vehicleDetails);

                                        uploadVehicleToCustomer(uri.toString(),uid,uris);

                                    }
                                });
                                     /*    x=x+1;
                                        if (x<7){
                                            checkUpload();
                                        }else x=0;*/
                            }
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading your Room.Please Wait..!");
                        }
                    });
        }

    }


    public void uploadVehicleToCustomer(final String url, final String uid, final Uri[] uris2){

        final DatabaseReference uploadVehicle = FirebaseDatabase.getInstance().getReference();

        uploadVehicle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(city)){

                    DataSnapshot snapshot = dataSnapshot.child(city);

                    if (snapshot.hasChild("room")){

                        DataSnapshot snapshot1 = snapshot.child("room");

                        if (snapshot1.hasChild(vehName)){

                            DataSnapshot dataSnapshot1 = snapshot1.child(vehName);
                            String noOfVehicles = dataSnapshot1.child("NoOfVehiclesAvailable").getValue(String.class);
                            String updatedNoOfVehicles = Integer.toString(Integer.parseInt(noOfVehicles) + Integer.parseInt(noOfVehiclesAvailable.getText().toString()));
                            DatabaseReference referenceToUpdate = FirebaseDatabase.getInstance().getReference(city + "/room/" + vehName);
                            referenceToUpdate.child("NoOfVehiclesAvailable").setValue(updatedNoOfVehicles);
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("Address",parkingAddress.getText().toString());
                            map.put("VendorName",FirstRunSecondActivity.vendorName);
                            map.put("VendorUid",FirstRunSecondActivity.userUid);
                            map.put("NoOfVehicles",noOfVehiclesAvailable.getText().toString());
                            map.put("isVehicleBlocked","false");
                            map.put("status","Pending");
                            if (id.getText().toString().length() != 0){

                                map.put("AgentId",id.getText().toString());

                            }
                            else {

                                map.put("AgentId","MainAdmin");

                            }
                            for(int i=0;i<6;i++){
                                if (cb[i].isChecked()) {
                                    map.put("Tag" + i, cb[i].getText().toString());
                                }

                            }
                            map.put("LocationLatitude",locationLatitude);
                            map.put("LocationLongitude",locationLongitude);
                            referenceToUpdate.child("ParkingAddress").child(uid).setValue(map);

                        }
                        else {

                            newVehicle(url,uid);

                        }

                    }
                    else {

                        newVehicle(url,uid);
                    }

                }
                else {

                    newVehicle(url,uid);
                }

                extraImages(noOfImages, uris2,uid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void newVehicle(String url,String uid){

        Map<String,String> map = new HashMap<String, String>();
        map.put("NoOfVehiclesAvailable",noOfVehiclesAvailable.getText().toString());
        map.put("PricePerHour",pricePerHour.getText().toString());
        map.put("PricePerDay",pricePerDay.getText().toString());
        //map.put("VehicleNumber",vehicleNumber.getText().toString());
        map.put("VehiclePhoto",url);
        //map.put("isAddedToFavourites","false");

        DatabaseReference referenceToUpdate = FirebaseDatabase.getInstance().getReference(city + "/room/" + vehName);

        // DatabaseReference uploadReference = FirebaseDatabase.getInstance().getReference(cityName.getText().toString() + "/" + vehicleType.getText().toString());

        referenceToUpdate.setValue(map);

        Map<String,String> parkingAddressMap = new HashMap<String, String>();
        parkingAddressMap.put("Address",parkingAddress.getText().toString());
        parkingAddressMap.put("VendorName",FirstRunSecondActivity.vendorName);
        parkingAddressMap.put("VendorUid",FirstRunSecondActivity.userUid);
        parkingAddressMap.put("NoOfVehicles",noOfVehiclesAvailable.getText().toString());
        parkingAddressMap.put("status","Pending");

        for(int i=0;i<6;i++){
            if (cb[i].isChecked()) {
                parkingAddressMap.put("Tag" + i, cb[i].getText().toString());
            }

        }
        parkingAddressMap.put("isVehicleBlocked","false");
        parkingAddressMap.put("LocationLatitude",locationLatitude);
        parkingAddressMap.put("LocationLongitude",locationLongitude);
        if (id.getText().toString().length() != 0){

            parkingAddressMap.put("AgentId",id.getText().toString());

        }
        else {

            parkingAddressMap.put("AgentId","MainAdmin");

        }

        // DatabaseReference parkingReference = FirebaseDatabase.getInstance().getReference(cityName.getText().toString() + "/" + vehicleType.getText().toString() + "/" + vehicleName.getText().toString());

        referenceToUpdate.child("ParkingAddress").child(uid).setValue(parkingAddressMap);

    }

    public void extraImages(int allData, Uri[] uris1, final String uid) {

        for (int i = 0; i < allData; i++) {
            Uri uri = uris1[i];
           /* String nameFile = getRightPathFromProvider(MainActivity.this,uri);
            mList.add(new ImageModel(nameFile));
            mListStatus.add("uploading");*/

            //FireBase Storage
            final int finalI = i;

            FirebaseStorage.getInstance().getReference("Vendors/" + FirstRunSecondActivity.vendorName + "/" + vehName + "/" + parkingAddress.getText().toString() + "/ExtraImages/" + uid + "/" + "ExtraImage" + i).putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })

                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                storageRef.child("Vendors/" + FirstRunSecondActivity.vendorName + "/" + vehName + "/" + parkingAddress.getText().toString() + "/ExtraImages/" + uid + "/" + "ExtraImage" + finalI).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {

                                        DatabaseReference extraReference = FirebaseDatabase.getInstance().getReference(city + "/room/" + vehName + "/ExtraImages");

                                        extraReference.push().child(FirstRunSecondActivity.vendorName).setValue(uri.toString());

                                        Log.i("status","task completed");

                                        DatabaseReference vendorExtraReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");

                                        vendorExtraReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    if (vehName.equals(snapshot.child("VehicleName").getValue(String.class))) {

                                                        DatabaseReference vendorReference = snapshot.getRef();

                                                        vendorReference.child("ExtraImages").push().child("ExtraImage").setValue(uri.toString());

                                                    }

                                                }

                                                updateFavourites();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                });

                            }

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            // mProgressBar.setProgress((int) progress);

                        }
                    });


        }
    }

    public void updateFavourites(){

        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("Users");

        updateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    DataSnapshot dataSnapshot1 = snapshot.child("Favourites");

                    for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()){

                        if (vehName.equals(snapshot1.child("VehicleName").getValue(String.class))){

                            String noOfVehicles = snapshot1.child("NoOfVehiclesAvailable").getValue(String.class);
                            String updatedNoOfVehicles = Integer.toString(Integer.parseInt(noOfVehicles) + Integer.parseInt(noOfVehiclesAvailable.getText().toString()));
                            snapshot1.getRef().child("NoOfVehiclesAvailable").setValue(updatedNoOfVehicles);
                            snapshot1.getRef().child("ParkingAddress").setValue("This vehicle is available from " + updatedNoOfVehicles + " vendors");

                        }

                    }

                }

                Toast.makeText(UploadVehicleActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                vehicleImage.setVisibility(View.GONE);

                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        vehType = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(),vehType, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
