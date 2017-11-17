package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupIndexActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String group_id,group_name, JSON_ACTIVITIES,JSON_RESULT;
    EditText txtpost;
    Button post;
    Context ctx;
    List<Post> postList;
    RecyclerView rpost;
    Button btnpost;
    String user_id ;
    String body;
    SwipeRefreshLayout swipeRefreshLayout;

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
        rpost = (RecyclerView) findViewById(R.id.recyclerView);

        rpost.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        fetchPost();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_up_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList.clear();
               fetchPost();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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


            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(group_name);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setHomeButtonEnabled(true);
        bar.show();


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
                    fetchPost();
                }
            }
        }
    AsyncAddPost ah = new AsyncAddPost();
        ah.execute();
    }
    private void fetchPost(){
        class AsyncFetchPost extends AsyncTask<Void,Void,String>{
            ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Fetching Post");
                loading.setMessage("Please Wait...");
                loading.show();
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                JSON_RESULT = result;
                //  Toast.makeText(ctx, JSON_RESULT, Toast.LENGTH_LONG).show();
                showPost();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> params = new HashMap<>();
                params.put("group_id", group_id);
                String res = rh.sendPostRequest(Config.get_activities_url, params);
                return res;
            }
        }
        AsyncFetchPost aa = new AsyncFetchPost();
        aa.execute();
    }

  private void showPost(){
      try {
          JSONObject obj = new JSONObject(JSON_RESULT);
          JSONArray jsonArray = obj.getJSONArray("result");
          for(int i = 0; i < jsonArray.length(); i++){
              JSONObject o = jsonArray.getJSONObject(i);
              postList.add(new Post
                      (
                              o.getInt("post_id"),
                              o.getString("name"),
                              o.getString("time"),
                              o.getString("body"),
                              o.getString("image")
                      )
              );

          }

      } catch (JSONException e) {
          e.printStackTrace();
      }
      PostAdapter adapter = new PostAdapter(ctx, postList);
      rpost.setAdapter(adapter);
  }
}
