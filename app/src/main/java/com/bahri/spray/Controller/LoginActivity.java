package com.bahri.spray.Controller;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

public class LoginActivity extends ActionBarActivity {

    // Declare Variables
    Button loginbutton;
    Button signup;
    Button img;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;
    int cUser;
    BluetoothAdapter mBluetoothAdapter;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            MyModel.getInstance().setLoginActivity(this);
            // If current user is NOT anonymous user
            // Get current user data from Parse.com

            if (MyModel.getInstance().IsParseUserConnect()) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(LoginActivity.this, tab_test_activity.class);
                startActivity(intent);
                finish();
            }

        setContentView(R.layout.activity_login);

        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        cUser = MyModel.getInstance().UserCounter();




                // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        img = (Button) findViewById(R.id.img);



        // Login Button Click Listener
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                MyModel.getInstance().LoginToSpray(usernametxt, passwordtxt);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void loginSucces(){
        // If user exist and authenticated, send user to Welcome.class
        Intent intent = new Intent(
                LoginActivity.this,
                Welcome.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),
                "Successfully Logged in",
                Toast.LENGTH_LONG).show();
        finish();
    }

    public void loginErr(){
        Toast.makeText(
                getApplicationContext(),
                "No such user exist, please signup",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
