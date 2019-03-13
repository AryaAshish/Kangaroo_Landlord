package com.architectica.rental05.thevendorsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    EditText newRent;
    String rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        newRent = (EditText)findViewById(R.id.newRent);

    }

    public void updateRent(View view){

        rent = newRent.getText().toString();

        if (rent.length() != 0){

            DatabaseReference update = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/UploadedVehicles");
            update.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if (HotelAdapter.city.equals(snapshot.child("City").getValue(String.class)) && HotelAdapter.name.equals(snapshot.child("VehicleName").getValue(String.class))){

                            snapshot.child("PricePerDay").getRef().setValue(rent);

                        }

                    }

                    FirebaseDatabase.getInstance().getReference(HotelAdapter.city + "/room/" + HotelAdapter.name).child("PricePerDay").setValue(rent).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(EditActivity.this, "rent updated", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else {

                                Toast.makeText(EditActivity.this, "failed to update rent.please try again later", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
