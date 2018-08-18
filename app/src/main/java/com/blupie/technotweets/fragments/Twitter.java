package com.blupie.technotweets.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blupie.technotweets.R;
import com.blupie.technotweets.activities.NewPostActivity;
import com.blupie.technotweets.activities.mainActivity;
import com.blupie.technotweets.adapters.TwitterAdapter;
import com.blupie.technotweets.models.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.blupie.technotweets.activities.mainActivity.pager;

public class Twitter extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    DatabaseReference mref;
    TwitterAdapter adapter;
    List<User> userList = new ArrayList<>();
    List<User> newList = new ArrayList<>();
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
    @BindView(R.id.fragment_twitter)
    CoordinatorLayout fragmentTwitter;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    public Twitter() {
        //empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        user = mauth.getCurrentUser();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        //Toast.makeText(getActivity().getApplicationContext(), "twitter"+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
        twitterRecycler = (RecyclerView) v.findViewById(R.id.twitter_recycler);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        swipe.setOnRefreshListener(this);
        //      progressDialog.show();
        loadData();
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                setup();
            }
        });

    }

    @SuppressLint("NewApi")
    private void loadData() {

        mref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
        mref.keepSynced(true);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                try {
                    adapter = new TwitterAdapter(getContext(), userList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setStackFromEnd(true);
                    layoutManager.setReverseLayout(true);
                    twitterRecycler.setLayoutManager(layoutManager);
                    twitterRecycler.setHasFixedSize(true);
                    twitterRecycler.setAdapter(adapter);
                }
                catch (Exception e){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newList.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    newList.add(user);
                }
                int i = (newList.size() - userList.size());
                if (i >= 3) {
//                    SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("login", Context.MODE_PRIVATE);
//                    int flag = prefs.getInt("flag", 1);
//                    if (flag == 1) {
//                        if (pager.getCurrentItem() == 0) {
//                            LayoutInflater inflater = getLayoutInflater();
//                            View layout = inflater.inflate(R.layout.custom_toast_layout,
//                                    (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
//                            TextView toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
//                            ImageView toastImageView = (ImageView) layout.findViewById(R.id.toastImageView);
//                            // set the text in the TextView
//                            toastTextView.setText("New Posts Available");
//                            toastImageView.setImageResource(R.drawable.ic_refresh_black_24dp);
//                            final PopupWindow pw = new PopupWindow(layout,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
//                            pw.setWindowLayoutType(WindowManager.LayoutParams.TYPE_TOAST);
//                            pw.showAtLocation(layout, Gravity.CENTER | Gravity.TOP, 0, 350);
//                            pw.setTouchable(true);
//
//                            layout.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    pw.dismiss();
//                                    try {
//                                        adapter = new TwitterAdapter(getContext(), newList);
//                                        userList = newList;
//                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                                        layoutManager.setStackFromEnd(true);
//                                        layoutManager.setReverseLayout(true);
//                                        twitterRecycler.setLayoutManager(layoutManager);
//                                        twitterRecycler.setHasFixedSize(true);
//                                        twitterRecycler.setAdapter(adapter);
//                                    } catch (Exception e) {
//                                    }
//                                }
//                            });
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    pw.dismiss();
//                                }
//                            }, 2000);
//                        }
//                    }
                }
                else if (i<0)
                {
                    adapter = new TwitterAdapter(getContext(), newList);
                    userList = newList;
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setStackFromEnd(true);
                    layoutManager.setReverseLayout(true);
                    twitterRecycler.setLayoutManager(layoutManager);
                    twitterRecycler.setHasFixedSize(true);
                    twitterRecycler.setAdapter(adapter);
                }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setup() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_nav, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(getContext()), R.style.BottomSheetDialog);
        dialog.setContentView(view);
        dialog.show();

        CircleImageView circleImageView;
        circleImageView = (CircleImageView) view.findViewById(R.id.profilephoto);
        Glide.with(getContext()).load(user.getPhotoUrl()).into(circleImageView);

        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.app_bar_fav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                navigationView.setCheckedItem(menuItem.getItemId());
                Fragment newFragment;
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
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
                        Bundle bundle = new Bundle();
                        bundle.putString("userid", null);
                        newFragment.setArguments(bundle);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.fab)
    public void onFabClicked() {

        if (Objects.requireNonNull(user.getEmail()).endsWith("@technonjr.org")) {
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            startActivityForResult(intent, 123);
        } else {
            Snackbar snackbar = Snackbar.make(fragmentTwitter, "Please Login with College ID to upload a Post", Snackbar.LENGTH_SHORT);

            snackbar.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            adapter = new TwitterAdapter(getContext(), newList);
            userList = newList;
            try {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setStackFromEnd(true);
                layoutManager.setReverseLayout(true);
                twitterRecycler.setLayoutManager(layoutManager);
                twitterRecycler.setHasFixedSize(true);
                twitterRecycler.setAdapter(adapter);
            }
            catch (Exception e){}

        }
    }

    @Override
    public void onRefresh() {
        try {
            //Toast.makeText(getActivity().getApplicationContext(), "twitter refresh"+pager.getCurrentItem(), Toast.LENGTH_SHORT).show();
            adapter = new TwitterAdapter(getContext(), newList);
            userList = newList;
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            twitterRecycler.setLayoutManager(layoutManager);
            twitterRecycler.setHasFixedSize(true);
            twitterRecycler.setAdapter(adapter);


            if (swipe.isRefreshing()) {
                swipe.setRefreshing(false);
            }
        }catch (Exception e){

        }
    }
}