package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.Model.ParseModel;
import com.bahri.spray.R;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by mac on 3/29/15.
 */
public class SprayFragment extends Fragment implements LocationListener {
    LocationManager locationManager;
    TextView locationTextView,closeUsersTextView;
    Geocoder geocoder;
    Button sprayButton,scanButton;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    TextView textView;

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            // your implementation here
            String s = device.getName();
            //textView.setText("id:  "+device.getName()+"   "+device.getType());

            //MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
            if(device.getName()!=null)
            MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
            Log.w("myApp", "device discoverd: "+device.getName()+" "+device.getUuids());
            ParcelUuid[] p = device.getUuids();
            textView.setText("discoverd: "+device.getName()+" "+device.getAddress()
            );

        }
    };
    public  void display(){
        Toast.makeText(getActivity(),"device discoverd: "+device.getName(),Toast.LENGTH_SHORT).show();

    }
    public void startScanAndTransmit()
    {
        UUID[] uuids = new UUID[1];
        uuids[0] = UUID.fromString("11DA3FD1-7E10-41C1-B16F-4430B506CDE7");

        mBluetoothAdapter.startLeScan(uuids,leScanCallback);
        //mBluetoothAdapter.startLeScan(leScanCallback);
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
        closeUsersTextView.setText(MyModel.discoverdUsers.size() + " People around you");
    }
    private ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitleColor(Color.WHITE);

        return inflater.inflate(R.layout.spray_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initLocation();
        MyModel.getInstance().deleteRelations();
        textView = (TextView)getActivity().findViewById(R.id.testBTtextView);
        initBluetooth();
        getActivity().setTitleColor(Color.WHITE);
        getActivity().setTitle("Spray");
       // MyModel.getInstance().updateRelationsInServer(77);
        //getActivity().getActionBar().getCustomView().setBackground(getResources().getDrawable(R.drawable.orange_background));


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
        closeUsersTextView = (TextView)getActivity().findViewById(R.id.close_users_text_view);
        sprayButton = (Button)getActivity().findViewById(R.id.spray_to_button);
        sprayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SprayToActivity.class);
                startActivity(intent);
                mBluetoothAdapter.stopLeScan(leScanCallback);
            }
        });
        scanButton = (Button)getActivity().findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyModel.getInstance().getCloseUsers();
            }
        });

    }
    public void initLocation()
    {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       // getLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

    }
    public SprayFragment(){}
    @Override
    public void onLocationChanged(Location location) {

        getLocation(location);
    }
    public void getLocation(Location location)
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

                locationTextView.setText(
                        addresses.get(0).getThoroughfare() + " "
                                + addresses.get(0).getSubThoroughfare() + ", "
                                + addresses.get(0).getLocality());

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
        mBluetoothAdapter.startLeScan(leScanCallback);
    }
}
