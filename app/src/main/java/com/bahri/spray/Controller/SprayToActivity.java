package com.bahri.spray.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bahri.spray.R;

public class SprayToActivity extends ActionBarActivity {
    Button sprayToAllButton,sprayToGroupButton,chooseRecipientButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.orangeColor))));
        getSupportActionBar().setTitle("Spray To");
        setContentView(R.layout.activity_spray_to_layout);
        initViews();
    }
    public void initViews()
    {
        sprayToAllButton = (Button)findViewById(R.id.spray_to_all_button);
        sprayToAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SprayToActivity.this,SprayMediaActivity.class);
                startActivity(intent);
            }
        });
        sprayToGroupButton = (Button)findViewById(R.id.spray_to_group_button);
        sprayToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SprayToActivity.this,SprayToGroupActivity.class);
                startActivity(intent);
            }
        });
        chooseRecipientButton = (Button)findViewById(R.id.choose_recipient_button);
        chooseRecipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SprayToActivity.this, ChooseRecipientActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_in_top);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spray_to, menu);
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
