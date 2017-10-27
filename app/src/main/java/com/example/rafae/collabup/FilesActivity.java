package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FilesActivity extends AppCompatActivity {
   private String folder_id, JSON_FILES;
   private ListView file_list;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        Intent i = getIntent();
        folder_id = i.getStringExtra("folder_id");
        ctx = this;
        file_list = (ListView) findViewById(R.id.file_list);

        getFiles();

        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = (HashMap) parent.getItemAtPosition(position);
                String view_url = map.get("view_url").toString();
                downloadFile(view_url);
            }
        });
    }
    private void downloadFile(final String url){
        class AsyncDownload extends AsyncTask<Void,Void,String>{
          ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Loading");
                loading.setMessage("Please wait...");
                loading.show();
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                final Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(i);
                return null;
            }
        }
    AsyncDownload ad = new AsyncDownload();
        ad.execute();
    }

    private void getFiles(){
        class AsyncGetFiles extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);

            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Fetching Folder's Files");
                loading.setMessage("Please wait...");
                loading.show();

            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                JSON_FILES = result;
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                   showFiles();
            }

            @Override
            protected String doInBackground(Void... s) {
                HashMap<String,String> params = new HashMap<>();
                params.put("folder_id", folder_id);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.fetchfile_url, params);
                return res;
            }

        }
    AsyncGetFiles aff = new AsyncGetFiles();
        aff.execute();
    }

    private void showFiles(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject obj = new JSONObject(JSON_FILES);
            JSONArray jsonArray = obj.getJSONArray("result");

            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String file_name = o.getString("file_name");
                String view_url = o.getString("view_url");
                String file_owner = o.getString("file_owner");

                HashMap<String,String> filemap = new HashMap<>();
                filemap.put("file_name", file_name);
                filemap.put("view_url", view_url);
                filemap.put("file_owner", file_owner);

              list.add(filemap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(ctx,
                list,R.layout.list_file,
                new String[]{"file_name", "view_url", "file_owner"},
                new int[]{R.id.file_name,R.id.view_link, R.id.file_owner});

        file_list.setAdapter(adapter);

    }
}
