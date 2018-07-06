package com.collegeapp.collegeapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.DisplayAdaptor;
import com.collegeapp.collegeapp.models.contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactLinkFragement extends Fragment {

    public String key;
    public String stwitter, sfacebook, semail, sname,slinkedin,sdes,imgurl;
    public View v;
    DatabaseReference myref;
    static CardView cardView;


    Unbinder unbinder;
    @BindView(R.id.profileimage_contactlink)
    CircleImageView profileimageContactlink;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.description_contactlink)
    TextView description;
    @BindView(R.id.twitterid)
    ImageView twitterid;
    @BindView(R.id.facebookid)
    ImageView facebookid;
    @BindView(R.id.gmailid)
    ImageView gmailid;
    @BindView(R.id.linkedinid)
    ImageView linkedinid;
    @BindView(R.id.cardViewcontectlink)
    CardView cardViewcontectlink;

    public ContactLinkFragement() {
    }

    @SuppressLint("ValidFragment")
    public ContactLinkFragement(String key) {
        // Required empty public constructor
        this.key = key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_link_fragement, container, false);
        cardView = (CardView) rootView.findViewById(R.id.cardViewcontectlink);
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
                sname = dataSnapshot.child(key).child("name").getValue().toString();
                slinkedin = dataSnapshot.child(key).child("linkedin").getValue().toString();
                sdes = dataSnapshot.child(key).child("description").getValue().toString();
                imgurl = dataSnapshot.child(key).child("image").getValue().toString();
                username.setText(sname);
                description.setText(sdes);
                Glide.with(getContext()).load(imgurl).into(profileimageContactlink);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static CardView getCardView() {
        return cardView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.twitterid, R.id.facebookid, R.id.gmailid, R.id.linkedinid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.twitterid: {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(stwitter)));
                break;
            }
            case R.id.facebookid:
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sfacebook)));
                break;
            }
            case R.id.gmailid:
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", semail, null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            }
            case R.id.linkedinid:
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(slinkedin)));
                break;
            }
        }
    }
}
