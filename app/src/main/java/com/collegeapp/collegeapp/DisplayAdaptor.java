package com.collegeapp.collegeapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DisplayAdaptor extends FragmentPagerAdapter
{
    public DisplayAdaptor(FragmentManager fm) {
        super( fm );
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                About_usFragement about_usFragement = new About_usFragement();
                return about_usFragement;
            case 1:
                ContactLinkFragement contactLinkFragement = new ContactLinkFragement();
                return contactLinkFragement;
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
                return "About Us";
            case 1:
                return "Contact Link";
            default:
                return null ;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
