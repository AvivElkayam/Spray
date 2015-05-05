package com.bahri.spray.Controller;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bahri.spray.WifiHotSpot.ClientScanResult;
import com.bahri.spray.WifiHotSpot.FinishScanListener;
import com.bahri.spray.WifiHotSpot.WifiApManager;

import com.bahri.spray.R;

public class HotSpotActivity extends ActionBarActivity {
    TextView textView1;
    WifiApManager wifiApManager;
    WifiManager wifi;
    Method getWifiConfig;
    WifiConfiguration wifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_spot);

        textView1 = (TextView) findViewById(R.id.textView1);
        //
        wifiApManager = new WifiApManager(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        try {
            getWifiConfig = wifi.getClass().getMethod("getWifiApConfiguration",null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        try {
            wifiConfiguration = (WifiConfiguration) getWifiConfig.invoke(wifi,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //       String s = wifiConfiguration.SSID.toString();
        wifiConfiguration.SSID ="Spray" ;

        wifiConfiguration.wepKeys[0] = "\"1234\"";
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        wifiApManager.setWifiApEnabled(wifiConfiguration, true);


        scan();

    }

    private void scan() {
        wifiApManager.getClientList(false, new FinishScanListener() {

            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {

                textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");
                textView1.append("Clients: \n");
                for (ClientScanResult clientScanResult : clients) {
                    textView1.append("####################\n");
                    textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
                    textView1.append("Device: " + clientScanResult.getDevice() + "\n");
                    textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
                    textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Get Clients");
        menu.add(0, 1, 0, "Open AP");
        menu.add(0, 2, 0, "Close AP");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected (int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                scan();
                break;
            case 1:
                wifiApManager.setWifiApEnabled(null, true);
                break;
            case 2:
                wifiApManager.setWifiApEnabled(null, false);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}