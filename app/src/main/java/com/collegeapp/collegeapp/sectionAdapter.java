package com.collegeapp.collegeapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

public class sectionAdapter extends FragmentPagerAdapter {
    public sectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ContactList contactList =new ContactList();
                return  contactList;
            case 1:
                BusRoute busRoute = new BusRoute();
                return busRoute;
            default:
                return null;
        }


    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position)
        {
            case 0:
                return "Contact List";
            case 1:
                return "Bus Route";
                default:
                    return null ;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
