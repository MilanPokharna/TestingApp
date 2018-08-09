package com.blupie.technotweets.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.blupie.technotweets.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactLinkFragement extends Fragment {

    public String key;
    public String squal, sarea, semail, sname, sexp, imgurl, spos ,snum;
    public View v;
    DatabaseReference myref;
    static CardView cardView;


    Unbinder unbinder;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.cardposition)
    TextView cardposition;

    @BindView(R.id.qual)
    TextView qual;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.exp)
    TextView exp;
    @BindView(R.id.cardscroll)
    ScrollView cardscroll;
    @BindView(R.id.cardViewcontectlink)
    CardView cardViewcontectlink;
    @BindView(R.id.profileimage_contactlink)
    CircleImageView profileimageContactlink;
    @BindView(R.id.linklayout)
    RelativeLayout linklayout;
    @BindView(R.id.explayout)
    LinearLayout explayout;


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
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        key = getArguments().getString("key");
        loadData();
    }

    private void loadData() {
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");
        final StorageReference ref = FirebaseStorage.getInstance().getReference();
        myref.keepSynced(true);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semail = dataSnapshot.child(key).child("emailid").getValue().toString();
                sname = dataSnapshot.child(key).child("name").getValue().toString();
                spos = dataSnapshot.child(key).child("pos").getValue().toString();
//                snum = dataSnapshot.child(key).child("number").getValue().toString();
                imgurl = dataSnapshot.child(key).child("image").getValue().toString();
                squal = dataSnapshot.child(key).child("qualification").getValue().toString();
                sarea = dataSnapshot.child(key).child("area").getValue().toString();
                sexp = dataSnapshot.child(key).child("experience").getValue().toString();
                username.setText(sname);
                cardposition.setText(spos);
                qual.setText(squal);
                area.setText(sarea);
                if (!sexp.equals("00"))
                    exp.setText(sexp);
                else
                {
                    explayout.setVisibility(View.GONE);
                }
                Glide.with(getContext()).using(new FirebaseImageLoader()).load(ref.child(imgurl)).into(profileimageContactlink);
                //Glide.with(getContext()).load(imgurl).into(profileimageContactlink);
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

    @OnClick(R.id.linklayout)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
