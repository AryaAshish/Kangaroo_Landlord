package com.architectica.rental05.thevendorsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    //String vendorUid;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    DatabaseReference vendorReference;
    EditText firstName,lastName,address,aadharNumber,agentId;
    ImageView aadharImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference,storageRef;
    DatabaseReference vendorDetails;
    Bitmap bitmap;
    int noOfImages;
    Uri[] uris;
    String agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //vendorUid = FirebaseAuth.getInstance().getUid();
        vendorReference = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid);
        firstName = (EditText)findViewById(R.id.edit_text_first_name);
        lastName = (EditText)findViewById(R.id.edit_text_last_name);
        address = (EditText)findViewById(R.id.edit_text_address);
        aadharNumber = (EditText)findViewById(R.id.edit_text_aadhar);
        aadharImage = (ImageView)findViewById(R.id.aadharImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Vendors/" + FirstRunSecondActivity.vendorName);
        storageRef = storage.getReference();
        agentId = (EditText)findViewById(R.id.agentId);
    }

    public void uploadAadhar(View view){

        //upload aadhar button is clicked
        //go to gallery to select a photo

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
                Toast.makeText(ProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
                aadharImage.setImageBitmap(bitmap);
                aadharImage.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void completeProfile(View view){

        //complete profile button is clicked
        //upload profile details to firebase

        if (firstName.getText().toString().length() != 0 && lastName.getText().toString().length() != 0 && address.getText().toString().length() != 0 && aadharNumber.getText().toString().length() != 0 && bitmap != null){

            if(filePath != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                for (int i = 1; i <= noOfImages; i++) {

                    Uri uri = uris[i - 1];
                    final int finalI = i;

                    StorageReference ref = storageReference.child("IdProofs/" + i);
                    ref.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){

                                        storageRef.child("Vendors/" + FirstRunSecondActivity.vendorName + "/IdProofs/" + finalI).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                FirstRunSecondActivity.userUid = FirebaseAuth.getInstance().getUid();

                                                vendorDetails = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid);

                                                if (finalI==1){

                                                    vendorDetails.child("AadharUrl").setValue(uri.toString());

                                                }
                                                else {

                                                    vendorDetails.child("IdProof" + finalI).setValue(uri.toString());

                                                }

                                            }
                                        });

                                    }
                                    else {
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });

                }

                FirstRunSecondActivity.userUid = FirebaseAuth.getInstance().getUid();
                agent = agentId.getText().toString();

                vendorDetails = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid);
                vendorDetails.child("FirstName").setValue(firstName.getText().toString());
                vendorDetails.child("LastName").setValue(lastName.getText().toString());
                vendorDetails.child("VendorAddress").setValue(address.getText().toString());
                vendorDetails.child("AadharNumber").setValue(aadharNumber.getText().toString());
                vendorDetails.child("Status").setValue("Pending");
                vendorDetails.child("Name").setValue(FirstRunThirdActivity.name);
                vendorDetails.child("Email").setValue(FirstRunThirdActivity.email);
                vendorDetails.child("PhoneNumber").setValue(FirstRunThirdActivity.countryCodeMobileNumber);
                if (agent.length() == 0){
                    vendorDetails.child("AgentId").setValue("MainAdmin");
                }
                else {
                    vendorDetails.child("AgentId").setValue(agent);
                }
                getSharedPreferences("User",MODE_PRIVATE)
                        .edit()
                        .putString("UserUid", FirstRunSecondActivity.userUid)
                        .apply();

                getSharedPreferences("VendorName",MODE_PRIVATE)
                        .edit()
                        .putString("Name", FirstRunSecondActivity.vendorName)
                        .apply();

                //MainActivity.deviceTokenId = FirebaseInstanceId.getInstance().getToken();

                //FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid).child("TokenId").setValue(MainActivity.deviceTokenId);

                progressDialog.dismiss();

                aadharImage.setVisibility(View.GONE);

                Intent intent = new Intent(ProfileActivity.this,UnVerifiedUser.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

            }

        }
        else {

            //all fields not entered
            //ask the user to enter all the fields
            Toast.makeText(this, "Please specify all the fields", Toast.LENGTH_SHORT).show();

        }

    }

}
