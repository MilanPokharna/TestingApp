package com.collegeapp.collegeapp.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        msectionAdapter = new sectionAdapter(getSupportFragmentManager());
        pager.setAdapter(msectionAdapter);
        tablayout.setupWithViewPager(pager);

    }
}
