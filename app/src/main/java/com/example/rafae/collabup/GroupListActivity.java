package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupListActivity extends AppCompatActivity {
//private int id;
    private android.content.Context ctx;
    private String role;
    List<Group> groupList;
    RecyclerView group;
    String id ;
    SwipeRefreshLayout swipeRefreshLayout;
     private String JSON_RESULT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ctx = this;
        group = (RecyclerView) findViewById(R.id.recyclerView);
        group.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        id = String.valueOf(pref.getInt("id", 1));
       // user_id = String.valueOf(pref.getInt("id", 1));
        role = pref.getString("role", "head");
        groupList = new ArrayList<>();
        fetchGroup();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_up_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                groupList.clear();
                fetchGroup();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

   private void fetchGroup(){
       class AsyncFetchGroup extends AsyncTask<Void,Void,String>{
           ProgressDialog loading = new ProgressDialog(ctx);
           @Override
           protected void onPreExecute(){
               super.onPreExecute();
               loading.setTitle("Fetching Group");
               loading.setMessage("Please Wait...");
               loading.show();
           }
           @Override
           protected void onPostExecute(String result){
               super.onPostExecute(result);
               loading.dismiss();
               JSON_RESULT = result;
                 Toast.makeText(ctx, JSON_RESULT, Toast.LENGTH_LONG).show();
               showGroup();
           }

           @Override
           protected String doInBackground(Void... voids) {
               RequestHandler rh = new RequestHandler();
               HashMap<String,String> params = new HashMap<>();
               params.put("id", String.valueOf(id));
               params.put("role", role);
               String res = rh.sendPostRequest(Config.URL_GET_ALL_GROUP, params);
               return res;
           }
       }
       AsyncFetchGroup aa = new AsyncFetchGroup();
       aa.execute();
   }

    private void showGroup(){
        try {
            JSONObject obj = new JSONObject(JSON_RESULT);
            JSONArray jsonArray = obj.getJSONArray("result");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                groupList.add(new Group
                        (
                                o.getInt("group_id"),
                                o.getString("group_name"),
                                o.getString("description")
                        )
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GroupAdapter adapter = new GroupAdapter(ctx, groupList);
        group.setAdapter(adapter);
    }
}

