package com.bahri.spray.Controller;

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

public class SignUpActivity extends ActionBarActivity {

    // Declare Variables
    Button SelectFromGallery;
    Button signUp;
    Button takePhoto;
    Button alreadyAccount;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;
    int cUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        MyModel.getInstance().setSignInActivity(this);


        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        cUser = MyModel.getInstance().UserCounter();




        // Locate Buttons in main.xml
        SelectFromGallery = (Button) findViewById(R.id.SelectFromGallery);
        signUp = (Button) findViewById(R.id.signup);
        takePhoto = (Button) findViewById(R.id.TakePhoto);
        alreadyAccount = (Button) findViewById(R.id.alreadyAccount);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();
                MyModel.getInstance().SignUpToSpray(usernametxt,passwordtxt);


            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        SignUpActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void signUpSucces(){

        Toast.makeText(getApplicationContext(),
                "Successfully Signed up, please log in.",
                Toast.LENGTH_LONG).show();
    }

    public void signUpErr(){
        Toast.makeText(
                getApplicationContext(),
                "Sign up Error",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
