package com.bahri.spray.Controller;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

public class Welcome extends FragmentActivity {

    Button logout;
    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"),
                SettingsFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab2"),
//                Tab2Fragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Tab3"),
//                Tab3Fragment.class, null);




//        // Locate Button in welcome.xml
//        logout = (Button) findViewById(R.id.logout);
//
//        // Logout Button Click Listener
//        logout.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                MyModel.getInstance().LogOutFromSpray();
//                Toast.makeText(getApplicationContext(),
//                        "Successfully Logged out",
//                        Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Welcome.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//
//
//
//
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
