package com.collegeapp.collegeapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mainActivity extends AppCompatActivity {


    public ViewPager mviewpager;
    public TabLayout mTabLayout;
    public sectionAdapter msectionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mviewpager = findViewById(R.id.pager);
        mTabLayout = findViewById(R.id.tablayout);
        msectionAdapter = new sectionAdapter(getSupportFragmentManager());
        mviewpager.setAdapter(msectionAdapter);
        mTabLayout.setupWithViewPager(mviewpager);



    }
}
