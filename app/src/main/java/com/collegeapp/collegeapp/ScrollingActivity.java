package com.collegeapp.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.adapters.DisplayAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollingActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseReference myref;
    public String key;
    public  DisplayAdaptor displayAdaptor;
    public ViewPager mViewpager;
    TabLayout mtablayout;

    public CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profileimage)
    ImageView profileimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mViewpager =(ViewPager)findViewById(R.id.pager);
        mtablayout =(TabLayout)findViewById(R.id.tablayout);
        Intent i = getIntent();

        key = i.getStringExtra("key");
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.child(key).child("image").getValue().toString();
                Glide.with(getApplicationContext()).load(a).into(profileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayAdaptor = new DisplayAdaptor(getSupportFragmentManager(), key);
        mViewpager.setAdapter(displayAdaptor);
        mtablayout.setupWithViewPager(mViewpager);


    }
}
