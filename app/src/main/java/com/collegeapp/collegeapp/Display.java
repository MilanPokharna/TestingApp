package com.collegeapp.collegeapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.app.ProgressDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Display extends AppCompatActivity {
    ProgressDialog progressDialog;
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");
    public String key;
    public ImageView img;
    public ViewPager mviewpager;
    public TabLayout mTabLayout;
    public DisplayAdaptor mDisplayAdapter;
    public AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display );
        Intent i = getIntent();
        key = i.getStringExtra("key");
//        Bundle bundle = new Bundle();
//        bundle.putString("key",key);
//        ContactLinkFragement fragobj = new ContactLinkFragement();
//        fragobj.setArguments(bundle);
//        Bundle b = new Bundle();
//        b.putString("master1",key);
//        About_usFragement fragob = new About_usFragement();
//        fragob.setArguments(b);
        img = (ImageView)findViewById(R.id.profileimage);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.child(key).child("image").getValue().toString();
                Glide.with(getApplicationContext()).load(a).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mviewpager = findViewById(R.id.pager);
        mTabLayout = findViewById(R.id.tablayout);
        mDisplayAdapter = new DisplayAdaptor(getSupportFragmentManager());
        mviewpager.setAdapter(mDisplayAdapter);
        mTabLayout.setupWithViewPager(mviewpager);
    }
}
