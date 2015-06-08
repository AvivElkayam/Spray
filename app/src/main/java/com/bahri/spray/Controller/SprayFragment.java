package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.bahri.spray.SprayUser;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by mac on 3/29/15.
 */
public class SprayFragment extends Fragment implements LocationListener {
    LocationManager locationManager;
    TextView locationTextView,closeUsersTextView;
    Geocoder geocoder;
    String locationText = "My Location";
    Button sprayButton,scanButton;
    LinearLayout usersLinearLayout;
    ProgressBar progressBar;
    String myBSSID;
    WifiManager mainWifiObj;
    //RippleBackground rippleBackground;

//    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
//            // your implementation here
//            String s = device.getName();
//            //textView.setText("id:  "+device.getName()+"   "+device.getType());
//
//            //MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
//            if(device.getName()!=null)
//            {
//                if(!checkIfUserExistInLocalArray(Integer.parseInt(device.getName())))
//                {
//                    MyModel.discoverdUsersIDSLocalArray.add(Integer.parseInt(device.getName()));
//                    MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
//                    Log.w("myApp", "device discoverd: "+device.getName()+" "+device.getUuids());
//                    ParcelUuid[] p = device.getUuids();
//                    //textView.setText("discoverd: "+device.getName()+" "+device.getAddress()
//                   // );
//                }
//            }
//
//
//        }
//    };

    private boolean checkIfUserExistInLocalArray(Integer id)
    {
        for(Integer id1 :MyModel.discoverdUsersIDSLocalArray)
        {
            if(id1==id)
            {
                return true;
            }
        }
        return false;
    }

//    public void startScanAndTransmit()
//    {
//        UUID[] uuids = new UUID[1];
//        uuids[0] = UUID.fromString("11DA3FD1-7E10-41C1-B16F-4430B506CDE7");
//
//        mBluetoothAdapter.startLeScan(uuids,leScanCallback);
//        mBluetoothAdapter.startLeScan(leScanCallback);
//        Log.w("startedScanning","Started Scaninng");
////        new Thread() {
////            public void run() {
////                /* block of code which need to execute via thread */
////                for (int i=0;i<3;i++)
////                {
////
////                    try {
////                        MyModel.getInstance().getCloseUsers();
////                        sleep(4000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        }.start();
//    }
    public void updateCloseUsersTextView()
    {
//        if(progressBar.isShowing())
//        progressBar.dismiss();
       // rippleBackground.stopRippleAnimation();

        progressBar.setVisibility(View.GONE);
        closeUsersTextView.setText(MyModel.discoverdUsers.size() + " People around you");
        usersLinearLayout.removeAllViews();
        for(SprayUser user:MyModel.getInstance().discoverdUsers)
        {
            usersLinearLayout.addView(createUserCellInScrollView(user));
        }

    }

    public View createUserCellInScrollView(SprayUser user)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.spray_fragment_scroll_custom_view, usersLinearLayout,false);
        TextView textView = (TextView)view.findViewById(R.id.spray_scroll_view_text_view);
        ImageView imageView = (ImageView)view.findViewById(R.id.spray_scroll_view_image_view);

        textView.setText(user.getUserName());
        if(user.getImage()==null)
        {
            imageView.setBackground(getResources().getDrawable(R.drawable.group));
          //  imageView.set
            //imageView.setIma

        }
        else
        {
            imageView.setBackground(new BitmapDrawable(user.getImage()));

        }
        return view;
    }
    private ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Spray");
        return inflater.inflate(R.layout.spray_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initWifiManger();
        initLocation();
        initViews();

        //MyModel.getInstance().deleteRelations();
        //textView = (TextView)getActivity().findViewById(R.id.testBTtextView);
       // initBluetooth();

        getActivity().setTitleColor(Color.WHITE);

        Log.w("myApp", "started scanning");

       // getCloseUsersByGPS();
    }

    public void initViews()
    {
        locationTextView = (TextView) getActivity().findViewById(R.id.location_text_view_id);
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = getLastKnownLocation();
        if(lastKnownLocation!=null) {
            updateLocationTextView(lastKnownLocation);
            MyModel.getInstance().updateLocation(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

        }
        //rippleBackground = (RippleBackground)getActivity().findViewById(R.id.cool_progress_bar);
        usersLinearLayout = (LinearLayout)getActivity().findViewById(R.id.spray_users_linear_layout);
        progressBar = (ProgressBar)getActivity().findViewById(R.id.spray_progress_bar);
        closeUsersTextView = (TextView)getActivity().findViewById(R.id.close_users_text_view);
        closeUsersTextView.setText(MyModel.discoverdUsers.size() + " People around you");
        sprayButton = (Button)getActivity().findViewById(R.id.spray_to_button);
        sprayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SprayToActivity.class);
                startActivity(intent);

               // mBluetoothAdapter.stopLeScan(leScanCallback);
            }
        });
        scanButton = (Button)getActivity().findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersLinearLayout.removeAllViews();
                MyModel.discoverdUsers.clear();
//                //getUsersConnectedToMyWifi();
                //getCloseUsersByGPS();
                getCloseUsersByBluetooth();


            }
        });
        Button cellularCell = (Button)getActivity().findViewById(R.id.cellular_cell_button);
        cellularCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CellularCellActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    private void initWifiManger()
    {      mainWifiObj = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);


//        WifiScanReceiver wifiReciever = new WifiScanReceiver();
        // registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //  List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
        String data2 = mainWifiObj.getConnectionInfo().getBSSID();

        WifiInfo d = mainWifiObj.getConnectionInfo();
        myBSSID = d.getBSSID();

        if(myBSSID==null) {
            MyModel.getInstance().updateWifi("nowifi");
        }
        else
        {
            MyModel.getInstance().updateWifi(myBSSID);
        }
//        String d2 =s2  ;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initWifiConnectionEvents(intent);
            }
        }, intentFilter);}
    private void initWifiConnectionEvents(Intent intent)
    {
        final String action = intent.getAction();
//        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
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
    private void getUsersConnectedToMyWifi()
    {
        //ImageView imageView=(ImageView)getActivity().findViewById(R.id.centerImage);

                //rippleBackground.startRippleAnimation();



        if(myBSSID!=null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.scan_animation));
//                progressBar.setMessage("Scanning for close users...");
//                progressBar.show();
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.scan_animation));
            MyModel.getInstance().getCloseUsersConectedToSameWifi(myBSSID);
        }

    }
    private void getCloseUsersByGPS()
    {
        //MyModel.getInstance().getCloseUsers();
       // MyModel.discoverdUsers.clear();
        MyModel.getInstance().getCloseUsersByGPS();
        //MyModel.getInstance().getCloseUsersConectedToSameWifi(myBSSID);
//                progressBar = new ProgressDialog(getActivity());
//                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.scan_animation));
//                progressBar = ProgressDialog.show(getActivity(), "","Scanning for close users...", true);


        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.scan_animation));
//                progressBar.setMessage("Scanning for close users...");
//                progressBar.show();
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.scan_animation));

    }
    public void initLocation()
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       // updateLocationTextView(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

    }
    public SprayFragment(){}
    @Override
    public void onLocationChanged(Location location) {

       // Log.w("ff","location has been updated");
        updateLocationTextView(location);
        MyModel.getInstance().updateLocation(location.getLatitude(),location.getLongitude());
    }
    public void updateLocationTextView(Location location)
    {
        geocoder = new Geocoder(getActivity().getApplicationContext());
        List<Address> addresses = null;
        try {
            {
                if(location!=null) {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(addresses!=null)
                locationText =
                        addresses.get(0).getThoroughfare() + ", "
                                + addresses.get(0).getLocality();
                locationTextView.setText(locationText);

    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
       Log.d("Latitude","status");
    }

    @Override
    public void onResume() {
        super.onResume();
        //mBluetoothAdapter.startLeScan(leScanCallback);
        Log.w("s","OnnnnnnnResume");
    }
    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    private void getCloseUsersByBluetooth()
    {
       // MyModel.discoverdUsers.clear();
        if (!((MainTabActivity)getActivity()).getmBluetoothAdapter().isEnabled()) {
            ((MainTabActivity)getActivity()).requestBluetoothEnable();
        }
        else
        {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.scan_animation));
//                progressBar.setMessage("Scanning for close users...");
//                progressBar.show();
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.scan_animation));
//            MyModel.discoverdUsers.clear();
//            usersLinearLayout.removeAllViews();
//            MyModel.getInstance().getCloseUserByBluetooth();
            //l
            ((MainTabActivity)getActivity()).startDiscovery();
        }
    }

}
