package com.bahri.spray.Controller;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

public class MainTabActivity extends ActionBarActivity {

    private FragmentTabHost mTabHost;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyModel.getInstance().setMainTabActivity(this);
        setContentView(R.layout.activity_tab_test_activity);
        initTabs();
        actionBar=getSupportActionBar();
//        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//        TextView abTitle = (TextView) findViewById(titleId);
//        abTitle.setTextColor(Color.WHITE);
        actionBar.setDisplayHomeAsUpEnabled(true);
        chageActionBarStyle();
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_background));




    }
    public void chageActionBarStyle()
    {
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.toString()
                .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("",getResources().getDrawable(R.drawable.media)),
                MediaFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("",getResources().getDrawable(R.drawable.group)),
                GroupFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("",getResources().getDrawable(R.drawable.settings)),
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
}
