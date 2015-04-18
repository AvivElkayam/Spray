package com.bahri.spray.Model;

import android.bluetooth.BluetoothAdapter;

import com.bahri.spray.AppConstants;
import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.MainTabActivity;
import com.bahri.spray.Controller.SignUpActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by user on 19/03/2015.
 */
public class ParseModel implements MyModel.ModelInterface {
    SignUpActivity signUpActivity;
    LoginActivity loginActivity;
    MainTabActivity mainTabActivity;
    String address;
    BluetoothAdapter mBluetoothAdapter;
    boolean success,exist=false;


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
        user.put(AppConstants.USER_MAJOR_ID,cUser+1);
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

    public  void setMainTabActivity(MainTabActivity mainTabActivity){
        this.mainTabActivity = mainTabActivity;
    }

    @Override
    public void updateRelationsInServer(Integer id) {
        checkIfRelationExist(id);

    }
    public boolean checkIfUserExist(Integer id)
    {
        boolean exist = false;
        for(ParseUser user : MyModel.discoverdUsers)
        {
            if(user.get(AppConstants.USER_MAJOR_ID)==id)
            {
                return true;
            }
        }
        return false;
    }
    public void checkIfRelationExist(final Integer id)
    {
        ParseQuery query = new ParseQuery(AppConstants.RELATIONS);
        query.whereEqualTo(AppConstants.RELATIONS_DECIVE_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if (e == null) {
                    exist=false;
                    for(int i=0;i<list.size();i++)
                    {
                        ParseObject obj =(ParseObject) list.get(i);
                        if(obj.get(AppConstants.RELATIONS_BEACON_ID)==id)
                        {
                            exist=true;
                            break;
                        }

                    }
                    if(exist==false)
                    {
                        ParseObject relation = new ParseObject(AppConstants.RELATIONS);
                        relation.put(AppConstants.RELATIONS_DECIVE_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
                        relation.put(AppConstants.RELATIONS_BEACON_ID, id);
                        ParseObject relation2 = new ParseObject(AppConstants.RELATIONS);
                        relation2.put(AppConstants.RELATIONS_BEACON_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
                        relation2.put(AppConstants.RELATIONS_DECIVE_ID, id);
                        relation.saveInBackground();
                        relation2.saveInBackground();
                        getCloseUsers();
                    }

                } else {
                }
            }
        });
    }
    @Override
    public void deleteRelations() {

        ParseQuery query = new ParseQuery(AppConstants.RELATIONS);
        query.whereEqualTo(AppConstants.RELATIONS_DECIVE_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if (e == null) {
                    for(int i=0;i<list.size();i++)
                    {
                        ParseObject obj =(ParseObject) list.get(i);
                        obj.deleteInBackground();
                    }

                    ParseQuery query2 = new ParseQuery(AppConstants.RELATIONS);

                    query2.whereEqualTo(AppConstants.RELATIONS_BEACON_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
                    query2.findInBackground(new FindCallback() {
                        @Override
                        public void done(List list, ParseException e) {
                            if (e == null) {
                                for(int i=0;i<list.size();i++)
                                {
                                    ParseObject obj =(ParseObject) list.get(i);
                                    try {
                                        obj.delete();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                mainTabActivity.startScanAndTransmit();
                            } else {
                            }
                        }
                    });






                } else {
                }
            }
        });

    }

    @Override
    public void getCloseUsers() {
        ParseQuery query = new ParseQuery(AppConstants.RELATIONS);
        query.whereEqualTo(AppConstants.RELATIONS_DECIVE_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {

                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject obj = (ParseObject) list.get(i);
                        ParseUser user = getUserByID((Integer) obj.get(AppConstants.RELATIONS_BEACON_ID));
                        if (user != null) {
                            if(!checkIfUserExist((Integer)user.get(AppConstants.USER_MAJOR_ID))) {
                                MyModel.discoverdUsers.add(user);
                            }
                        }

                    }
                    mainTabActivity.updateCloseUsersTextView();

                } else {
                }

            }
        });

    }

    public ParseUser getUserByID(Integer id) {
        //final ParseObject[] parseObject = new ParseObject[1];
//        ParseQuery<ParseUser> query = new ParseQuery().getUserQuery();
//        query.whereEqualTo(AppConstants.USER_MAJOR_ID,id);
//        try {
//            List list = query.find();
//            if (list.size() > 0) {
//                return (ParseUser) list.get(0);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
        ParseUser user=null;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(AppConstants.USER_MAJOR_ID, id);
        try {
            List<ParseUser> list = query.find();
            if(list.size()!=0)
            {
                user = (ParseUser) list.get(0);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }
}
