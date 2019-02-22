package com.architectica.rental05.thevendorsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatsList extends AppCompatActivity {

    ListView customersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        customersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(chatsList.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        DatabaseReference membersList = FirebaseDatabase.getInstance().getReference("Vendors/" + FirstRunSecondActivity.userUid + "/Chats");

        //load chats list from firebase

        membersList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                al.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    al.add(snapshot.getKey());

                    totalUsers++;

                }

                Log.i("total users","" + totalUsers);

                if(totalUsers < 1){
                    noUsersText.setVisibility(View.VISIBLE);
                    customersList.setVisibility(View.GONE);
                }
                else{
                    noUsersText.setVisibility(View.GONE);
                    customersList.setVisibility(View.VISIBLE);
                    customersList.setAdapter(new ArrayAdapter<String>(chatsList.this, android.R.layout.simple_list_item_1, al));
                }

                pd.dismiss();

                customersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserDetails.username = FirstRunSecondActivity.vendorName;
                        UserDetails.chatWith = al.get(position);
                        startActivity(new Intent(chatsList.this, Chat.class));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
