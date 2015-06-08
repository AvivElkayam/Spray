package com.bahri.spray.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImagePreviewActivity extends ActionBarActivity {
    ImageView imageView;
    Button sprayButton;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));

        setContentView(R.layout.activity_image_preview);
        initViews();
       // image = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("imageByteArray"), 0, getIntent().getByteArrayExtra("imageByteArray").length);

        //image = (Bitmap) getIntent().getParcelableExtra("imageByteArray");
        //image = loadImageFromStorage(getIntent().getStringExtra("path"));

        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra("path"))
                .centerCrop()
                .into(imageView);

        //imageView.setImageBitmap(image);
        int i =8;
    }

    private Bitmap loadImageFromStorage(String path)
    {
        String str = path;
        Bitmap b = null;
        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();

        }
        return  b;
    }

    private void initViews() {
        imageView = (ImageView)findViewById(R.id.image_preview_image_view);
        sprayButton = (Button)findViewById(R.id.image_preview_spray_button);
        sprayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage(getIntent().getStringArrayListExtra("usersIndex"),image);
                finish();
                Intent intent = new Intent(ImagePreviewActivity.this,MainTabActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendImage(ArrayList<String> usersTosendTo,Bitmap bitmap) {
        MyModel.getInstance().sendImageToUsers(usersTosendTo, bitmap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_preview, menu);
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
