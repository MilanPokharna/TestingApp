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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (isNetworkConnected())
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            msectionAdapter = new sectionAdapter(getSupportFragmentManager());
            pager.setAdapter(msectionAdapter);
            tablayout.setupWithViewPager(pager);
        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity.this);
            dialog.setTitle("Connectino Error ");
            dialog.setCancelable(false);
            dialog.setMessage("Unable to connect with the server.\n Check your Internet connection and try again." );
            dialog.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(),mainActivity.class);
                    startActivity(intent);
                }
            }).show();
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
