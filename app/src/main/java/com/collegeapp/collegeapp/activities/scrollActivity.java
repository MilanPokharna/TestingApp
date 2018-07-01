package com.collegeapp.collegeapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.DisplayAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class scrollActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseReference myref;
    public String key;
    public  DisplayAdaptor displayAdaptor;
    public ViewPager mViewpager;

    public CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(com.collegeapp.collegeapp.R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.profileImg)
    ImageView profileimage;
    private boolean mShowingFragments = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.collegeapp.collegeapp.R.layout.activity_scrolling);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mViewpager =(ViewPager)findViewById( R.id.viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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


    }
}

