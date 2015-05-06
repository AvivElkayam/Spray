package com.bahri.spray;

import android.graphics.Bitmap;

/**
 * Created by mac on 4/20/15.
 */
public class SprayFile {
    private Bitmap bitmap;
    private String fileTitle;

    public SprayFile(Bitmap bitmap, String fileTitle) {
        this.bitmap = bitmap;
        this.fileTitle = fileTitle;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
