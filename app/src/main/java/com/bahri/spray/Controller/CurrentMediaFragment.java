package com.bahri.spray.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.bahri.spray.SprayFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/4/15.
 */
public class CurrentMediaFragment extends Fragment {
    ArrayList<SprayFile> files;
    CurrentMediaAdapter adapter;
    ListView listView;
    ProgressDialog progressDialog;
    public class CurrentMediaAdapter extends ArrayAdapter<SprayFile>
    {
        public CurrentMediaAdapter()
        {
            super(getActivity(),R.layout.current_file_list_view_cell,files);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            //ParseUser user = (ParseUser)listView.getItemAtPosition(position);

            if(itemView==null)
            {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.current_file_list_view_cell,parent,false);
            }
            SprayFile sprayFile = files.get(position);
            TextView titleTextView = (TextView)itemView.findViewById(R.id.current_file_cell_text_view);
            titleTextView.setText(sprayFile.getFileTitle());
            ImageView imageView = (ImageView)itemView.findViewById(R.id.current_file_cell_image_view);
            imageView.setImageBitmap(sprayFile.getBitmap());

            return itemView;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.current_media_fragment_layout,container,false);
    }
    public CurrentMediaFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyModel.getInstance().setCurrentMediaFragment(this);
        initViews();
        getCurrentFiles();
    }

    private void initViews() {
        files = new ArrayList<SprayFile>();
        listView = (ListView)getActivity().findViewById(R.id.current_files_list_view);
        adapter = new CurrentMediaAdapter();
        listView.setAdapter(adapter);


    }

    private void getCurrentFiles() {
//         progressBar = ProgressDialog.show(getActivity(), "",
//                "Downloading Image...", true);
        MyModel.getInstance().getCurrentFiles();
    }
    public void fileCompletedDownloading(SprayFile fileFromServer)
    {
        this.files.add(fileFromServer);
        adapter.notifyDataSetChanged();
       // initViews();
       // progressBar.dismiss();

    }
}
