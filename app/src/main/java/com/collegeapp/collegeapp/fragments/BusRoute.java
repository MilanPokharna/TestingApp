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
import com.collegeapp.collegeapp.adapters.RecyclerViewAdapter;
import com.collegeapp.collegeapp.adapters.RecyclerViewAdaptertwo;
import com.collegeapp.collegeapp.models.contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BusRoute extends Fragment {

    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView;
    Unbinder unbinder;
    public LinearLayoutManager layoutManager;
    public RecyclerViewAdaptertwo recyclerViewAdapterTwo;
    public View view;
    public List<contacts>contactsList=new ArrayList<>();
    DatabaseReference myref;
    public BusRoute() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bus_route, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadData();
    }

    private void loadData() {
        contactsList.clear();
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("bus routes");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    contacts contactvar = new contacts(snapshot.child("name").getValue().toString(), snapshot.child("route").getValue().toString(),
                            snapshot.child("number").getValue().toString(), snapshot.child("profilepic").getValue().toString());
                    contactsList.add(contactvar);
                }
                recyclerViewAdapterTwo = new RecyclerViewAdaptertwo(getContext(), contactsList);
                recyclerView.setAdapter(recyclerViewAdapterTwo);
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
