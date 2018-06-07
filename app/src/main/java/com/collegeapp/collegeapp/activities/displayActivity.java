package com.collegeapp.collegeapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

public class displayActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");

    public String key;

    public DisplayAdaptor mDisplayAdapter;

    @BindView(R.id.profileimage)
    ImageView profileimage;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);
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

        mDisplayAdapter = new DisplayAdaptor(getSupportFragmentManager());
        pager.setAdapter(mDisplayAdapter);
        tablayout.setupWithViewPager(pager);
    }
}