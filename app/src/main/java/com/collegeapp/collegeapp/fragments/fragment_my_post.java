package com.collegeapp.collegeapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.R;
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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class fragment_my_post extends Fragment {
    DatabaseReference mref, ref, reference;
    RecyclerView recyclerView;
    profileAdapter adapter;

    List<String> userList = new ArrayList<>();
    List<User> posts = new ArrayList<>();
    View v;
    String uid;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    ProgressDialog progressDialog;

    public static TextView text;
    Unbinder unbinder;
    public static CoordinatorLayout snakebar;

    public fragment_my_post() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.myprofile, container, false);
        progressDialog = new ProgressDialog(getActivity());
        user = auth.getCurrentUser();
        text = (TextView)rootview.findViewById(R.id.text);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.profilerecycler);
        snakebar = (CoordinatorLayout)rootview.findViewById(R.id.snakebar);
        unbinder = ButterKnife.bind(this, rootview);
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

        uid = user.getUid().toString();
        ref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users").child(uid).child("value");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue().toString();
                if (a.equals("1")) {
                    text.setVisibility(View.GONE);
                    callme();
                } else {
                    text.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callme() {
        mref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users").child(uid).child("posts");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String a = snapshot.getValue().toString();
                    userList.add(a);
                }
                callme2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callme2() {
        reference = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
        reference.keepSynced(true);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (String z : userList) {
                    User user = dataSnapshot.child(z).getValue(User.class);
                    posts.add(user);
                }
                adapter = new profileAdapter(getContext(), posts,userList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
