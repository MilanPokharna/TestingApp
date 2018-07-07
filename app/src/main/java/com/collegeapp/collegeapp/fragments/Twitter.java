package com.collegeapp.collegeapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.activities.NewPostActivity;
import com.collegeapp.collegeapp.adapters.TwitterAdapter;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class Twitter extends Fragment {


    DatabaseReference mref;
    TwitterAdapter adapter;
    List<User> userList = new ArrayList<>();
    View v;
    @BindView(R.id.bar)
    BottomAppBar bar;
    Unbinder unbinder;
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    RecyclerView twitterRecycler;
    ProgressDialog progressDialog;
    public FirebaseUser user;
    public FirebaseAuth mauth = FirebaseAuth.getInstance();

    public Twitter() {
        //only constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        twitterRecycler = (RecyclerView)v.findViewById(R.id.twitter_recycler);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadData();

    }

    private void loadData() {


        mref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
        mref.keepSynced(true);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter = new TwitterAdapter(getContext(), userList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);
                twitterRecycler.setLayoutManager(layoutManager);
                twitterRecycler.setHasFixedSize(true);
                twitterRecycler.setAdapter(adapter);
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

    @OnClick(R.id.bar)
    public void onViewClicked() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_nav, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        dialog.setContentView(view);
        dialog.show();
        user = mauth.getCurrentUser();

        CircleImageView circleImageView;
        circleImageView = (CircleImageView)view.findViewById(R.id.profilephoto);
        Glide.with(getContext()).load(user.getPhotoUrl()).into(circleImageView);

        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.app_bar_fav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                navigationView.setCheckedItem(menuItem.getItemId());
                Fragment newFragment;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {

                    case R.id.app_bar_fav:
                        newFragment = new Twitter();
                        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.twitter_recycle, newFragment)
                        .commit();
                        dialog.dismiss();
                        break;

                    case R.id.app_bar_search:
                        newFragment = new fragment_my_post();
                        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.newRelative, newFragment)
                        .commit();
                        dialog.dismiss();
                        break;
                }

                return false;
            }
        });

    }

    @OnClick(R.id.fab)
    public void onFabClicked() {

        Intent intent = new Intent(getActivity(), NewPostActivity.class);
        getActivity().startActivity(intent);
    }

}
