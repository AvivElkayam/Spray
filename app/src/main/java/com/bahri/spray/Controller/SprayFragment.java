package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.Model.ParseModel;
import com.bahri.spray.R;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mac on 3/29/15.
 */
public class SprayFragment extends Fragment implements LocationListener {
    LocationManager locationManager;
    TextView locationTextView;
    Geocoder geocoder;
    Button sprayButton;
    BluetoothAdapter mBluetoothAdapter;
    ArrayList<BluetoothDevice> discoverdDevices;
    TextView textView;
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            // your implementation here
            String s = device.getName();
            textView.setText("id:  "+device.getName()+"   "+device.getType());

            // MyModel.getInstance().updateRelationsInServer(Integer.parseInt(device.getName()));
            Log.w("myApp", "device discoverd: "+device.getName());

        }
    };
    private ActionBar actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Spray");

        return inflater.inflate(R.layout.spray_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
       // initLocation();
        textView = (TextView)getActivity().findViewById(R.id.testBTtextView);
        initBluetooth();

        mBluetoothAdapter.startLeScan(leScanCallback);
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
        sprayButton = (Button)getActivity().findViewById(R.id.spray_to_button);
        sprayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SprayToActivity.class);
                startActivity(intent);
                // Create new fragment and transaction
                // Create new fragment and transaction
//                Fragment newFragment = new SprayToFragment();
//                // consider using Java coding conventions (upper first char class names!!!)
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.pager, newFragment);
//                transaction.addToBackStack("replaceSpray");
//
//                // Commit the transaction
//                transaction.commit();
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


}
