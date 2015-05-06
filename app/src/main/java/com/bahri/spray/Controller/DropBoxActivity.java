package com.bahri.spray.Controller;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bahri.spray.AppConstants;
import com.bahri.spray.DropBoxWithSpray;
import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.bahri.spray.SprayUser;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DropBoxActivity extends ActionBarActivity {
    private Stack<String> pathStack;
    private String chosenPath="";
    private String chosenPathParent="";
    private Integer currentTag=0;

    public Stack<String> getPathStack() {
        return pathStack;
    }

    public void setPathStack(Stack<String> pathStack) {
        this.pathStack = pathStack;
    }

    public String getChosenPathParent() {
        return chosenPathParent;
    }

    public void setChosenPathParent(String chosenPathParent) {
        this.chosenPathParent = chosenPathParent;
    }

    public Integer getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(Integer currentTag) {
        this.currentTag = currentTag;
    }

    public String getChosenPath() {
        return chosenPath;
    }

    public void setChosenPath(String chosenPath) {
        this.chosenPath = chosenPath;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_box);
        this.pathStack = new Stack();
        this.pathStack.push("/");
        initFragment();

    }


    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DropBoxFolderFragment fragment = new DropBoxFolderFragment();
        fragmentTransaction.add(R.id.dropbox_file_container, fragment, String.valueOf(this.currentTag)).addToBackStack(String.valueOf(this.currentTag));
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drop_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void dropBoxFoldersAreReady(ArrayList<DropboxAPI.Entry> folders)
    {
        DropBoxFolderFragment fragment = (DropBoxFolderFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(this.currentTag));
        fragment.initListView(folders);
    }
    public void dropBoxFilesAreReady(ArrayList<String> files)
    {}
    public void sessionIsReady()
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.currentTag-=1;
        this.pathStack.pop();
        if(this.pathStack.size()==0)
            finish();
//        final DropBoxFolderFragment fragment = (DropBoxFolderFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(this.currentTag));
//
//        this.chosenPath=fragment.getParentPath();

    }
}
