package com.collegeapp.collegeapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

public class About_usFragement extends Fragment {

    public String key;
    public String sname;
    public String spos;
    public String sdesc;
    static CardView cardView;
    public View v;

    ProgressDialog progressDialog;
    DatabaseReference myref;

    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.description)
    TextView description;
    Unbinder unbinder;

    public About_usFragement() {
    }
    @SuppressLint("ValidFragment")
    public About_usFragement(String key) {
        this.key = key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Details");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        View rootView = inflater.inflate(R.layout.fragment_about_us_fragement, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        cardView=(CardView)rootView.findViewById(R.id.cardViewdetails);
        cardView.setMaxCardElevation(cardView.getCardElevation()
                * DisplayAdaptor.MAX_ELEVATION_FACTOR);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        loadData();
    }

    private void loadData() {
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons").child(key);
        myref.keepSynced(true);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spos = (String) dataSnapshot.child("pos").getValue();
                sname = (String) dataSnapshot.child("name").getValue();
                sdesc = (String) dataSnapshot.child("description").getValue();
                name.setText(sname);
                description.setText(sdesc);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static CardView getCardView() {
        return cardView;
    }
}
