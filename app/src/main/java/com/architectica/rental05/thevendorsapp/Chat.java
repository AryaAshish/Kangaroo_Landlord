package com.architectica.rental05.thevendorsapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    //String userUid;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Users/" + FirstRunSecondActivity.userUid + "/Name");

        pd = new ProgressDialog(Chat.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                for (com.google.firebase.database.DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UserDetails.username = dataSnapshot1.getValue().toString();
                }

                Firebase.setAndroidContext(getApplicationContext());
                reference1 = new Firebase("https://kangaroorooms-288e3.firebaseio.com/Vendors/" + FirstRunSecondActivity.userUid + "/Chats/" + UserDetails.chatWith);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                        for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()){

                            com.google.firebase.database.DataSnapshot dataSnapshot1 = snapshot.child("Name");

                            for (com.google.firebase.database.DataSnapshot snapshot1 : dataSnapshot1.getChildren()){

                                if (UserDetails.chatWith.equals(snapshot1.getValue(String.class))){

                                    reference2 = new Firebase("https://kangaroorooms-288e3.firebaseio.com/Users/" + snapshot.getKey() + "/Chats/" + UserDetails.username);

                                    sendButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String messageText = messageArea.getText().toString();

                                            //add chat to firebase
                                            if(!messageText.equals("")){
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("message", messageText);
                                                map.put("user", UserDetails.username);
                                                reference1.push().setValue(map);
                                                reference2.push().setValue(map);
                                                messageArea.setText("");
                                            }
                                        }
                                    });

                                    reference1.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Map map = dataSnapshot.getValue(Map.class);
                                            String message = map.get("message").toString();
                                            String userName = map.get("user").toString();

                                            //load chats from firebase

                                            if(userName.equals(UserDetails.username)){
                                                addMessageBox("You:-\n" + message, 1);
                                            }
                                            else{
                                                addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                            pd.dismiss();
                                        }
                                    });

                                }

                            }


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addMessageBox(String message, int type){

        pd.dismiss();
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
