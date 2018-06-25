package com.collegeapp.collegeapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.fragments.BusRoute;
import com.collegeapp.collegeapp.fragments.Canteen;
import com.collegeapp.collegeapp.fragments.ContactList;
import com.collegeapp.collegeapp.fragments.Twitter;

public class sectionAdapter extends FragmentPagerAdapter {
    public sectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new Twitter();
            case 1:
                ContactList contactList =new ContactList();
                return  contactList;
            case 2:
                BusRoute busRoute = new BusRoute();
                return busRoute;
            case 3:
                Canteen canteen = new Canteen();
                return canteen;
            default:
                return null;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position)
        {
            case 0:
                return null;
            case 1:
                return "Contacts";
            case 2:
                return "Bus Route";
            case 3:
                return "Canteen";
            default:
                return null ;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
