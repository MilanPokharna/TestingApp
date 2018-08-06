package com.collegeapp.collegeapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.app.ProgressDialog;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.sectionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mainActivity extends AppCompatActivity {

    public sectionAdapter msectionAdapter;


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.mpager)
    ViewPager pager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int tabIcons = R.drawable.ic_group_black_24dp;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("notify");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            msectionAdapter = new sectionAdapter(getSupportFragmentManager());
            pager.setAdapter(msectionAdapter);
            tablayout.setupWithViewPager(pager);
            tablayout.getTabAt(0).setIcon(tabIcons);
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tablayout.getChildAt(0)).getChildAt(0));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
            layoutParams.weight = 0.5f;

            layout.setLayoutParams(layoutParams);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threebutton,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_set:
            {
                Intent intent = new Intent(mainActivity.this,aboutUs.class);
                startActivity(intent);
                break;
            }
            case R.id.action_logout:
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity.this);
                dialog.setTitle("Logout");
                dialog.setCancelable(false);
                dialog.setMessage("Logout from Techno Tweets Are You Sure ?" );
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        FirebaseAuth mauth = FirebaseAuth.getInstance();
//                        FirebaseUser user = mauth.getCurrentUser();
                        SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
                        prefs.edit().putInt("loginvar", 0).apply();
                        Intent intent = new Intent(mainActivity.this,StartActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

                break;
            }

        }
        return  true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int pid=android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
