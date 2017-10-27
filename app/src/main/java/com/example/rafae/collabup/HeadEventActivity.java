package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HeadEventActivity extends AppCompatActivity {
    private String group_id,JSON_RESULT;
    private ListView listView;
    private TextView title, start, end, body;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = this;
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        getEvents();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void getEvents(){
        class AsyncGetPost extends AsyncTask<Void,Void,String> {
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

                params.put("group_id", String.valueOf(group_id));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.fetch_events_url,params);
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                JSON_RESULT = result;
                System.out.println(result);
                showEvent();
            }
        }
        AsyncGetPost agp = new AsyncGetPost();
        agp.execute();
    }

    private void showEvent(){

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {

            JSONObject obj = new JSONObject(JSON_RESULT);
            JSONArray jsonArray = obj.getJSONArray("result");
            for(int i = 0; i <=jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                String eventTitle = o.getString("title");
                String eventBody = o.getString("body");
                String eventStart = o.getString("start");
                String eventEnd = o.getString("end");
                String eventId = o.getString("id");

                HashMap<String,String> params = new HashMap<>();
                params.put("title",eventTitle);
                params.put("body",eventBody);
                params.put("start",eventStart);
                params.put("end",eventEnd);
                params.put("id",eventId);

                list.add(params);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ctx, list, R.layout.list_events,
                new String[]{"title", "body", "start", "end"},
                new int[]{R.id.title,R.id.body, R.id.start, R.id.end}
        );
        listView.setAdapter(adapter);

    }

}
