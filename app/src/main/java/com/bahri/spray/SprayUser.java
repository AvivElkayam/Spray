package com.bahri.spray;

import android.graphics.Bitmap;

/**
 * Created by mac on 4/21/15.
 */
public class SprayUser {
    String userName,userID;
    Integer majorID;
    Bitmap Image;
    float distance;
    boolean providers[] = new boolean[3];
    public final static int PROVIDER_BT = 0;
    public final static int PROVIDER_WIFI = 1;
    public final static int PROVIDER_CELL = 2;

    public SprayUser(String userName,String userID,Integer majorID, Bitmap image,float distance) {
        this.userName = userName;
        this.majorID = majorID;
        Image = image;
        this.userID=userID;
        this.distance=distance;
    }//


    public float getDistance() {
        return distance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getMajorID() {
        return majorID;
    }

    public void setMajorID(Integer majorID) {
        this.majorID = majorID;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public boolean isProviderBT() {
        return providers[PROVIDER_BT];
    }
    public boolean isProviderWIFI() {
        return providers[PROVIDER_WIFI];
    }
    public boolean isProviderCELL() {
        return providers[PROVIDER_CELL];
    }

    public void setProvider(int provider) {
        providers[provider] = true;
    }
}
