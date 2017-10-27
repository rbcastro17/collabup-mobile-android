package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPostActivity extends AppCompatActivity {

    Context ctx;
    private String post_id, JSON_RESULT, JSON_COMMENT;
    private int id;
    private EditText body;
    private Button comment;
    private String JSON_POST;
    private ListView comment_list;
    private TextView post_name, post_body, post_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        ctx = this;
        Intent i = getIntent();
        post_id = i.getStringExtra("post_id");
        SharedPreferences pref= getSharedPreferences("userdata", MODE_PRIVATE);
        id = pref.getInt("id",0);
        //for Adding Comment

        body = (EditText) findViewById(R.id.add_comment_txt);
        comment = (Button) findViewById(R.id.add_comment_btn);
        comment_list = (ListView) findViewById(R.id.comment_list);


        post_name = (TextView) findViewById(R.id.post_name);
        post_body = (TextView) findViewById(R.id.post_body);
        post_date = (TextView) findViewById(R.id.post_date);

        getPost();
        getComments();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    private void getPost(){
        class AsyncGetPost extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                loading.setTitle("Fetching Post");
                loading.setMessage("Please wait...");
                loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("post_id", post_id);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.fetch_post_url,params);
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                JSON_POST = result;
          //      Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                showPost();
            }
        }
        AsyncGetPost agp = new AsyncGetPost();
        agp.execute();
    }

    public void showPost(){

        try {
           JSONObject obj = new JSONObject(JSON_POST);
            JSONArray jsonArray = obj.getJSONArray("result");
            JSONObject o = jsonArray.getJSONObject(0);
            String name = o.getString("name");
            String date = o.getString("created_at");
            String body = o.getString("body");

          post_name.setText(name);
          post_body.setText(body);
          post_date.setText(date);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void addComment(){
        final String commentTxt = body.getText().toString().trim();
        class AsyncAddComment extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
            loading.setTitle("Adding Your Comment");
            loading.setMessage("Please wait...");
            loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String > params = new HashMap<>();
                params.put("id",String.valueOf(id));
                params.put("post_id",post_id);
                params.put("body",commentTxt);

                RequestHandler rh = new RequestHandler();

                String res = rh.sendPostRequest(Config.add_comment_url, params);
            return res;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                if(result.equalsIgnoreCase("success")){
                    Toast.makeText(ctx, "Comment Added", Toast.LENGTH_SHORT).show();
                    getComments();
                }else{
                    Toast.makeText(ctx, "Commenting Failed", Toast.LENGTH_SHORT).show();
                }


            }
        }
        AsyncAddComment aac = new AsyncAddComment();
        aac.execute();
    }


    private void getComments(){
        class AsyncGetComments extends AsyncTask<Void,Void,String>{
            ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
            super.onPreExecute();
                loading.setTitle("Fetching Comments");
                loading.setMessage("Please wait...");
                loading.show();
            }

            @Override
            protected void onPostExecute(String result){
            super.onPostExecute(result);
                loading.dismiss();
                //JSON_RESULT =result;
                JSON_COMMENT = result;
                showComments();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", post_id);

                String res = rh.sendPostRequest(Config.fetch_comments_url, params);
                return res;
            }
        }
        AsyncGetComments aa= new AsyncGetComments();
        aa.execute();

    }
    private void showComments(){

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {

            JSONObject obj = new JSONObject(JSON_COMMENT);
            JSONArray jsonArray = obj.getJSONArray("result");
            for(int i = 0; i <=jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String name = o.getString("name");
                String date = o.getString("date");
                String body = o.getString("body");

                HashMap<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("body",body);
                params.put("date",date);

                list.add(params);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_comment,
                new String[]{"name", "body", "date"},
                new int[]{R.id.comment_name,R.id.comment_body, R.id.comment_datecreated}
        );
        comment_list.setAdapter(adapter);

    }
    @Override
    public void onBackPressed(){
        finish();
    }
}
