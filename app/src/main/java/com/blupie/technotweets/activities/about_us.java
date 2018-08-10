package com.blupie.technotweets.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blupie.technotweets.R;
import com.blupie.technotweets.adapters.DisplayAdaptor;
import com.blupie.technotweets.models.ShadowTransformer;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class about_us extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseReference myref;
    public String key;
    public  DisplayAdaptor displayAdaptor;
    public ViewPager mViewpager;

    public CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
//    @BindView(R.id.profileImg)
//    CircleImageView profileimage;
//    @BindView(R.id.name)
//    TextView name;
//    @BindView(R.id.position)
//    TextView position;
//    @BindView(R.id.phone)
//    TextView phone;

    private boolean mShowingFragments = false;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_about_us);
         ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mViewpager =(ViewPager)findViewById( R.id.viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();

//        key = i.getStringExtra("key");
//        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");

//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String a = dataSnapshot.child(key).child("image").getValue().toString();
//                String na = dataSnapshot.child(key).child("name").getValue().toString();
//                String ph = dataSnapshot.child(key).child("pos").getValue().toString();
//                String num = dataSnapshot.child(key).child("number").getValue().toString();
//                name.setText(na);
//                position.setText(ph);
//                phone.setText(num);
//                Glide.with(getApplicationContext()).load(a).into(profileimage);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        displayAdaptor = new DisplayAdaptor(getSupportFragmentManager());
        ShadowTransformer shadowTransformer=new ShadowTransformer(mViewpager,displayAdaptor);
        mViewpager.setAdapter(displayAdaptor);
        mViewpager.setPageTransformer(false,shadowTransformer);
        mViewpager.setOffscreenPageLimit(1);


    }

}

