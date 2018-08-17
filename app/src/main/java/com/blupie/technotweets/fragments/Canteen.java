package com.blupie.technotweets.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blupie.technotweets.adapters.staggeredgridviewadapter;
import com.blupie.technotweets.models.canteen;
import com.blupie.technotweets.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.blupie.technotweets.activities.mainActivity.pager;

public class Canteen extends Fragment {

    public View view;
    @BindView(R.id.recyclerView3)
    RecyclerView recyclerView3;
    Unbinder unbinder;
    String dayOfTheWeek;
    public List<canteen> dishlist = new ArrayList<>();
    public String mon = null,tue= null,wed= null,thur= null,fri= null,sat= null,sun = null;
    public DatabaseReference myref, ref;
    public ProgressDialog progressDialog;
    public com.blupie.technotweets.adapters.staggeredgridviewadapter staggeredgridviewadapter;
    public GridLayoutManager staggeredGridLayoutManager;
    @BindView(R.id.thaliitem)
    TextView thaliitem;

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
        //Toast.makeText(getActivity().getApplicationContext(), "canteen"+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        staggeredGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(staggeredGridLayoutManager);
        ref = FirebaseDatabase.getInstance().getReference().child("root").child("canteen").child("days");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mon = dataSnapshot.child("1").getValue().toString();
                tue = dataSnapshot.child("2").getValue().toString();
                wed = dataSnapshot.child("3").getValue().toString();
                thur = dataSnapshot.child("4").getValue().toString();
                fri = dataSnapshot.child("5").getValue().toString();
                sat = dataSnapshot.child("6").getValue().toString();
                sun = dataSnapshot.child("7").getValue().toString();

                callme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callme() {
        try {

            if (dayOfTheWeek.equals("Monday")) {
                if (mon != null)
                    thaliitem.setText(mon);
            } else if (dayOfTheWeek.equals("Tuesday")) {
                if (tue != null)
                    thaliitem.setText(tue);
            } else if (dayOfTheWeek.equals("Wednesday")) {
                if (wed != null)
                    thaliitem.setText(wed);
            } else if (dayOfTheWeek.equals("Thursday")) {
                if (thur != null)
                    thaliitem.setText(thur);
            } else if (dayOfTheWeek.equals("Friday")) {
                if (fri != null)
                    thaliitem.setText(fri);
            } else if (dayOfTheWeek.equals("Saturday")) {
                if (sat != null)
                    thaliitem.setText(sat);
            } else if (dayOfTheWeek.equals("Sunday")) {
                if (sun != null)
                    thaliitem.setText(sun);
            } else
                Toast.makeText(getContext(), dayOfTheWeek, Toast.LENGTH_SHORT).show();

            loadData();
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Canteen"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        try {

            myref = FirebaseDatabase.getInstance().getReference().child("root").child("canteen").child("breakfast");
            myref.keepSynced(true);
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dishlist.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        canteen canteen = snapshot.getValue(com.blupie.technotweets.models.canteen.class);
                        dishlist.add(canteen);
                    }
                    try {

                            staggeredGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            //recyclerView3.setHasFixedSize(true);
                            recyclerView3.setLayoutManager(staggeredGridLayoutManager);
                            staggeredgridviewadapter = new staggeredgridviewadapter(getContext(), dishlist);
                            recyclerView3.setAdapter(staggeredgridviewadapter);

                    }
                    catch (Exception a)
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Canteen"+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}