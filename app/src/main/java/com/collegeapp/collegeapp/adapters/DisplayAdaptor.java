package com.collegeapp.collegeapp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.CardView;

import com.collegeapp.collegeapp.fragments.About_usFragement;
import com.collegeapp.collegeapp.fragments.ContactLinkFragement;

public class DisplayAdaptor extends FragmentPagerAdapter
{


    public static final int MAX_ELEVATION_FACTOR = 8;
    String key;
    Fragment fragment;
    public DisplayAdaptor(FragmentManager fm, String key) {

        super( fm );
        this.key = key;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                About_usFragement about_usFragement = new About_usFragement(key);
                return about_usFragement;
            case 1:
                ContactLinkFragement contactLinkFragement = new ContactLinkFragement(key);
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


    public CardView getCardViewAt(int position) {
        switch (position) {
            case 0:
                return About_usFragement.getCardView();
            case 1:
                return ContactLinkFragement.getCardView();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public float getBaseElevation() {
        return 2;
    }


}
