package com.bahri.spray.Controller;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import com.bahri.spray.SprayUser;

import java.util.ArrayList;

public class MainTabActivity extends ActionBarActivity {

    private FragmentTabHost mTabHost;
    private ActionBar actionBar;
    BluetoothAdapter mBluetoothAdapter;
    ArrayList<String> macAddressList;
    Handler mHandler = new Handler();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //MyModel.getInstance().getCloseUsersByBluetooth(device.getAddress());
                Log.w("disocer", "discoverd " + device.getName());
                MyModel.getInstance().getBTUserDetails(device.getAddress(), new MyModel.GetSprayUserCallback() {
                    @Override
                    public void done(SprayUser user) {
                        if (user != null){
                            user.setProvider(SprayUser.PROVIDER_BT);
                            MyModel.getInstance().addCloseUser(user);
                            Log.w("disocer", "discoverd user" + user.getUserName());
                        }
                        callback.deviceFound(user);
                    }
                });
            }
        }
    };

    public void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        MyModel.getInstance().updateBluetoothMACAddress(mBluetoothAdapter.getAddress());
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            // (If your BTDiscoverable is this equivalent if this, you can use it)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        } else {
            /*
             * As the bluetooth is now on and the device can be connected/discovered, USER MUST START THE SERVER SITE ON DEVICE BY
             * CREATING NEW THREAD
             */
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.left_out_animation, R.anim.right__out_animation);
        macAddressList = new ArrayList<String>();
        Log.w("S", "main tab on create");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));
        MyModel.getInstance().setMainTabActivity(this);
        setContentView(R.layout.activity_tab_test_activity);
        initTabs();
        actionBar = getSupportActionBar();
        setTitleColor(Color.WHITE);
        initBluetooth();


    }

    public void chageActionBarStyle() {
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.toString()
                .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setTitle(spannableString);
    }

    //    public void startScanAndTransmit()
//    {
//        ( (SprayFragment)getSupportFragmentManager().findFragmentByTag("SprayFragment")).startScanAndTransmit();
//    }
    public void updateCloseUsersTextView() {
        ((SprayFragment) getSupportFragmentManager().findFragmentByTag("SprayFragment")).updateCloseUsersTextView();
    }

    private void initTabs() {

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost1);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        mTabHost.addTab(mTabHost.newTabSpec("SprayFragment").setIndicator("", getResources().getDrawable(R.drawable.spray)),
                SprayFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("MediaFragment").setIndicator("", getResources().getDrawable(R.drawable.media)),
                MediaFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("GroupsFragment").setIndicator("", getResources().getDrawable(R.drawable.group)),
                GroupFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("SettingsFragment").setIndicator("", getResources().getDrawable(R.drawable.settings)),
                SettingsFragment.class, null);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#E0E0E0")); //unselected
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_tab));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
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

    public void upateWifiBSSID(String s) {
        MyModel.getInstance().updateWifi(s);
    }

    final static int REQUEST_ENABLE_BT = 3;
    public void requestBluetoothEnable() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                mBluetoothAdapter.startDiscovery();
            }
        }
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public interface ScanCallback{
        public void deviceFound(SprayUser user);
    }

    ScanCallback callback;
    public void startDiscovery(ScanCallback callback) {
        this.callback = callback;
        if (!getmBluetoothAdapter().isEnabled()) {
            requestBluetoothEnable();
        } else {
            mBluetoothAdapter.startDiscovery();
        }
    }
}
