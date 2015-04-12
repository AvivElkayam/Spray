package com.bahri.spray.Model;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.Toast;

import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.SignUpActivity;
import com.bahri.spray.Controller.Welcome;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by user on 19/03/2015.
 */
public class ParseModel implements MyModel.ModelInterface {
    SignUpActivity signUpActivity;
    LoginActivity loginActivity;
    String address;
    BluetoothAdapter mBluetoothAdapter;
    boolean success;


    @Override
    public void LoginToSpray(String userName, String Password) {

        //Get the mac bluetooth address
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        address=mBluetoothAdapter.getAddress();

        ParseUser.logInInBackground(userName, Password,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            currentUser.put("BlueMac",address);
                            currentUser.saveInBackground();
                            loginActivity.loginSucces();


                        } else {
                            loginActivity.loginErr();
                        }
                    }
                });

    }

    @Override
    public void LogOutFromSpray() {

        // Logout current user
        ParseUser.logOut();

    }

    @Override
    public void SignUpToSpray(String userName, String password) {


        // Save new user data into Parse.com Data Storage

        //Get the mac bluetooth address
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        address=mBluetoothAdapter.getAddress();

        int cUser = UserCounter();
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.put("majorID",cUser+1);
        user.put("BlueMac",address);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                       signUpActivity.signUpSucces();
                } else {
                        signUpActivity.signUpErr();
                }
            }
        });

    }

    @Override
    public boolean IsParseUserConnect() {
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
         return false;
        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int UserCounter() {
        int con = 0;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            con = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
            con = 666;
        }
        return  con;
    }

    @Override
    public void setSignActivity(SignUpActivity signActivity) {
        this.signUpActivity=signActivity;
    }

    @Override
    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    public void updateRelationsInServer(Integer id) {
        ParseObject relation = new ParseObject("Relations");
        relation.put("deviceID", ParseUser.getCurrentUser().get("majorID"));
        relation.put("beaconID", id);
        ParseObject relation2 = new ParseObject("Relations");
        relation2.put("beaconID", ParseUser.getCurrentUser().get("majorID"));
        relation2.put("deviceID", id);
        relation.saveInBackground();
        relation2.saveInBackground();
    }
}
