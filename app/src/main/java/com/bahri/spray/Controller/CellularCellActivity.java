package com.bahri.spray.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bahri.spray.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CellularCellActivity extends ActionBarActivity {
    TextView textView1,textView2,textView3,textView4;
    int myLatitude, myLongitude;
    boolean bool;

    String IMEINumber;
    String subscriberID;
    String SIMSerialNumber;
    String networkCountryISO;
    String SIMCountryISO;
    String softwareVersion;
    String voiceMailNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellular_cell);
        textView1 = (TextView)findViewById(R.id.celluar_cell_text_view1);
        textView2 = (TextView)findViewById(R.id.celluar_cell_text_view2);
        textView3 = (TextView)findViewById(R.id.celluar_cell_text_view3);
        textView4 = (TextView)findViewById(R.id.celluar_cell_text_view4);
        //Get the instance of TelephonyManager
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        //Calling the methods of TelephonyManager the returns the information
        IMEINumber=tm.getDeviceId();
        subscriberID=tm.getDeviceId();
        SIMSerialNumber=tm.getSimSerialNumber();
        networkCountryISO=tm.getNetworkCountryIso();
        SIMCountryISO=tm.getSimCountryIso();
        softwareVersion=tm.getDeviceSoftwareVersion();
        voiceMailNumber=tm.getVoiceMailNumber();
        CellLocation location = tm.getCellLocation();

       textView1.setText(location.toString());
        List<NeighboringCellInfo> list = tm.getNeighboringCellInfo();
        for(NeighboringCellInfo cell:list)
        {
            //cell.get
        }
        RqsLocation();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cellular_cell, menu);
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
    private Boolean RqsLocation(){
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
       final GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        int cid1 = cellLocation.getCid();
        int lac2 = cellLocation.getLac();
            new AsyncTask<Integer,Integer,String>()
            {
                @Override
                protected String doInBackground(Integer... params) {
                    //Boolean result = false;

                    String urlmmap = "http://www.google.com/glm/mmap";

                    try {
                        URL url = new URL(urlmmap);
                        URLConnection conn = url.openConnection();
                        HttpURLConnection httpConn = (HttpURLConnection) conn;
                        httpConn.setRequestMethod("POST");
                        httpConn.setDoOutput(true);
                        httpConn.setDoInput(true);
                        httpConn.connect();

                        OutputStream outputStream = httpConn.getOutputStream();
                        WriteData(outputStream, params[0], params[1]);

                        InputStream inputStream = httpConn.getInputStream();
                        DataInputStream dataInputStream = new DataInputStream(inputStream);

                        dataInputStream.readShort();
                        dataInputStream.readByte();
                        int code = dataInputStream.readInt();
                        if (code == 0) {
                            myLatitude = dataInputStream.readInt();
                            myLongitude = dataInputStream.readInt();

                            bool = true;
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    textView1.setText(cellLocation.toString());
                   // textView2.setText("gsm cell id: " + String.valueOf(cid));
                    //textView3.setText("gsm location area code: " + String.valueOf(lac));

                    if(bool){
                        textView4.setText(
                                String.valueOf((float)myLatitude/1000000)
                                        + " : "
                                        + String.valueOf((float)myLongitude/1000000));
                    }else{
                        textView4.setText("Can't find Location!");
                    }
                }
            }.execute(cid1,lac2);
//        Boolean result = false;
//
//        String urlmmap = "http://www.google.com/glm/mmap";
//
//        try {
//            URL url = new URL(urlmmap);
//            URLConnection conn = url.openConnection();
//            HttpURLConnection httpConn = (HttpURLConnection) conn;
//            httpConn.setRequestMethod("POST");
//            httpConn.setDoOutput(true);
//            httpConn.setDoInput(true);
//            httpConn.connect();
//
//            OutputStream outputStream = httpConn.getOutputStream();
//            WriteData(outputStream, cid, lac);
//
//            InputStream inputStream = httpConn.getInputStream();
//            DataInputStream dataInputStream = new DataInputStream(inputStream);
//
//            dataInputStream.readShort();
//            dataInputStream.readByte();
//            int code = dataInputStream.readInt();
//            if (code == 0) {
//                myLatitude = dataInputStream.readInt();
//                myLongitude = dataInputStream.readInt();
//
//                result = true;

//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        return false;

    }
    private void cellular()
    {



    }
    private void WriteData(OutputStream out, int cid, int lac)
            throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        dataOutputStream.writeShort(21);
        dataOutputStream.writeLong(0);
        dataOutputStream.writeUTF("en");
        dataOutputStream.writeUTF("Android");
        dataOutputStream.writeUTF("1.0");
        dataOutputStream.writeUTF("Web");
        dataOutputStream.writeByte(27);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(3);
        dataOutputStream.writeUTF("");

        dataOutputStream.writeInt(cid);
        dataOutputStream.writeInt(lac);

        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.flush();
    }

}
