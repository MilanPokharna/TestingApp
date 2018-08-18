package com.blupie.technotweets.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blupie.technotweets.R;
import com.blupie.technotweets.adapters.RecyclerViewAdaptertwo;
import com.blupie.technotweets.models.contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.blupie.technotweets.activities.mainActivity.pager;

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
    @BindView(R.id.datebus)
    TextView date;
    @BindView(R.id.timebus)
    TextView time;
    Boolean perm = false;
    public static FrameLayout bus;
//    @BindView(R.id.swipe2)
//    SwipeRefreshLayout swipe2;
    @BindView(R.id.num)
    TextView num;

    public BusRoute() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bus_route, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        bus = (FrameLayout) rootView.findViewById(R.id.busroute);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        //Toast.makeText(getActivity().getApplicationContext(), "busroutes"+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
        try {
            layoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.setLayoutManager(layoutManager);
            //swipe2.setOnRefreshListener(this);
            loadData();
        } catch (Exception e) {

        }

    }

    private void loadData() {
        try {
            dt = FirebaseDatabase.getInstance().getReference().child("root").child("bus routes");
            dt.keepSynced(true);
            dt.addValueEventListener(new ValueEventListener() {
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
            myref.keepSynced(true);
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    contactsList.clear();
                    recyclerViewAdapterTwo.notifyDataSetChanged();
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        contacts contactvar = new contacts(snapshot.child("bus").getValue().toString(), snapshot.child("driver").getValue().toString(), snapshot.child("number").getValue().toString(),
                                snapshot.child("route").getValue().toString());
                        contactsList.add(contactvar);
                    }
                    try {
                        recyclerViewAdapterTwo = new RecyclerViewAdaptertwo(getContext(), contactsList);
                        recyclerView.setAdapter(recyclerViewAdapterTwo);
                    } catch (Exception e) {

                    }
                    //Toast.makeText(getContext(), "buslist :"+contactsList.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onRefresh() {
//        try {
//            //Toast.makeText(getActivity().getApplicationContext(), "bus refresh"+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
//            dt.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String d = dataSnapshot.child("date").getValue().toString();
//                    String t = dataSnapshot.child("time").getValue().toString();
//                    date.setText(d);
//                    time.setText(t);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            myref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                    contactsList.clear();
//                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
//                        contacts contactvar = new contacts(snapshot.child("bus").getValue().toString(), snapshot.child("driver").getValue().toString(), snapshot.child("number").getValue().toString(),
//                                snapshot.child("route").getValue().toString());
//                        contactsList.add(contactvar);
//                    }
//                    try {
//                        recyclerViewAdapterTwo = new RecyclerViewAdaptertwo(getContext(), contactsList);
//                        recyclerView.setAdapter(recyclerViewAdapterTwo);
//                    } catch (Exception e) {
//
//                    }
//                    //Toast.makeText(getContext(), "buslist :"+contactsList.size(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            if(swipe2.isRefreshing())
//            {
//                swipe2.setRefreshing(false);
//            }
//        } catch (Exception e) {
//
//        }
//
//    }

    @OnClick(R.id.num)
    public void onViewClicked() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + "8696932780"));
        startActivity(callIntent);
    }
}
