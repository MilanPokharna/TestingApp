package com.collegeapp.collegeapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.app.ProgressDialog;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.sectionAdapter;

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
    protected void onDestroy() {
        super.onDestroy();
        int pid=android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

}
