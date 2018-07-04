package com.collegeapp.collegeapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.staggeredgridviewadapter;
import com.collegeapp.collegeapp.models.canteen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Canteen extends Fragment {

    public View view;
    @BindView(R.id.recyclerView3)
    RecyclerView recyclerView3;
    Unbinder unbinder;

    public List<canteen> dishlist = new ArrayList<>();
    public String bld;
    public DatabaseReference myref,ref;
    public ProgressDialog progressDialog;
    public staggeredgridviewadapter staggeredgridviewadapter;
    public GridLayoutManager staggeredGridLayoutManager;

    public Canteen() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_canteen, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        staggeredGridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(staggeredGridLayoutManager);
        ref = FirebaseDatabase.getInstance().getReference().child("root").child("canteen").child("theme");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bld = dataSnapshot.getValue().toString();
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadData() {
        dishlist.clear();

        myref = FirebaseDatabase.getInstance().getReference().child("root").child("canteen").child(bld);
        myref.keepSynced(true);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    canteen canteen = snapshot.getValue(com.collegeapp.collegeapp.models.canteen.class);
                    dishlist.add(canteen);
                }
                staggeredgridviewadapter = new staggeredgridviewadapter(getContext(),dishlist);
                recyclerView3.setAdapter(staggeredgridviewadapter);
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