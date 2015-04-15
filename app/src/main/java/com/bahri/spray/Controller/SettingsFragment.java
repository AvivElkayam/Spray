package com.bahri.spray.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

/**
 * Created by mac on 3/29/15.
 */
public class SettingsFragment extends Fragment {
    Button logOutButton;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        return  inflater.inflate(R.layout.fragment_setting_layout,container,false);
    }
    public SettingsFragment() {

    }

    private void initView() {
        logOutButton = (Button)getActivity().findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyModel.getInstance().LogOutFromSpray();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }
}
