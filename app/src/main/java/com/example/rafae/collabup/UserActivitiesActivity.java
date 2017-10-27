package com.example.rafae.collabup;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserActivitiesActivity extends AppCompatActivity implements ListView.OnItemClickListener{
String id;
   private ListView activities;
    Context ctx;
   private String JSON_STRING;
    private Toolbar back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities);
        ctx = this;
        back = (Toolbar) findViewById(R.id.toolbarActivities);
        back.setTitle("My Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        SharedPreferences pref= getSharedPreferences("userdata", MODE_PRIVATE);
        id = String.valueOf(pref.getInt("id",0));
        activities = (ListView) findViewById(R.id.own_activity);
        activities.setOnItemClickListener(this);
        getActivities();
        //registerForContextMenu(activities);

    }

    private void getActivities(){
        class AsyncFetchActivities extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
              loading.setTitle("Retrieving User's Activities");
              loading.setMessage("Loading\n Please wait...");
              loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id", id);

                RequestHandler rh= new RequestHandler();
                String res = rh.sendPostRequest(Config.get_activities_own_url, params);
                return res;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                JSON_STRING = result;
//                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                showActivities();
            }
        }
    AsyncFetchActivities afa = new AsyncFetchActivities();
        afa.execute();
    }

    private void showActivities(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject obj = new JSONObject(JSON_STRING);
            JSONArray jsonArray = obj.getJSONArray("result");
            for(int i = 0 ; i <jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String user_id = o.getString("user_id");
                String post_id = o.getString("post_id");
                String name = o.getString("name");
            //    String group = o.getString("group");
                String body = o.getString("body");
                String time = o.getString("time");

                HashMap<String,String> activity = new HashMap<>();
                activity.put("user_id", user_id);
                activity.put("post_id", post_id);
                activity.put("name", name);
             //   activity.put("group",group);
                activity.put("body", body);
                activity.put("time", time);
                list.add(activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_own_activities,
                new String[]{"post_id","user_id", "name", "group", "body", "time"},
                new int[]{R.id.post_id,R.id.user_id,R.id.name, R.id.body, R.id.time}
        );
        activities.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,  String> map = (HashMap) parent.getItemAtPosition(position);
        String map_Post_id = map.get("post_id").toString();
        Intent i = new Intent(ctx,EditPostActivity.class);
        i.putExtra("id", map_Post_id);
        Toast.makeText(ctx,map_Post_id, Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
