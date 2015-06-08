package com.bahri.spray.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by mac on 4/17/15.
 */
public class SprayImageFragment extends Fragment {
    ListView listView;
    ArrayList<String> cellTitleArrayList;
    SprayImageArrayListAdapter adapter;
    ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int[] images = {
            R.drawable.takepic_icon29,
            R.drawable.gallery_icon29,
            R.drawable.dropbox_icon29
    };
    public class SprayImageArrayListAdapter extends ArrayAdapter<String>
    {
        public SprayImageArrayListAdapter()
        {
            super(getActivity(),R.layout.spray_image_cell_layout, cellTitleArrayList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if(itemView==null)
            {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.spray_image_cell_layout,parent,false);
            }
            String title = cellTitleArrayList.get(position);
            TextView titleTextView = (TextView)itemView.findViewById(R.id.spray_image_cell_text_view);
            titleTextView.setText(title);
            ImageView image = (ImageView)itemView.findViewById(R.id.spray_image_cell_image);
            image.setBackgroundResource(images[position]);
            switch (position)
            {
                case 0:
                {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                            startActivityForResult(intent, 1);

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }

                        }
                    });
                    break;
                }
                case 1:
                {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);



                        }
                    });
                    break;
                }
                case 2:
                {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // Intent intent = new   Intent(getActivity(),DropBoxActivity.class);
                            Intent intent = new   Intent(getActivity(),DropBoxNewActivity.class);
                            startActivityForResult(intent, 3);

                        }
                    });
                    break;

                }
            }
            return itemView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    putBitmapToIntentAndStartActivity(imageBitmap);
                   // mImageView.setImageBitmap(imageBitmap);

            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                imageView.setImageBitmap(thumbnail);
                putBitmapToIntentAndStartActivity(thumbnail);

            }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListTitleList();
        initViews();
    }

    private void initViews() {
        adapter = new SprayImageArrayListAdapter();
       listView = (ListView)getActivity().findViewById(R.id.spray_image_list_view);
       listView.setAdapter(adapter);
        imageView = (ImageView)getActivity().findViewById(R.id.spray_image_image);
    }

    private void initListTitleList() {
        cellTitleArrayList = new ArrayList<String>();
        cellTitleArrayList.add("Take a picture");
        cellTitleArrayList.add("Photo library");
        cellTitleArrayList.add("From DropBox");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_spray_image_layout, container, false);
    }
    private void putBitmapToIntentAndStartActivity(Bitmap bitmap)
    {
        Intent i = new Intent(getActivity(), ImagePreviewActivity.class);
        //Bitmap b=scaleDown(bitmap,300,true); // your bitmap
        Bitmap b=bitmap;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, bs);
        i.putExtra("imageByteArray", bs.toByteArray());
        i.putStringArrayListExtra("usersIndex",((SprayMediaActivity)getActivity()).getChosenUsersIDs());
        startActivity(i);


    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
