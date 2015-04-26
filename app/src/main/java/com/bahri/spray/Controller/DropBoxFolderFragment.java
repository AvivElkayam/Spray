package com.bahri.spray.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bahri.spray.DropBoxWithSpray;
import com.bahri.spray.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/26/15.
 */
public class DropBoxFolderFragment extends Fragment {
    ListView listView;
    private DropBoxWithSpray dropBoxWithSpray;
    ArrayList<DropboxAPI.Entry> folders;
    DropBoxFolderArrayAdapter arrayAdapter;
    private String parentPath;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drop_box_folders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDropBox();

    }    private void initDropBox() {
        dropBoxWithSpray = new DropBoxWithSpray((DropBoxActivity)getActivity());
        dropBoxWithSpray.initSession();


    }

    public class DropBoxFolderArrayAdapter extends ArrayAdapter<DropboxAPI.Entry> {
        public DropBoxFolderArrayAdapter(List<DropboxAPI.Entry> objects) {
            super(getActivity(), R.layout.drop_box_list_view_cell, objects);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            // SprayUser user = (SprayUSer)closeUsersListView.getItemAtPosition(position);

            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.drop_box_list_view_cell, parent, false);
            }
            final DropboxAPI.Entry entry = folders.get(position);
            TextView folderTitleTextView = (TextView) itemView.findViewById(R.id.drop_folder_cell_text_view);


            if(entry.isDir) {
                folderTitleTextView.setText(entry.path);
                parentPath=entry.parentPath();
                itemView.setOnClickListener(new View.OnClickListener() {
                    //directory
                    @Override
                    public void onClick(View v) {
                        //  ((DropBoxActivity)getActivity()).setChosenPath(entry.path);
//                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.dropbox_file_container);
//                    linearLayout.removeAllViews();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        DropBoxFolderFragment fragment = new DropBoxFolderFragment();
                        Integer integer = ((DropBoxActivity) getActivity()).getCurrentTag();
                        String path = ((DropBoxActivity) getActivity()).getChosenPath();
                        ((DropBoxActivity) getActivity()).setCurrentTag(integer + 1);
                        ((DropBoxActivity)getActivity()).getPathStack().push(entry.path);
                        fragmentTransaction.replace(R.id.dropbox_file_container, fragment, String.valueOf(integer + 1)).addToBackStack(String.valueOf(integer + 1));
                        fragmentTransaction.commit();

                    }
                });
            }
            else
            {

                folderTitleTextView.setText(entry.fileName());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            // TextView distanceTextView = (TextView)itemView.findViewById(R.id.choose_recipient_cell_distance_text_view);
//            float i2=(float)sprayUser.getDistance();
//            distanceTextView.setText(new DecimalFormat("##.##").format(i2));
            // distanceTextView.setText((sprayUser.getDistance())+" KM from you");
            // ImageView imageView = (ImageView)itemView.findViewById(R.id.choose_recipient_cel_image_view);

            //   imageView.setImageBitmap(sprayUser.getImage());


            return itemView;
        }
    }


    private void initViews() {
        listView = (ListView) getActivity().findViewById(R.id.drop_box_list_view);

    }

    public void initListView(ArrayList<DropboxAPI.Entry> folders) {
        this.folders = folders;
        arrayAdapter = new DropBoxFolderArrayAdapter(this.folders);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        dropBoxWithSpray.onResume();
    }

}

