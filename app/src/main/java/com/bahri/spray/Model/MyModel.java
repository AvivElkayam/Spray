package com.bahri.spray.Model;


import android.bluetooth.BluetoothDevice;

import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.MainTabActivity;
import com.bahri.spray.Controller.SignUpActivity;
import com.bahri.spray.Controller.SprayFragment;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by user on 19/03/2015.
 */
public class MyModel {
    public static ArrayList<ParseUser> discoverdUsers = new ArrayList<ParseUser>();
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
        public void SignUpToSpray(String userName, String password);
        public boolean IsParseUserConnect();
        public int UserCounter();
        public void setSignActivity(SignUpActivity signActivity);
        public void setLoginActivity(LoginActivity loginActivity);
        public  void setMainTabActivity(MainTabActivity mainTabActivity);
        public void updateRelationsInServer(Integer id);
        public void deleteRelations();
        public void getCloseUsers();
    }

    public  void LoginToSpray(String userName, String password){

        model.LoginToSpray(userName, password);
    }

    public  void LogOutFromSpray(){
        model.LogOutFromSpray();
    }

    public void SignUpToSpray(String userName, String password){
        model.SignUpToSpray(userName,password);
    }

    public void setSprayFragment(SprayFragment fragment)
    {}


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

}
