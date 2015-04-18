package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bahri.spray.R;

import java.util.ArrayList;

/**
 * Created by mac on 4/1/15.
 */
public class MediaFragment extends Fragment {

    TextView textView;
    ViewPager viewPager;
    FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Media");

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.media_tab_host);
        mTabHost.addTab(mTabHost.newTabSpec("Current").setIndicator("Current"),
                CurrentMediaFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Stored").setIndicator("Stored"),
                StoredMediaFragment.class, null);
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


        return mTabHost;


    }
        //return  inflater.inflate(R.layout.fragment_media_layout, container, false);    }
        public void changeActionBarStyle()
        {
            ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
            SpannableString spannableString = new SpannableString("Media");
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.toString()
                    .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //actionBar.setDisplayShowHomeEnabled(true);
            // getSupportActionBar().setIcon(R.drawable.ic_launcher);
            actionBar.setTitle(spannableString);

        }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Media");


    }
    private View initTabs()
    {
        mTabHost = (FragmentTabHost)getActivity().findViewById(R.id.media_tab_host);
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.media_tabs_container);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("",getResources().getDrawable(R.drawable.spray)),
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
        return mTabHost;
    }


    @Override
    public void onResume() {
        super.onResume();
//        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
//        {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivity(enableIntent);
//            getActivity().finish();
//            return;
//        }
//
//        if(!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE));
//        {
//            Toast.makeText(getActivity(),"No le Support",Toast.LENGTH_SHORT).show();
//            getActivity().finish();
//            return;
        //}
    }
    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    // Top Rated fragment activity
                    return new StoredMediaFragment();
                case 1:
                    // Games fragment activity
                    return new CurrentMediaFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 2;
        }

    }
}
