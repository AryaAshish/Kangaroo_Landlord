package com.architectica.rental05.thevendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    //String vendorUid;
    DatabaseReference vendorReference;
    EditText firstName,lastName,address,aadharNumber;
    ImageView aadharImage;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference,storageRef;
    DatabaseReference vendorDetails;
    Bitmap bitmap;

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
    }

    public void uploadAadhar(View view){

        //upload aadhar button is clicked
        //go to gallery to select a photo

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //picture selected

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
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

                StorageReference ref = storageReference.child("AadharPhoto");
                ref.putFile(filePath)
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

                                    storageRef.child("Vendors/" + FirstRunSecondActivity.vendorName + "/AadharPhoto").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            FirstRunSecondActivity.userUid = FirebaseAuth.getInstance().getUid();

                                            vendorDetails = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid);
                                            vendorDetails.child("FirstName").setValue(firstName.getText().toString());
                                            vendorDetails.child("LastName").setValue(lastName.getText().toString());
                                            vendorDetails.child("VendorAddress").setValue(address.getText().toString());
                                            vendorDetails.child("AadharNumber").setValue(aadharNumber.getText().toString());
                                            vendorDetails.child("AadharUrl").setValue(uri.toString());
                                            vendorDetails.child("Status").setValue("Pending");
                                            vendorDetails.child("Name").setValue(FirstRunThirdActivity.name);
                                            vendorDetails.child("Email").setValue(FirstRunThirdActivity.email);
                                            vendorDetails.child("PhoneNumber").setValue(FirstRunThirdActivity.countryCodeMobileNumber);

                                            progressDialog.dismiss();

                                            aadharImage.setVisibility(View.GONE);

                                            getSharedPreferences("User",MODE_PRIVATE)
                                                    .edit()
                                                    .putString("UserUid", FirstRunSecondActivity.userUid)
                                                    .apply();

                                            getSharedPreferences("VendorName",MODE_PRIVATE)
                                                    .edit()
                                                    .putString("Name", FirstRunSecondActivity.vendorName)
                                                    .apply();


                                            Intent intent = new Intent(ProfileActivity.this,UnVerifiedUser.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
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

        }
        else {

            //all fields not entered
            //ask the user to enter all the fields
            Toast.makeText(this, "Please specify all the fields", Toast.LENGTH_SHORT).show();

        }

    }

}
