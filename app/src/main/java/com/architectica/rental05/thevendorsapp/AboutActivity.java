package com.architectica.rental05.thevendorsapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(this);

        viewPager.setAdapter(slideAdapter);

    }
}
