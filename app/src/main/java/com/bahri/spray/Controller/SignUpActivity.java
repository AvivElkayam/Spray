package com.bahri.spray.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bahri.spray.Model.MyModel;
import com.bahri.spray.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SignUpActivity extends ActionBarActivity {

    // Declare Variables
    Button SelectFromGallery;
    Button signUp;
    Button chooseImageButton;
    Button alreadyAccount;
    String usernametxt,passwordtxt;
    EditText passwordTextField,emailTextField,userNameTextField;
    Bitmap imageBitmap;
    int cUser;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        MyModel.getInstance().setSignInActivity(this);


        // Locate EditTexts in main.xml
        userNameTextField = (EditText) findViewById(R.id.username);
        passwordTextField = (EditText) findViewById(R.id.password);
        emailTextField = (EditText)findViewById(R.id.signup_email_text_field);
        cUser = MyModel.getInstance().UserCounter();



        imageView = (ImageView)findViewById(R.id.signup_image_view);
        // Locate Buttons in main.xml
        signUp = (Button) findViewById(R.id.signup_signup_button);
        chooseImageButton = (Button)findViewById(R.id.signup_choose_image_button);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAndShowDialog();
            }
        });
        alreadyAccount = (Button) findViewById(R.id.signup_have_account_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyModel.getInstance().SignUpToSpray(userNameTextField.getText().toString(),passwordTextField.getText().toString(),emailTextField.getText().toString(),imageBitmap);


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
    private void buildAndShowDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.login_image_dialog_box);
        dialog.setTitle("Choose Photo");

        // set the custom dialog components - text, image and button
        Button camera = (Button) dialog.findViewById(R.id.login_image_dialog_box_camera_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                dialog.dismiss();
                startActivityForResult(intent, 1);

            }
        });
        Button photoLibrary = (Button) dialog.findViewById(R.id.login_image_dialog_box_photo_library_button);
        photoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                dialog.dismiss();
                startActivityForResult(intent, 2);

            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.login_image_dialog_box_cancel_button);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            //from camera
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);
//                    Picasso.with(getActivity())
//                            .load(f)
//                            .resize(imageView.getWidth(), imageView.getHeight())
//                            .centerCrop()
//                            .into(imageView);
//                    Target target = new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            imageView.setImageBitmap(bitmap);
//                            Drawable image = imageView.getDrawable();
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                        }
//                    };
//
//                    Picasso.with(getActivity()).load(f).into(target);


                imageView.setImageBitmap(bitmap);
                this.imageBitmap=bitmap;
                //putBitmapToIntentAndStartActivity(bitmap);
                String path = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {

            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                imageView.setImageBitmap(thumbnail);
                this.imageBitmap = thumbnail;
                //putBitmapToIntentAndStartActivity(thumbnail);
            }
        }

    }

    public void signUpSucces(){

        Toast.makeText(getApplicationContext(),
                "Successfully Signed up",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(
        SignUpActivity.this,
                LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUpErr(String error){
        Toast.makeText(
                getApplicationContext(),
                error,
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
