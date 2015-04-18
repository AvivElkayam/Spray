package com.bahri.spray;

/**
 * Created by mac on 4/4/15.
 */
public class AppConstants {
    //USER
    public static String USER = "User";
    public static String USER_MAJOR_ID =  "majorID";
    //RELATIONS
    public static String RELATIONS = "Relations";
    public static String RELATIONS_BEACON_ID = "beaconID";
    public static String RELATIONS_DECIVE_ID = "deviceID";
    //INSTAGRAM
    public static String INSTAGRAM_CLIENT_ID = "ad7a62cd8d5449a98d8d4a4192b4f3ea";
    public static String INSTAGRAM_SECRET_ID = "0989f04a6f4c433582fc6e2264da18f9";
    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    //Used for Authentication.
    private static final String TOKENURL ="https://api.instagram.com/oauth/access_token";
    //Used for getting token and User details.
    public static final String APIURL = "https://api.instagram.com/v1";
    //Used to specify the API version which we are going to use.
    public static String CALLBACKURL = "Your Redirect URI";
//The callback url that we have used while registering the application.

}
