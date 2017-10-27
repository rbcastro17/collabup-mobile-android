package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class GroupIndexActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String group_id,group_name, JSON_ACTIVITIES;
    ListView feed;
    EditText txtpost;
    Button post;
    Context ctx;
   // private ListView groupActivities;
    String user_id ;
    String body;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
               //     mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(ctx, ViewMembersActivity.class);
                    i.putExtra("group_id", group_id);
                    startActivity(i);
                    return true;
                case R.id.navigation_notifications:
                    Intent mi = new Intent(ctx, FolderActivity.class);
                    mi.putExtra("group_id", group_id);
                    startActivity(mi);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);
        ctx = this;
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        group_name = i.getStringExtra("group_name");

      //  Toast.makeText(ctx,group_id,Toast.LENGTH_LONG).show();
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        user_id = String.valueOf(pref.getInt("id", 1));
        post = (Button) findViewById(R.id.btnaddpost);
        txtpost =(EditText) findViewById(R.id.txtPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strbody = txtpost.getText().toString().trim();

                if(strbody.isEmpty()){
                    txtpost.setError("Input field is required");
                }else {
                    addPost();
                }
                }
        });
        feed = (ListView) findViewById(R.id.group_activities);



            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(group_name);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setHomeButtonEnabled(true);
        bar.show();

    getPostActivities();

    feed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
            String map_post_id = map.get("post_id");
            Intent i = new Intent(ctx, ViewPostActivity.class);
            i.putExtra("post_id", map_post_id);
            //Toast.makeText(ctx, map_post_id, Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    });

    }

    private void addPost(){
         body = txtpost.getText().toString().trim();

        class AsyncAddPost extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
            loading.setTitle("Adding Post");
            loading.setMessage("Please wait...");
                loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                params.put("user_id",user_id);
                params.put("group_id", group_id);
                params.put("body",body);
                RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Config.add_post_url, params);
            return res;
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                if(result.equalsIgnoreCase("success")){
                    Toast.makeText(ctx, "Posted", Toast.LENGTH_SHORT).show();
                  txtpost.setText(null);
                    getPostActivities();
                }
            }
        }
    AsyncAddPost ah = new AsyncAddPost();
        ah.execute();
    }
    private void getPostActivities(){
        class AsyncGetPosts extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                loading.setTitle("Loading");
                loading.setMessage("Please wait...");
                loading.show();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("group_id", group_id);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.get_activities_url, params);
                return res;
            }
            @Override
            protected void onPostExecute(String result){
                loading.dismiss();
                JSON_ACTIVITIES = result;
                showPosts();
            }
        }
    AsyncGetPosts agp = new AsyncGetPosts();
        agp.execute();
    }
    private void showPosts(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try{
            JSONObject obj = new JSONObject(JSON_ACTIVITIES);
            JSONArray jsonArray = obj.getJSONArray("result");

                for(int i= 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                    String post_id = o.getString("post_id");
                    String id = o.getString("user_id");
                    String name = o.getString("name");
                    String body = o.getString("body");
                    String time = o.getString("time");

                    HashMap<String,String> activity = new HashMap<>();
                    activity.put("post_id", post_id);
                    activity.put("user_id", id);
                    activity.put("name", name);
                    activity.put("body", body);
                    activity.put("time", time);
                    list.add(activity);

                }
            }catch (JSONException e){

            }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_group_activities,
                new String[]{"user_id","name","body","time"},
                new int[]{R.id.guser_id,R.id.gname,R.id.gbody,R.id.gtime}
        );
    feed.setAdapter(adapter);
    }
}
