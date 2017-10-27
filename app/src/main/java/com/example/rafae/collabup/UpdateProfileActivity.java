package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateImage(){
      class AsyncUpdateImage extends AsyncTask<Void,Void,String>{
         CustomProgressDialog loading = new CustomProgressDialog(ctx);
          @Override
          protected void onPreExecute(){
              super.onPreExecute();
              loading.show();
          }


          @Override
          protected String doInBackground(Void... params) {
              return null;
          }

          @Override
          protected void onPostExecute(String result){
              super.onPostExecute(result);
          loading.dismiss();
          }
      }

      AsyncUpdateImage aui = new AsyncUpdateImage();
        aui.execute();
    }

    private void updateProfile(){

        class AsyncUpdateProfile extends AsyncTask<Void, Void, String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
              super.onPreExecute();
              loading.setTitle("Updating your Profile");
              loading.setMessage( "Please wait...");
              loading.show();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put("server_ip", Config.IP);
                params.put("name", "name.jpg");
             RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.url_image_upload, params);
                return res;
            }
        }
        AsyncUpdateProfile aup = new AsyncUpdateProfile();
        aup.execute();
    }

    private void showPhotoChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select a Picture"), PICK_IMAGE_REQUEST);
    }

}
