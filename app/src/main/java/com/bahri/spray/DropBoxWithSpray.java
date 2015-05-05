package com.bahri.spray;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.bahri.spray.Controller.DropBoxActivity;
import com.bahri.spray.Controller.DropBoxFilesFragment;
import com.bahri.spray.Controller.DropBoxFolderFragment;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/26/15.
 */
public class DropBoxWithSpray
{
    final static private String APP_KEY = "61c3y9t99mqkzyu";
    final static private String APP_SECRET = "65ym6egmso2rm5t";
    static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    DropboxAPI<AndroidAuthSession> mDBApi;
    DropBoxActivity activity;
    DropBoxFolderFragment folderFragment;
    String keyToken,secretToken;
    public DropBoxWithSpray(DropBoxActivity activity) {
        this.activity=activity;
    }
    public void initSession()
    {

// And later in some initialization function:
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        getTokens();
        if(keyToken!=null && secretToken!=null)
        {
            AccessTokenPair tokens=new AccessTokenPair(this.keyToken,this.secretToken);
            mDBApi.getSession().setAccessTokenPair(tokens);


        }
        else
        {
            mDBApi.getSession().startAuthentication(activity);

        }
        try {
            getFolders();
        } catch (DropboxException e) {
            e.printStackTrace();
        }

    }
    private void saveTokens(String a,String b)
    {
        // MY_PREFS_NAME - a static String variable like:
//public static final String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences.Editor editor = activity.getSharedPreferences("tokens", Context.MODE_PRIVATE).edit();
        editor.putString("key", a);
        editor.putString("secret", b);
        editor.commit();
    }
    private void getTokens()
    {
        SharedPreferences prefs = activity.getSharedPreferences("tokens", Context.MODE_PRIVATE);
         this.keyToken = prefs.getString("key", null);
         this.secretToken = prefs.getString("secret",null);
    }
    public void onResume()
    {
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // MANDATORY call to complete auth.
                // Sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();

                // Provide your own storeKeys to persist the access token pair
                // A typical way to store tokens is using SharedPreferences
                saveTokens(tokens.key,tokens.secret);
                //storeKeys(tokens.key, tokens.secret);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
    public ArrayList<String> getFolders() throws DropboxException {


        new AsyncTask<String, String, String>() {
            DropboxAPI.Entry dirent;
            String[] fnames = null;
            @Override
            protected String doInBackground(String... params) {
                try {
                    dirent = mDBApi.metadata(activity.getPathStack().peek(), 1000, null, true, null);
                } catch (DropboxException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ArrayList<DropboxAPI.Entry> files = new ArrayList<DropboxAPI.Entry>();
                ArrayList<String> dir=new ArrayList<String>();
                int i=0;
                if(dirent!=null) {
                    for (DropboxAPI.Entry ent : dirent.contents) {
                        Log.w("s",ent.icon);
                        files.add(ent);// Add it to the list of thumbs we can choose from
                        //dir = new ArrayList<String>();
                        dir.add(new String(files.get(i++).path));
                    }
                }
                activity.dropBoxFoldersAreReady(files);
//                fnames=dir.toArray(new String[dir.size()]);
//                new AsyncTask<ArrayList, String, String>() {
//                    @Override
//                    protected String doInBackground(ArrayList... params) {
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                    }
//                };
            }
        }.execute();

        return null;


//        try{
//            ArrayList<String> folderName=new ArrayList<String>();
//            DropboxAPI.Entry dropboxDir1 = mDBApi.metadata("/", 10, null, true, null);
//            if (dropboxDir1.isDir) {
//
//
//                List<DropboxAPI.Entry> contents1 = dropboxDir1.contents;
//
//                if (contents1 != null) {
//                    folderName.clear();
//
//                    for (int i = 0; i < contents1.size(); i++) {
//                        DropboxAPI.Entry e = contents1.get(i);
//
//                        String a = e.fileName();
//
//
//                        if(String.valueOf(e.isDir).equalsIgnoreCase("true")){
//                            folderName.add(a);
//                        }
//                    }
//                }
//            }
//            return folderName;
//        }catch (Exception ex) {
//
//
//        }
    //   return null;
    }
    public ArrayList<String> getFilesInFolder(ArrayList<String> folders,int pos)
    {try{
        ArrayList<String> file_name=new ArrayList<String>();
        DropboxAPI.Entry dropboxDir1 = mDBApi.metadata("/"+folders.get(pos), 0, null, true, null);
        if (dropboxDir1.isDir) {
            System.out.println("___isdir");

            List<DropboxAPI.Entry> contents1 = dropboxDir1.contents;

            if (contents1 != null) {
                file_name.clear();

                for (int i = 0; i < contents1.size(); i++) {
                    DropboxAPI.Entry e = contents1.get(i);

                    String a = e.path;
                    file_name.add(a);
                }
            }
        }
        return file_name;
    }catch (Exception ex) {

    }
    return null;}

    public File downloadFile(ArrayList<String> file_name)
    {
        FileOutputStream outputStream = null;
        try {
            File sdCard = Environment.getExternalStorageDirectory();

            File file = new File(sdCard.getAbsolutePath()+"/temp");

            outputStream = new FileOutputStream(file);
            @SuppressWarnings("unused")
            DropboxAPI.DropboxFileInfo info = mDBApi.getFile(file_name.get(0), null, outputStream, null);
            return file;
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("___"+e);
                }
            }
        }
        return null;
    }

}
