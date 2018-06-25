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
import android.widget.TextView;

import com.collegeapp.collegeapp.R;
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
    public List<contacts> contactsList = new ArrayList<>();
    DatabaseReference myref, dt;
    ProgressDialog progressDialog;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;

    public BusRoute() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_bus_route, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Contact List");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        init();
        loadData();

    }

    private void loadData() {
        contactsList.clear();
        dt = FirebaseDatabase.getInstance().getReference().child("root").child("bus routes");
        dt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String d = dataSnapshot.child("date").getValue().toString();
                String t = dataSnapshot.child("time").getValue().toString();
                date.setText(d);
                time.setText(t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("bus routes").child("going buses");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    contacts contactvar = new contacts(snapshot.child("bus").getValue().toString(), snapshot.child("driver").getValue().toString() ,snapshot.child("number").getValue().toString(),
                            snapshot.child("route").getValue().toString());
                    contactsList.add(contactvar);
                }
                recyclerViewAdapterTwo = new RecyclerViewAdaptertwo(getContext(), contactsList);
                progressDialog.dismiss();
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
