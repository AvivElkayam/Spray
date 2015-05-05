package com.bahri.spray.Model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();



        // Add your initialization code here
        //Parse.initialize(this,"k9KGqEYPv4nMyVSXg84Vc6huhuGUeVJ6B0ngD369", "DktWRLudyfxfK9w1Dc2ykBeYLe32Yiv5Qi7zzECy");

            Parse.initialize(this, "k9KGqEYPv4nMyVSXg84Vc6huhuGUeVJ6B0ngD369", "DktWRLudyfxfK9w1Dc2ykBeYLe32Yiv5Qi7zzECy");
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            if(ParseUser.getCurrentUser()!=null)
            installation.put("user", ParseUser.getCurrentUser());
            installation.saveInBackground();
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();
            subscribeInBackGround();
            // If you would like all objects to be private by default, remove this
            // line.
            defaultACL.setPublicReadAccess(true);

            ParseACL.setDefaultACL(defaultACL, true);

    }

    private void subscribeInBackGround() {
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        ParsePush.subscribeInBackground("spray", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}