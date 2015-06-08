package com.bahri.spray.Controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bahri.spray.R;


public class PreviewFragment extends Fragment {
ImageView imageView;
Button sendButton;
Bitmap image;
    public static PreviewFragment newInstance(String param1, String param2) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_preview, container, false);
        imageView = (ImageView)v.findViewById(R.id.preview_fragment_image_view_id);
        imageView.setImageBitmap(image);
        sendButton = (Button)v.findViewById(R.id.preview_fragment_send_button_id);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    public void onButtonPressed(Uri uri) {

    }
    public void setImageFromFragment(Bitmap bitmap)
    {
        this.image=bitmap;
    }

}
