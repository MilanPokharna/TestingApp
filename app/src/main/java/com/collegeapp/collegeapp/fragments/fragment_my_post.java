package com.collegeapp.collegeapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.TwitterAdapter;
import com.collegeapp.collegeapp.adapters.profileAdapter;
import com.collegeapp.collegeapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class fragment_my_post extends Fragment {
    DatabaseReference mref;
    RecyclerView recyclerView;
    profileAdapter adapter;
    List<String> userList = new ArrayList<>();
    View v;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    ProgressDialog progressDialog;
    public fragment_my_post() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.myprofile, container, false);
        progressDialog = new ProgressDialog(getActivity());
        user = auth.getCurrentUser();
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadData();
    }

    private void loadData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        String uid = user.getUid().toString();
        mref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users").child(uid).child("posts");
        userList.clear();
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String a = snapshot.getValue().toString();
                    userList.add(a);
                }
                adapter = new profileAdapter(getContext(), userList);
                recyclerView.setAdapter(adapter);
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
