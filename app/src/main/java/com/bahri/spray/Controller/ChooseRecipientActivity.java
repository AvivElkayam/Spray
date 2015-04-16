package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChooseRecipientActivity extends ActionBarActivity {
    ListView closeUsersListView;
    ArrayAdapter<ParseUser> myArrayAdapter;
    Spinner spinner;
    ArrayList<Integer> imagesArray = new ArrayList<Integer>();
    int[] images = {
            R.drawable.group,
            R.drawable.media,
            R.drawable.spray,
            R.drawable.settings
    };
    public class SpinnerAdapter extends ArrayAdapter<Integer>
    {

        public SpinnerAdapter(Context context, int resource, List<Integer> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position,convertView,parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            View itemView = getLayoutInflater().inflate(R.layout.choose_recipient_spinner_cell,parent,false);
            ImageView image = (ImageView)itemView.findViewById(R.id.choose_recipient_cell_image);
            image.setImageResource(imagesArray.get(position));
            return itemView;
        }
    }
    public class CloseUsersArrayAdapter extends ArrayAdapter<ParseUser>
    {
        public CloseUsersArrayAdapter()
        {
            super(ChooseRecipientActivity.this,R.layout.choose_recipient_cell, MyModel.discoverdUsers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            ParseUser user = (ParseUser)closeUsersListView.getItemAtPosition(position);

            if(itemView==null)
            {
                itemView = getLayoutInflater().inflate(R.layout.choose_recipient_cell,parent,false);
            }
            ParseUser parseUser = MyModel.discoverdUsers.get(position);
            TextView nameTextView = (TextView)itemView.findViewById(R.id.choose_recipient_cell_name_text_view);
            nameTextView.setText(user.getUsername());
            return itemView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipient_layout);
        initViews();
    }

    private void initViews() {
//        ParseUser user1 = new ParseUser();
//        user1.setUsername("Aviv");
//
//        ParseUser user2 = new ParseUser();
//        user2.setUsername("Raz");
//
//        ParseUser user3 = new ParseUser();
//        user3.setUsername("Amir");
//        MyModel.discoverdUsers.add(user1);
//        MyModel.discoverdUsers.add(user2);
//        MyModel.discoverdUsers.add(user3);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_background));
        myArrayAdapter = new CloseUsersArrayAdapter();
        closeUsersListView = (ListView)findViewById(R.id.choose_recipient_list_view);
        closeUsersListView.setAdapter(myArrayAdapter);
        spinner = (Spinner)findViewById(R.id.choose_recipient_spinner);
        for(int i=0;i<images.length;i++) {
            imagesArray.add(images[i]);
        }
        spinner.setAdapter(new SpinnerAdapter(this,R.layout.choose_recipient_spinner_cell,imagesArray));

        ActionBar actionBar = getSupportActionBar();
        SpannableString spannableString = new SpannableString("Choose Recipient");
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.toString()
                .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //actionBar.setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setIcon(R.drawable.ic_launcher);
        actionBar.setTitle(spannableString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_recipient, menu);
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
}
