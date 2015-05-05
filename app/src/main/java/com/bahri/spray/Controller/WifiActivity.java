package com.bahri.spray.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

import java.util.List;

public class WifiActivity extends ActionBarActivity {
    String myBSSID;
    WifiManager mainWifiObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        class WifiScanReceiver extends BroadcastReceiver {
            public void onReceive(Context c, Intent intent) {
            }
        }
//        WifiScanReceiver wifiReciever = new WifiScanReceiver();
       // registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
      //  List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
        String data2 = mainWifiObj.getConnectionInfo().getBSSID();

        WifiInfo d = mainWifiObj.getConnectionInfo();
        myBSSID = d.getBSSID();
        MyModel.getInstance().updateWifi(myBSSID);
//        String d2 =s2  ;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            initWifiConnectionEvents(intent);
            }
        }, intentFilter);
    }

private void initWifiConnectionEvents(Intent intent)
{
    final String action = intent.getAction();
    if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
        if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
            //do stuff        WifiInfo d = mainWifiObj.getConnectionInfo();
            WifiInfo d = mainWifiObj.getConnectionInfo();
            myBSSID = d.getBSSID();
            MyModel.getInstance().updateWifi(myBSSID);
        } else {
            // wifi connection was lost
            MyModel.getInstance().updateWifi("nowifi");

        }
    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi, menu);
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
