package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class GroupListActivity extends AppCompatActivity {
private int id;
  private  ListView listView;
    private android.content.Context ctx;
    private String role;
     private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ctx = this;
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        id = pref.getInt("id", 1);
        role = pref.getString("role", "head");

        //Toast.makeText(ctx, id+role, Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.group_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,  String> map = (HashMap) parent.getItemAtPosition(position);
                String map_Group_id = map.get("group_id").toString();
                String map_Group_name = map.get("group_name").toString();
                Intent i = new Intent(ctx,GroupIndexActivity.class);
                i.putExtra("group_id", map_Group_id);
                startActivity(i);
            }
        });
        getJSON();
    }

    private void showGroup(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String group_id = jo.getString("group_id");
                String group_name = jo.getString(Config.TAG_GROUPNAME);
                String description = jo.getString(Config.TAG_DESCRIPTION);

                HashMap<String,String> data = new HashMap<>();
                data.put("group_id", group_id);
                data.put(Config.TAG_GROUPNAME,group_name);
                data.put(Config.TAG_DESCRIPTION,description);

                list.add(data);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_groups,
                new String[]{"group_id",Config.TAG_GROUPNAME,Config.TAG_DESCRIPTION},
               new int[]{R.id.group_id,R.id.name,R.id.description});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ctx,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               JSON_STRING = s;
            //    Toast.makeText(ctx,s.toString(),Toast.LENGTH_LONG).show();

              if(s.equalsIgnoreCase("invalid")){
                 Toast.makeText(ctx,"Invalid Login", Toast.LENGTH_LONG).show();
                }
                else {
                showGroup();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("role", role);
                String s = rh.sendPostRequest(Config.URL_GET_ALL_GROUP, params);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}

