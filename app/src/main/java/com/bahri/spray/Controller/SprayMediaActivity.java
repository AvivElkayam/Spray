package com.bahri.spray.Controller;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.bahri.spray.R;

import java.util.ArrayList;

public class SprayMediaActivity extends ActionBarActivity {
    FragmentTabHost mTabHost;
    private ArrayList<String> chosenUsersIDs;

    public ArrayList<String> getChosenUsersIDs() {
        return chosenUsersIDs;
    }

    public void setChosenUsersIDs(ArrayList<String> chosenUsersIDs) {
        this.chosenUsersIDs = chosenUsersIDs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        setContentView(R.layout.activity_spray_media_layout);
        chosenUsersIDs = getIntent().getStringArrayListExtra("usersIndex");
        initTabs();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }}
        Log.i("Fragment manger size", "zzz" + getSupportFragmentManager().getFragments().size());
    }

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


        Log.i("Fragment manger size", "zzz" + getSupportFragmentManager().getFragments().size());


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
