package com.bahri.spray.Model;


import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;

import com.bahri.spray.Controller.CurrentMediaFragment;
import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.MainTabActivity;
import com.bahri.spray.Controller.SignUpActivity;
import com.bahri.spray.Controller.SprayFragment;
import com.bahri.spray.SprayUser;

import java.util.ArrayList;

/**
 * Created by user on 19/03/2015.
 */
public class MyModel {
    public static ArrayList<SprayUser> discoverdUsers = new ArrayList<SprayUser>();
    public static ArrayList<Integer> discoverdUsersIDSLocalArray = new ArrayList<Integer>();
    private static MyModel ourInstance = new MyModel();
    private ModelInterface model;
    public static MyModel getInstance() {
        return ourInstance;
    }

    private MyModel() {
        model = new ParseModel();
    }



    public interface ModelInterface
    {
        public  void LoginToSpray(String userName, String password);
        public  void LogOutFromSpray();
        public void SignUpToSpray(String userName, String password,String email,Bitmap userImage);
        public boolean IsParseUserConnect();
        public int UserCounter();
        public void setSignActivity(SignUpActivity signActivity);
        public void setLoginActivity(LoginActivity loginActivity);
        public  void setMainTabActivity(MainTabActivity mainTabActivity);
        public void updateRelationsInServer(Integer id);
        public void deleteRelations();
        public void getCloseUsers();
        public void sendImageToUsers(ArrayList<Integer> usersTosendTo,Bitmap bitmap);
        public void setCurrentMediaFragment(CurrentMediaFragment currentMediaFragment);
        public void getCurrentFiles();
        public void updateLocation(double latitude,double longitude);
        public void getCloseUsersByGPS();
    }

    public  void LoginToSpray(String userName, String password){

        model.LoginToSpray(userName, password);
    }

    public  void LogOutFromSpray(){
        model.LogOutFromSpray();
    }

    public void SignUpToSpray(String userName, String password,String email,Bitmap userImage){
        model.SignUpToSpray(userName,password,email,userImage);
    }

    public void setSprayFragment(SprayFragment fragment)
    {}
    public void setCurrentMediaFragment(CurrentMediaFragment currentMediaFragment)
    {
        model.setCurrentMediaFragment(currentMediaFragment);
    }

    public boolean IsParseUserConnect(){
        return  model.IsParseUserConnect();
    }
    public int UserCounter(){ return  model.UserCounter();}
    public void setSignInActivity(SignUpActivity activity){model.setSignActivity(activity);}
    public void setLoginActivity(LoginActivity activity){model.setLoginActivity(activity);}
    public  void setMainTabActivity(MainTabActivity mainTabActivity){model.setMainTabActivity(mainTabActivity);}
    public  void deleteRelations(){
        model.deleteRelations();}
    public void updateRelationsInServer(Integer id)
    {
        model.updateRelationsInServer(id);
    }
    public void getCloseUsers(){model.getCloseUsers();}
    public void sendImageToUsers(ArrayList<Integer> usersTosendTo,Bitmap bitmap)
    {
        model.sendImageToUsers(usersTosendTo,bitmap);
    }
    public void getCurrentFiles()
    {
        model.getCurrentFiles();
    }
    public void updateLocation(double latitude,double longitude)
    {
        model.updateLocation(latitude,longitude);
    }
    public void getCloseUsersByGPS()
    {
        model.getCloseUsersByGPS();
    }
}
