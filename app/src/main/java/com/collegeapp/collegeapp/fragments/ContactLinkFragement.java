package com.collegeapp.collegeapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.R;

import com.collegeapp.collegeapp.adapters.DisplayAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactLinkFragement extends Fragment {

    public String key;
    public String stwitter, sfacebook, semail, snumber;
    public View v;
    DatabaseReference myref;
    static CardView cardView;

    @BindView(R.id.twitterid)
    TextView twitterid;
    @BindView(R.id.facebookid)
    TextView facebookid;
    @BindView(R.id.emailid)
    TextView emailid;
    @BindView(R.id.linkedinid)
    TextView phone;
    Unbinder unbinder;

    public ContactLinkFragement() {
    }
    @SuppressLint("ValidFragment")
    public ContactLinkFragement(String key) {
        // Required empty public constructor
        this.key=key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_link_fragement, container, false);
        cardView=(CardView)rootView.findViewById(R.id.cardViewcontectlink);
        cardView.setMaxCardElevation(cardView.getCardElevation()
                * DisplayAdaptor.MAX_ELEVATION_FACTOR);

//        cardView.setCardElevation(20);
//        cardView.animate().scaleX(1.1f);
//        cardView.animate().scaleY(1.1f);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        loadData();
    }

    private void loadData() {
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");
        myref.keepSynced(true);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stwitter = dataSnapshot.child(key).child("twitter").getValue().toString();
                sfacebook = dataSnapshot.child(key).child("facebook").getValue().toString();
                semail = dataSnapshot.child(key).child("emailid").getValue().toString();
                snumber = dataSnapshot.child(key).child("number").getValue().toString();
                callme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callme() {
        twitterid.setText(stwitter);
        facebookid.setText(sfacebook);
        emailid.setText(semail);
        phone.setText(snumber);
    }

    public static CardView getCardView() {
        return cardView;
    }
}
