package com.bahri.spray.Controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

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
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeAdvertiser mLeAdvertiser;
    BluetoothDevice device;

    ProgressDialog progressDialog;
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            // your implementation here
            String s = device.getName();
            //textView.setText("id:  "+device.getName()+"   "+device.getType());

            //MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
            if(device.getName()!=null)
            {
                if(!checkIfUserExistInLocalArray(Integer.parseInt(device.getName())))
                {
                    MyModel.discoverdUsersIDSLocalArray.add(Integer.parseInt(device.getName()));
                    MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
                    Log.w("myApp", "device discoverd: "+device.getName()+" "+device.getUuids());
                    ParcelUuid[] p = device.getUuids();
                    //textView.setText("discoverd: "+device.getName()+" "+device.getAddress()
                   // );
                }
            }


        }
    };
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
    public  void display(){
        Toast.makeText(getActivity(),"device discoverd: "+device.getName(),Toast.LENGTH_SHORT).show();

    }
//    private void startAdvertising() {
//        ParcelUuid mAdvParcelUUID = ParcelUuid.fromString("0000FEFF-0000-1000-8000-00805F9B34FB");
//
//        mLeAdvertiser = (BluetoothLeAdvertiser)((BluetoothAdapter)((BluetoothManager)getActivity().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter()).getBluetoothLeAdvertiser();
//        if (mLeAdvertiser == null)
//        {
//            Log.e("startAdvertising", "didn't get a bluetooth le advertiser");
//            return;
//        }
//
//        AdvertiseSettings.Builder mLeAdvSettingsBuilder =
//                new AdvertiseSettings.Builder().setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
//        mLeAdvSettingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
//        mLeAdvSettingsBuilder.setConnectable(false);
//        AdvertiseData.Builder mLeAdvDataBuilder = new AdvertiseData.Builder();
//
//        List<ParcelUuid> myUUIDs = new ArrayList<ParcelUuid>();
//        myUUIDs.add(ParcelUuid.fromString("0000FE00-0000-1000-8000-00805F9B34FB"));
//        byte mServiceData[] = { (byte)0xff, (byte)0xfe, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04 };
//        mLeAdvDataBuilder.addServiceData(mAdvParcelUUID, mServiceData);
//
//        AdvertiseSettings.Builder advSetBuilder = new AdvertiseSettings.Builder();
//        advSetBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
//        advSetBuilder.setConnectable(false);
//        advSetBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
//        advSetBuilder.setTimeout(10000);
//        Log.d("advBuild", "settings:" + advSetBuilder.build());
//
//        AdvertiseData.Builder advDataBuilder = new AdvertiseData.Builder();
//        advDataBuilder.setIncludeDeviceName(false);
//        advDataBuilder.setIncludeTxPowerLevel(true);
//        advDataBuilder.addServiceData(mAdvParcelUUID, mServiceData);
//        mLeAdvertiser.startAdvertising(mLeAdvSettingsBuilder.build(), mLeAdvDataBuilder.build(), mLeAdvCallback);
//    }
//
//    /**
//     * Stop Advertisements
//     */
//    public void stopAdvertisements() {
//        if (mLeAdvertiser != null) {
//            mLeAdvertiser.stopAdvertising(mLeAdvCallback);
//        }
//    }
//
//    private final AdvertiseCallback mLeAdvCallback = new AdvertiseCallback() {
//        public void onStartSuccess (AdvertiseSettings settingsInEffect) {
//            Log.d("AdvertiseCallback", "onStartSuccess:" + settingsInEffect);
//        }
//
//        public void onStartFailure(int errorCode) {
//            String description = "";
//            if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED) description = "ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
//            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) description = "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
//            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED) description = "ADVERTISE_FAILED_ALREADY_STARTED";
//            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE) description = "ADVERTISE_FAILED_DATA_TOO_LARGE";
//            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR) description = "ADVERTISE_FAILED_INTERNAL_ERROR";
//            else description = "unknown";
//            Log.e("AdvertiseCB", "onFailure error:" + errorCode + " " + description);
//        }
//    };
    public void startScanAndTransmit()
    {
        UUID[] uuids = new UUID[1];
        uuids[0] = UUID.fromString("11DA3FD1-7E10-41C1-B16F-4430B506CDE7");

        mBluetoothAdapter.startLeScan(uuids,leScanCallback);
        mBluetoothAdapter.startLeScan(leScanCallback);
        Log.w("startedScanning","Started Scaninng");
//        new Thread() {
//            public void run() {
//                /* block of code which need to execute via thread */
//                for (int i=0;i<3;i++)
//                {
//
//                    try {
//                        MyModel.getInstance().getCloseUsers();
//                        sleep(4000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }
    public void updateCloseUsersTextView()
    {
        if(progressDialog.isShowing())
        progressDialog.dismiss();
        closeUsersTextView.setText(MyModel.discoverdUsers.size() + " People around you");


    }
    private ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        getActivity().setTitle("Spray");
        return inflater.inflate(R.layout.spray_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLocation();
        initViews();
        //MyModel.getInstance().deleteRelations();
        //textView = (TextView)getActivity().findViewById(R.id.testBTtextView);
       // initBluetooth();

        getActivity().setTitleColor(Color.WHITE);

        Log.w("myApp", "started scanning");


    }
    public void initBluetooth()
    {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

// Ensures Bluetooth is available on the device and it is enabled. If not,
// displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);


        }
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
        closeUsersTextView = (TextView)getActivity().findViewById(R.id.close_users_text_view);
        closeUsersTextView.setText(MyModel.discoverdUsersIDSLocalArray.size() + " People around you");
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
                //MyModel.getInstance().getCloseUsers();
                MyModel.discoverdUsers.clear();
                MyModel.getInstance().getCloseUsersByGPS();
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.scan_animation));
//                progressDialog = ProgressDialog.show(getActivity(), "","Scanning for close users...", true);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.scan_animation));
                progressDialog.setMessage("Scanning for close users...");
                progressDialog.show();
            }
        });

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

        Log.w("ff","location has been updated");
        updateLocationTextView(location);
        MyModel.getInstance().updateLocation(location.getLatitude(),location.getLongitude());
    }
    public void updateLocationTextView(Location location)
    {
        geocoder = new Geocoder(getActivity().getApplicationContext());
        List<Address> addresses = null;
        try {
            {
                if(location!=null)
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(addresses!=null)
                locationText =
                        addresses.get(0).getThoroughfare() + " "
                                + addresses.get(0).getSubThoroughfare() + ", "
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
}
