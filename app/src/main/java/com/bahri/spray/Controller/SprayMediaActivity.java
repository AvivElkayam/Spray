package com.bahri.spray.Controller;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.bahri.spray.R;

public class SprayMediaActivity extends ActionBarActivity {
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));
        setContentView(R.layout.activity_spray_media_layout);
        initTabs();
    }

    private void initTabs()
    {
        setTitle("Spray To All");
        mTabHost = (FragmentTabHost)findViewById(R.id.spray_to_all_tab_host);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.spray_to_all_tabs_container);
        mTabHost.addTab(mTabHost.newTabSpec("Image").setIndicator("Image"),
                SprayImageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("File").setIndicator("File"),
                SprayFileFragment.class, null);
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
        //return mTabHost;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spray_to_all, menu);
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
