package com.blupie.technotweets.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.CardView;

import com.blupie.technotweets.fragments.About_usFragement;
import com.blupie.technotweets.fragments.ContactLinkFragement;
import com.blupie.technotweets.fragments.Gaurav;
import com.blupie.technotweets.fragments.Milan;
import com.blupie.technotweets.fragments.Shubham;
import com.blupie.technotweets.fragments.Tushar;

public class DisplayAdaptor extends FragmentPagerAdapter
{


    public static final int MAX_ELEVATION_FACTOR = 8;
    String key;
    Fragment fragment;
    public DisplayAdaptor(FragmentManager fm) {

        super( fm );

    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Milan milan=new Milan();
                return  milan;
            case 1:
                Gaurav gaurav=new Gaurav();
                return  gaurav;
            case 2:
                Tushar tushar=new Tushar();
                return  tushar;
            case 3:
                Shubham shubham=new Shubham();
                return  shubham;

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
                return "Milan";
            case 1:
                return "Gaurav";
            case 2:
                return "Tushar";
            case 3:
                return "Shubham";

            default:
                return null ;
        }
    }


    public CardView getCardViewAt(int position) {
        switch (position) {
            case 0:
                return Milan.getCardView();
            case 1:
                return Gaurav.getCardView();
            case 2:
                return Tushar.getCardView();
            case 3:
                return Shubham.getCardView();


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public float getBaseElevation() {
        return 2;
    }


}
