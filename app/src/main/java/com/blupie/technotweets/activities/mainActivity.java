package com.blupie.technotweets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebBackForwardList;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blupie.technotweets.adapters.sectionAdapter;
import com.blupie.technotweets.R;
import com.blupie.technotweets.models.AppRater;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mainActivity extends AppCompatActivity {

    public sectionAdapter msectionAdapter;


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    public static ViewPager pager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static RewardedVideoAd mRewardedVideoAd;
    public static InterstitialAd mInterstitialAd;
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
            pager = (ViewPager)findViewById(R.id.mpager);
            MobileAds.initialize(getApplicationContext(),"ca-app-pub-2028698845219479~3499109503");
            msectionAdapter = new sectionAdapter(getSupportFragmentManager());
            pager.setAdapter(msectionAdapter);
            tablayout.setupWithViewPager(pager);
            tablayout.getTabAt(0).setIcon(tabIcons);
            LinearLayout layout = ((LinearLayout) ((LinearLayout) tablayout.getChildAt(0)).getChildAt(0));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
            layoutParams.weight = 0.5f;

            layout.setLayoutParams(layoutParams);
            if (getIntent().getIntExtra("notify",2)==123)
            {
                tablayout.getTabAt(2).select();
            }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2028698845219479/2538403123");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        AppRater.app_launched(this);
        loadRewardedVideoAd();

        final SharedPreferences pref=this.getSharedPreferences("admob",0);
        int a=pref.getInt("admoblaunch",0);
        if(a>=6)
        {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.

                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.

                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    pref.edit().putInt("admoblaunch",0).apply();
                }
            });
        }
        else
        {
            pref.edit().putInt("admoblaunch",a+1).apply();

        }
    }
    private boolean isNetworkConnected(mainActivity mainActivity) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public static void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd("ca-app-pub-2028698845219479/2137688698",
                    new AdRequest.Builder().build());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threebutton,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set: {
                Intent intent = new Intent(mainActivity.this, about_us.class);
                startActivity(intent);
                break;
            }
            case R.id.action_logout: {
                {
                    if (isNetworkConnected(this)) {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();

                        } else {
                            // Toast.makeText(this, "not", Toast.LENGTH_SHORT).show();
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            open();
                        }
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                // Code to be executed when an ad request fails.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                open();
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                open();
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when when the interstitial ad is closed.
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                open();
                            }
                        });
                    } else {

                        open();
                    }
                    break;
                }
            }
                    case R.id.action_share: {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "https://play.google.com/store/apps/details?id=com.blupie.technotweets";
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    }
                }

            return true;
        }
        public  void open()
        {
            loadRewardedVideoAd();
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
                    prefs.edit().putInt("persistent",1).apply();
                    mRewardedVideoAd.show();
                    Intent intent = new Intent(mainActivity.this,StartActivity.class);
                    startActivity(intent);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();

        }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
        prefs.edit().putInt("flag",1).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        int pid=android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
        SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
        prefs.edit().putInt("persistent", 1).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
        prefs.edit().putInt("flag",0).apply();
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
