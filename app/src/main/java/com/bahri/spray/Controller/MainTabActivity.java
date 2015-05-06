package com.bahri.spray.Controller;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

import java.util.ArrayList;

public class MainTabActivity extends ActionBarActivity {

    private FragmentTabHost mTabHost;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.left_out_animation, R.anim.right__out_animation);
        Log.w("S","main tab on create");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));
        MyModel.getInstance().setMainTabActivity(this);
        setContentView(R.layout.activity_tab_test_activity);
        initTabs();
        actionBar=getSupportActionBar();
        setTitleColor(Color.WHITE);
//
      //  Log.i("Fragment manger size", "zzz" + getSupportFragmentManager().getFragments().size());



    }

//    protected void onResume(){
//
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
//
//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            if ("text/plain".equals(type)) {
//                handleSendText(intent); // Handle text being sent
//            } else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
//            }
//        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
//            }}
//
//
//
//    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            mTabHost.setCurrentTabByTag("MediaFragment");

            SprayImageFragment fragment = (SprayImageFragment) this.getSupportFragmentManager().findFragmentByTag("Image");
            //    SprayImageFragment sprayImageFragment = (SprayImageFragment) getSupportFragmentManager().findFragmentById(R.id.SprayImageFragmentLayout);
            //   SprayImageFragment fragment = (SprayImageFragment)


            fragment.setImageFromUri(imageUri);
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    public void chageActionBarStyle()
    {
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.toString()
                .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setTitle(spannableString);
    }
    public void startScanAndTransmit()
    {
        ( (SprayFragment)getSupportFragmentManager().findFragmentByTag("SprayFragment")).startScanAndTransmit();
    }
    public void updateCloseUsersTextView()
    {
        ( (SprayFragment)getSupportFragmentManager().findFragmentByTag("SprayFragment")).updateCloseUsersTextView();
    }
    private void initTabs()
    {

        mTabHost = (FragmentTabHost)findViewById(R.id.tabhost1);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        mTabHost.addTab(mTabHost.newTabSpec("SprayFragment").setIndicator("", getResources().getDrawable(R.drawable.spray)),
                SprayFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("MediaFragment").setIndicator("",getResources().getDrawable(R.drawable.media)),
                MediaFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("GroupFragment").setIndicator("",getResources().getDrawable(R.drawable.group)),
                GroupFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("SettingsFragment").setIndicator("",getResources().getDrawable(R.drawable.settings)),
                SettingsFragment.class, null);

        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#E0E0E0")); //unselected
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_tab));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
                {
                    mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#E0E0E0")); //unselected
                }
                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_tab));

            }
        });
       // mTabHost.setCurrentTab(getIntent().getIntExtra("tab",0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_test_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void upateWifiBSSID(String s)
    {
        MyModel.getInstance().updateWifi(s);
    }
}
