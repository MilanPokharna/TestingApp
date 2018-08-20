package com.blupie.technotweets.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blupie.technotweets.adapters.profileAdapter;
import com.blupie.technotweets.models.User;
import com.bumptech.glide.Glide;
import com.blupie.technotweets.R;
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
import butterknife.Unbinder;

public class fragment_my_post extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    DatabaseReference mref, ref, reference;
    RecyclerView recyclerView;
    profileAdapter adapter;
    Toolbar toolbar;
    TextView name, email;
    List<String> userList = new ArrayList<>();
    List<User> posts = new ArrayList<>();
    View v;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;

    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;
    String uid;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    ProgressDialog progressDialog;
    ImageView circleImageView;
    public static TextView text;
    Unbinder unbinder;
    String data;
    Integer check = 0;
    public static CoordinatorLayout snakebar;

    public fragment_my_post() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.myprofile, container, false);
//        progressDialog = new ProgressDialog(getActivity());
        user = auth.getCurrentUser();
        circleImageView = (ImageView) rootview.findViewById(R.id.profileimage_profile);
        text = (TextView) rootview.findViewById(R.id.text);
        name = (TextView) rootview.findViewById(R.id.name_profile);
        email = (TextView) rootview.findViewById(R.id.email_profile);
        AppBarLayout appBarLayout = (AppBarLayout) rootview.findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appBarLayout.getTotalScrollRange();
        recyclerView = (RecyclerView) rootview.findViewById(R.id.profilerecycler);
        snakebar = (CoordinatorLayout) rootview.findViewById(R.id.snakebar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) rootview.findViewById(R.id.toolbar_profile);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }

        unbinder = ButterKnife.bind(this, rootview);
        return rootview;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = getArguments().getString("userid");
        check = 0;
        this.v = view;
        if (data != null) {
            try {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("root").child("twitter")
                        .child("users");
                database.keepSynced(true);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (data.equals(snapshot.getKey().toString())) {
                                uid = snapshot.child("uid").getValue().toString();
                                name.setText(snapshot.child("name").getValue().toString());
                                email.setText(snapshot.child("email").getValue().toString());
                                Glide.with(getActivity()).load(snapshot.child("profileimage").getValue().toString()).into(circleImageView);
                                check = 1;
                                if (uid.equals(user.getUid()))
                                    check = 0;
                                loadData();
                                break;
                            }
                        }
                        // name.setText(.getDisplayName());
                        //email.setText(user.getEmail());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            catch (Exception e){

            }
        }
        else {
            uid = user.getUid().toString();
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            Glide.with(getActivity()).load(user.getPhotoUrl()).into(circleImageView);
            loadData();
        }
//        progressDialog.setMessage("Loading");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

    }

    private void loadData() {


        ref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users").child(uid).child("value");
        ref.keepSynced(true);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue().toString();
                if (a.equals("1")) {
                    text.setVisibility(View.GONE);
                    callme();
                } else {
                    text.setVisibility(View.VISIBLE);
                    //progressDialog.dismiss();

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
                try {
                    adapter = new profileAdapter(getContext(), posts, userList, check);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setStackFromEnd(true);
                    layoutManager.setReverseLayout(true);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
                catch (Exception e){}

//                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            circleImageView.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            circleImageView.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
