package com.bahri.spray.Model;


import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.SignUpActivity;

/**
 * Created by user on 19/03/2015.
 */
public class MyModel {
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
        public void updateRelationsInServer(Integer id);
    }

    public  void LoginToSpray(String userName, String password){

        model.LoginToSpray(userName, password);
    }
    public void updateRelationsInServer(Integer id)
    {
        model.updateRelationsInServer(id);
    }
    public  void LogOutFromSpray(){
        model.LogOutFromSpray();
    }

    public void SignUpToSpray(String userName, String password){
        model.SignUpToSpray(userName,password);
    }


    public boolean IsParseUserConnect(){
        return  model.IsParseUserConnect();
    }
    public int UserCounter(){ return  model.UserCounter();}
    public void setSignInActivity(SignUpActivity activity){model.setSignActivity(activity);}
    public void setLoginActivity(LoginActivity activity){model.setLoginActivity(activity);}

}
