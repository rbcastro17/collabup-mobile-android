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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FolderActivity extends AppCompatActivity {
    private int id;
    private ListView folder_list;
    private android.content.Context ctx;
    private String JSON_ACTIVITIES;
    private String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        ctx = this;
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
      //  id = pref.getInt("id", 1);
        folder_list = (ListView) findViewById(R.id.folder_list);
        folder_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
                String map_Folder_id = map.get("id").toString();
           //     String map_Group_id = map.get("group_id").toString();
                String map_Folder_name = map.get("name").toString();
                String map_Folder_desc = map.get("description").toString();
                Intent i = new Intent(ctx, FilesActivity.class);
              //  i.putExtra("group_id", map_Group_id);
                i.putExtra("folder_id", map_Folder_id);
                startActivity(i);

            }
        });

        getFolders();
    }
    private void showFolder(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try{
            JSONObject obj = new JSONObject(JSON_ACTIVITIES);
            JSONArray jsonArray = obj.getJSONArray("result");

            for(int i= 0; i < jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String folder_id = o.getString("id");
                String folder_name = o.getString("name");
                String folder_description = o.getString("description");


                HashMap<String,String> activity = new HashMap<>();
                activity.put("id", folder_id);
                activity.put("name", folder_name);
                activity.put("description", folder_description);

                list.add(activity);

            }
        }catch (JSONException e){

        }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_folders,
                new String[]{"name","description"},
                new int[]{R.id.name,R.id.description}
        );
        folder_list.setAdapter(adapter);
    }
    private void getFolders(){
        class AsyncGetFolders extends AsyncTask<Void,Void,String>{
            ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                loading.setTitle("Loading");
                loading.setMessage("Please wait...");
                loading.show();
            }
            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(Config.fetch_folders_url, group_id);
                return res;
            }
            @Override
            protected void onPostExecute(String result){
                loading.dismiss();
                JSON_ACTIVITIES = result;

                showFolder();

            }
        }
        AsyncGetFolders agp = new AsyncGetFolders();
        agp.execute();
    }
}
