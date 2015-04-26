package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bahri.spray.AppConstants;
import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.bahri.spray.SprayUser;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ChooseRecipientActivity extends ActionBarActivity {
    ListView closeUsersListView;
    ArrayAdapter<SprayUser> myArrayAdapter;
    Spinner spinner;
    TextView noUsersTextView;
    LinearLayout usersNearbyLayout;
    ArrayList<Integer> imagesArray = new ArrayList<Integer>();
    ArrayList<Integer> chosenUsersIndexArray = new ArrayList<Integer>();
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
    public class CloseUsersArrayAdapter extends ArrayAdapter<SprayUser>
    {
        public CloseUsersArrayAdapter()
        {
            super(ChooseRecipientActivity.this,R.layout.choose_recipient_cell, MyModel.discoverdUsers);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

           // SprayUser user = (SprayUSer)closeUsersListView.getItemAtPosition(position);

            if(itemView==null)
            {
                itemView = getLayoutInflater().inflate(R.layout.choose_recipient_cell,parent,false);
            }
            SprayUser sprayUser = MyModel.discoverdUsers.get(position);
            TextView nameTextView = (TextView)itemView.findViewById(R.id.choose_recipient_cell_name_text_view);
            nameTextView.setText(sprayUser.getUserName());
            TextView distanceTextView = (TextView)itemView.findViewById(R.id.choose_recipient_cell_distance_text_view);
//            float i2=(float)sprayUser.getDistance();
//            distanceTextView.setText(new DecimalFormat("##.##").format(i2));
            distanceTextView.setText((sprayUser.getDistance())+" KM from you");
            ImageView imageView = (ImageView)itemView.findViewById(R.id.choose_recipient_cel_image_view);
            if(sprayUser.getImage()!=null) {
                //imageView.setImageBitmap(AppConstants.getRoundedCornerBitmap(sprayUser.getImage(), 64));
                imageView.setImageBitmap(sprayUser.getImage());
            }
            else
            {
                imageView.setImageBitmap(AppConstants.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.group),64));
            }
            CheckBox checkBox = (CheckBox)itemView.findViewById(R.id.choose_recipient_cell_check_box);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        chosenUsersIndexArray.add(position,position);
                    }
                    else
                    {
                        chosenUsersIndexArray.remove(position);
                    }
                }
            });
            return itemView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));

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
        noUsersTextView = (TextView)findViewById(R.id.choose_recipient_no_users_text_view);
        usersNearbyLayout = (LinearLayout)findViewById(R.id.choose_recipient_users_nearby_layout);
        if(MyModel.getInstance().discoverdUsers.size()==0)
        {
            noUsersTextView.setVisibility(View.VISIBLE);
            usersNearbyLayout.setVisibility(View.GONE);
        }
        else
        {
            noUsersTextView.setVisibility(View.GONE);
            usersNearbyLayout.setVisibility(View.VISIBLE);
        }
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
