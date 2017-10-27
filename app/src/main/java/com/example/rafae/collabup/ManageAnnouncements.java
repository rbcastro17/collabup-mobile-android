package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ManageAnnouncements extends AppCompatActivity implements View.OnClickListener{
    private EditText txtBody;

    private Button btnPost;
    private Button btnDelete;
    private String id, JSON_ANN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_announcements);
        Intent intent = getIntent();
        id = intent.getStringExtra(Config.ID);
        txtBody = (EditText) findViewById(R.id.txtBody);

        btnPost = (Button) findViewById(R.id.btnPost);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnPost.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        // txtBody.setText(id);
        getAnnoucement();
    }
    private void getAnnoucement(){
        class GetAnnoucement extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ManageAnnouncements.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_ANN = s;
                //   Toast.makeText(ManageAnnouncement.this,s,Toast.LENGTH_SHORT).show();
                showAnnoucement();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> p = new HashMap<>();
                p.put("id",id);
                String s = rh.sendPostRequest(Config.URL_GET_ANNOUNCEMENTONE,p);
                return s;
            }
        }
        GetAnnoucement ge = new GetAnnoucement();
        ge.execute();
    }

    private void showAnnoucement(){
        try {
            JSONObject jsonObject = new JSONObject(JSON_ANN);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String body = c.getString(Config.TAG_BODY);
            txtBody.setText(body);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateAnnoucement(){
        final String body = txtBody.getText().toString().trim();

        class UpdateAnnoucement extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ManageAnnouncements.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ManageAnnouncements.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID,id);
                hashMap.put(Config.KEY_BODY,body);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_ANNOUNCEMENT,hashMap);

                return s;
            }
        }

        UpdateAnnoucement ue = new UpdateAnnoucement();
        ue.execute();
        startActivity(new Intent(ManageAnnouncements.this,AdminAnnouncementActivity.class));
    }

    private void deleteAnnoucement(){
        class DeleteAnnoucement extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ManageAnnouncements.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ManageAnnouncements.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... vs) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                String s = rh.sendPostRequest(Config.URL_DELETE_ANNOUNCEMENT, params);
                return s;
            }
        }

        DeleteAnnoucement de = new DeleteAnnoucement();
        de.execute();
    }


    private void confirmDeleteAnnoucement(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this announcement?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteAnnoucement();
                        startActivity(new Intent(ManageAnnouncements.this,AdminAnnouncementActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if(v == btnPost){
            updateAnnoucement();
        }

        if(v == btnDelete){
            confirmDeleteAnnoucement();
        }
    }
}
