package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtBody;

    private Button btnSubmit;
    private Button btnDelete;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Intent i = getIntent();
        id = i.getStringExtra("id");
        txtBody = (EditText) findViewById(R.id.txtBody);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSubmit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        getPost();
    }
    private void getPost(){
        class GetPost extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditPostActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showAnnoucement(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> p = new HashMap<>();
                p.put("id",id);
                String s = rh.sendPostRequest(Config.URL_GET_POST,p);
                return s;
            }
        }
        GetPost ge = new GetPost();
        ge.execute();
    }
    private void showAnnoucement(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String body = c.getString(Config.TAG_BODY);

            txtBody.setText(body);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePost(){
        final String body = txtBody.getText().toString().trim();

        class UpdatePost extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditPostActivity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(EditPostActivity.this, "Post Updated Succefulyy", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditPostActivity.this, UserActivitiesActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(EditPostActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",id);
                hashMap.put("body",body);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_POST,hashMap);

                return s;
            }
        }

        UpdatePost ue = new UpdatePost();
        ue.execute();
        startActivity(new Intent(EditPostActivity.this,UserActivitiesActivity.class));
    }

    private void deletePost(){
        class DeletePost extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditPostActivity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(EditPostActivity.this, "Post Successfully Deleted", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EditPostActivity.this, UserActivitiesActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(EditPostActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> params = new HashMap<>();
                params.put("id", id);
                String s = rh.sendPostRequest(Config.URL_DELETE_POST, params);
                return s;
            }
        }

        DeletePost de = new DeletePost();
        de.execute();
    }

    private void confirmDeletePost(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this post?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletePost();
                        startActivity(new Intent(EditPostActivity.this,UserActivitiesActivity.class));
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
        if(v == btnSubmit){
            updatePost();
        }

        if(v == btnDelete){
            confirmDeletePost();
        }
    }
}
