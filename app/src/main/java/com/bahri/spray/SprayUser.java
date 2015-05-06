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
    public SprayUser(String userName,String userID,Integer majorID, Bitmap image,float distance) {
        this.userName = userName;
        this.majorID = majorID;
        Image = image;
        this.userID=userID;
        this.distance=distance;
    }

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
}
