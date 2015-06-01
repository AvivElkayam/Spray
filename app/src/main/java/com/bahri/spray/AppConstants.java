package com.bahri.spray;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by mac on 4/4/15.
 */
public class AppConstants {
    //
    public static String OBJECT_ID = "objectId";
    public static String UPDATED_AT = "updatedAt";
    //USER
    public static String USER = "User";

    public static String USER_MAJOR_ID =  "majorID";
    public static String USER_IMAGE = "image";
    public static String USER_LATITUDE = "Latitude";
    public static String USER_LONGITUDE = "Longitude";
    public static String USER_WIFI_BSSID = "WifiBSSID";
    public static String USER_BLUETOOTH_MAC_ADDRESS = "BlueMac";
    //RELATIONS
    public static String RELATIONS = "Relations";
    public static String RELATIONS_BEACON_ID = "beaconID";
    public static String RELATIONS_DECIVE_ID = "deviceID";
    //Items
    public static String ITEMS = "Items";
    public static String ITEMS_DISPLAY_NAME = "displayName";
    public static String ITEMS_DEVICE_ID = "deviceID";
    public static String ITEMS_BEACON_ID = "beaconID";
    public static String ITEMS_ITEM = "Item";
    public static String ITEMS_RECEIVER_ID = "receiverID";
    public static String ITEMS_SENDER_ID = "senderID";

    //Location
    public static Integer distance = 100;
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels*6, pixels*6, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
