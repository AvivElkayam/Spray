package com.bahri.spray.Model;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.FloatMath;

import com.bahri.spray.AppConstants;
import com.bahri.spray.Controller.CurrentMediaFragment;
import com.bahri.spray.Controller.LoginActivity;
import com.bahri.spray.Controller.MainTabActivity;
import com.bahri.spray.Controller.SignUpActivity;
import com.bahri.spray.SprayFile;
import com.bahri.spray.SprayUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 19/03/2015.
 */
public class ParseModel implements MyModel.ModelInterface {
    SignUpActivity signUpActivity;
    LoginActivity loginActivity;
    MainTabActivity mainTabActivity;
    CurrentMediaFragment currentMediaFragment;
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
    public void SignUpToSpray(String userName, String password,String email,Bitmap userImage) {


        // Save new user data into Parse.com Data Storage
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        userImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("androidbegin.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();
        //Get the mac bluetooth address
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        address=mBluetoothAdapter.getAddress();

        int cUser = UserCounter();
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.put(AppConstants.USER_IMAGE,file);
        user.put(AppConstants.USER_MAJOR_ID, cUser + 1);
//        user.put(AppConstants.USER_LATITUDE,0);
//        user.put(AppConstants.USER_LONGITUDE,0);
        user.put("BlueMac",address);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    signUpActivity.signUpSucces();
                } else {

                    signUpActivity.signUpErr(e.getMessage());
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
        for(SprayUser user : MyModel.discoverdUsers)
        {
            if(user.getMajorID()==id)
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
                                ParseFile file = (ParseFile)user.get(AppConstants.USER_IMAGE);

                                try {
                                    byte[] bytes = file.getData();
                                    Bitmap bmp = BitmapFactory
                                            .decodeByteArray(
                                                    bytes, 0,
                                                    bytes.length);
                                    SprayUser sprayUser = new SprayUser(user.getUsername(),(Integer)user.get(AppConstants.USER_MAJOR_ID),bmp,0);
                                    MyModel.discoverdUsers.add(sprayUser);
                                    mainTabActivity.updateCloseUsersTextView();

                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
//                                MyModel.discoverdUsers.add(user);
                            }
                        }

                    }
                    mainTabActivity.updateCloseUsersTextView();

                } else {
                }

            }
        });

    }

    @Override
    public void sendImageToUsers(ArrayList<Integer> usersTosendTo, Bitmap bitmap) {

        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("androidbegin.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject(AppConstants.ITEMS);

        // Create a column named "ImageName" and set the string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
        String currentDateAndTime = sdf.format(new Date());
        imgupload.put(AppConstants.ITEMS_DISPLAY_NAME, currentDateAndTime);
        imgupload.put(AppConstants.ITEMS_DEVICE_ID,ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        imgupload.put(AppConstants.ITEMS_BEACON_ID,ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        // Create a column named "ImageFile" and insert the image
        imgupload.put(AppConstants.ITEMS_ITEM, file);

        // Create the class and the columns
        imgupload.saveInBackground();

        // Show a simple toast message
//        Toast.makeText(MainActivity.this, "Image Uploaded",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrentMediaFragment(CurrentMediaFragment currentMediaFragment) {
        this.currentMediaFragment=currentMediaFragment;
    }

    @Override
    public void getCurrentFiles() {


        // Locate the class table named "ImageUpload" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                AppConstants.ITEMS);
        query.whereEqualTo(AppConstants.ITEMS_DEVICE_ID,ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        // Locate the objectId from the class
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<SprayFile> files = new ArrayList<SprayFile>();
                for(ParseObject object:parseObjects)
                {
                    String s = (String)object.get(AppConstants.ITEMS_DISPLAY_NAME);
                    ParseFile file = (ParseFile)object.get(AppConstants.ITEMS_ITEM);
                    try {
                        byte[] bytes = file.getData();
                        Bitmap bmp = BitmapFactory
                                .decodeByteArray(
                                        bytes, 0,
                                        bytes.length);
                        SprayFile sprayFile = new SprayFile(bmp,s);
                        currentMediaFragment.fileCompletedDownloading(sprayFile);
                        files.add(sprayFile);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                }


            }
        });



    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put(AppConstants.USER_LATITUDE,Double.toString(latitude));
        user.put(AppConstants.USER_LONGITUDE,Double.toString(longitude));
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    // Saved successfully
                } else {
                    // ParseException
                }
            }
        });

    }

    @Override
    public void getCloseUsersByGPS() {
        ParseQuery query = ParseUser.getQuery();
        //query.whereEqualTo(AppConstants.RELATIONS_DECIVE_ID, ParseUser.getCurrentUser().get(AppConstants.USER_MAJOR_ID));
        query.whereNotEqualTo(AppConstants.OBJECT_ID,ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                new ScanAsyncTask().execute(list);

            }
        });
    }
    private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180/3.14169);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        float t1 = FloatMath.cos(a1)*FloatMath.cos(a2)*FloatMath.cos(b1)*FloatMath.cos(b2);
        float t2 = FloatMath.cos(a1)*FloatMath.sin(a2)*FloatMath.cos(b1)*FloatMath.sin(b2);
        float t3 = FloatMath.sin(a1)*FloatMath.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
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
    public class ScanAsyncTask extends AsyncTask<List<ParseObject>,String,String>
    {
        @Override
        protected String doInBackground(List<ParseObject>... params) {
            Location locationCurrent = new Location("");
            double currentLatitude = Double.parseDouble((String)ParseUser.getCurrentUser().get(AppConstants.USER_LATITUDE));
            double currentLongitude = Double.parseDouble((String)ParseUser.getCurrentUser().get(AppConstants.USER_LONGITUDE));
            locationCurrent.setLatitude(currentLatitude);
            locationCurrent.setLongitude(currentLongitude);


                for (int i = 0; i < params[0].size(); i++) {
                    ParseObject user = (ParseObject) params[0].get(i);
                    Location location = new Location("");
                    if (user.get(AppConstants.USER_LATITUDE) != null && user.get(AppConstants.USER_LONGITUDE) != null) {

                        double userLatitude = Double.parseDouble((String)user.get(AppConstants.USER_LATITUDE)) ;
                        double userLongitude = Double.parseDouble((String) user.get(AppConstants.USER_LONGITUDE)) ;
                        location.setLatitude(userLatitude);
                        location.setLongitude(userLongitude);

                        float dis = (locationCurrent.distanceTo(location))/1000;
                        //float dis = (float)gps2m((float)locationCurrent.getLatitude(),(float)locationCurrent.getLongitude(),(float)location.getLatitude(),(float)location.getLongitude());
                        if (dis >=0) {
                            if (user != null  ) {


                                ParseFile file = (ParseFile) user.get(AppConstants.USER_IMAGE);

                                try {
                                    if(file!=null) {
                                        byte[] bytes = file.getData();
                                        Bitmap bmp = BitmapFactory
                                                .decodeByteArray(
                                                        bytes, 0,
                                                        bytes.length);
                                        SprayUser sprayUser = new SprayUser((String) user.get("username"), (Integer) user.get(AppConstants.USER_MAJOR_ID), bmp,dis);
                                        MyModel.discoverdUsers.add(sprayUser);
                                        //mainTabActivity.updateCloseUsersTextView();

                                    }
                                    else
                                    {
                                        SprayUser sprayUser = new SprayUser((String) user.get("username"), (Integer) user.get(AppConstants.USER_MAJOR_ID), null,dis);
                                        MyModel.discoverdUsers.add(sprayUser);
                                        //mainTabActivity.updateCloseUsersTextView();
                                    }


                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
//                                MyModel.discoverdUsers.add(user);

                            }
                        }
                    }
                }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            mainTabActivity.updateCloseUsersTextView();
            super.onPostExecute(s);
        }
    }
    {

    }
}
