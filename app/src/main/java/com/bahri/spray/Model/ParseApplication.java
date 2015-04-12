package com.bahri.spray.Model;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();




        // Add your initialization code here
        //Parse.initialize(this,"k9KGqEYPv4nMyVSXg84Vc6huhuGUeVJ6B0ngD369", "DktWRLudyfxfK9w1Dc2ykBeYLe32Yiv5Qi7zzECy");
        Parse.initialize(this, "578bGY5Q0u0oXpJIio0p51op4DjSyeRbpdIjoe5l", "d3PDJ6XeGS0tTUBY7HsdV8sJLXVIuenTc8OQTSih");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}