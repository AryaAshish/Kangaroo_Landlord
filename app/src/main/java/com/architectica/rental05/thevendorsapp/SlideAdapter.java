package com.architectica.rental05.thevendorsapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public int[] imagesList = {R.drawable.splashscreen,R.drawable.loginscreen,R.drawable.signupscreen,R.drawable.homescreen,R.drawable.uploadvehiclescreen,R.drawable.servicesscreen,R.drawable.bookingsscreen,R.drawable.chatscreen};

    public String[] description_text = {"The splash screen is the first screen that appears when you launch the app","When you launch the app for the first time you will be asked to login to your account.If you are a new user click on verify to create an account by verifying your phone number.","If you are a new user,then you have to create a new account","After successful login to yuor activity,you will see the home screen","Click on the upload vehicle button to upload a vehicle for rent","Click on the services button to view all your uploaded vehicles","Click on the Bookings button to see your bookings","Click on the Chats button to see your chats"};

    public SlideAdapter(Context context){

        this.context = context;

    }

    @Override
    public int getCount() {
        return imagesList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide,container,false);

        LinearLayout slideLayout = (LinearLayout)view.findViewById(R.id.slideLinearLayout);

        ImageView slideImg = (ImageView)view.findViewById(R.id.slideimg);

        TextView description = (TextView)view.findViewById(R.id.tutorialText);

        slideImg.setImageResource(imagesList[position]);

        description.setText(description_text[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout)object);

    }
}
