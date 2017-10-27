package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ViewMembersActivity extends AppCompatActivity{
    Context ctx;
    private String JSON_MEMBERS, group_id;
    private ListView list_members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        ctx = this;
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        Toast.makeText(ctx, group_id, Toast.LENGTH_LONG).show();
        list_members = (ListView) findViewById(R.id.list_members);
        getgroups();

    }

    private void getgroups(){

        class AsyncGetMembers extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Loading Members");
                loading.setMessage("Please wait...");
                loading.show();
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                JSON_MEMBERS = result;
                showMembers();

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("group_id", group_id);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.list_members_url, params);
                return res;
            }
        }
        AsyncGetMembers agm = new AsyncGetMembers();
        agm.execute();
    }

    private void showMembers(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject obj = new JSONObject(JSON_MEMBERS);
            JSONArray jsonArray = obj.getJSONArray("result");
            for(int i = 0; jsonArray.length() > i; i++){
            JSONObject o = jsonArray.getJSONObject(i);
             String id = o.getString("id");
             String name = o.getString("name");
             String email = o.getString("email");
             String date_joined = o.getString("date_joined");
                HashMap<String,String> params = new HashMap<>();
               params.put("id", id);
                params.put("name", name);
                params.put("email", email);
                params.put("date_joined", date_joined);

                list.add(params);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_member,
                new String[]{"name","email","date_joined"},
                new int[]{R.id.member_name,R.id.member_email,R.id.member_datejoined});

        list_members.setAdapter(adapter);
    }
}
