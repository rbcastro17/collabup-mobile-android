package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context ctx;
    private String result, username, role, JSON_GROUP;
    private int id;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ctx = this;
        SharedPreferences i = getSharedPreferences("userdata", MODE_PRIVATE);
        //Intent i = getIntent();
        id = i.getInt("id", 0);
        role =  i.getString("role","null");
        username = i.getString("username", "joe");
        listView = (ListView) findViewById(R.id.chat_list);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        fetchGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                HashMap<String,String> map =(HashMap)adapterView.getItemAtPosition(i);

                String group_id = map.get("group_id").toString();
                String group_name = map.get("group_name").toString();
                String code = map.get("code").toString();
                String owner = map.get("group_owner").toString();
                String isOkay = map.get("ok");
                intent.putExtra("group_id", group_id);
                intent.putExtra("group_name", group_name);
                intent.putExtra("code", code);
                intent.putExtra("owner", owner);
                intent.putExtra("username", username);
                intent.putExtra("ok", isOkay);
                startActivity(intent);
            }
        });
    }
    private void fetchGroups(){
        final ProgressDialog loading = new ProgressDialog(ctx);
        class AsyncGetGroups extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Loading Your Chat Lists");
                loading.setMessage("Please wait");
            }


            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                JSON_GROUP = result;
                Toast.makeText(ctx, username+id+role+JSON_GROUP,Toast.LENGTH_LONG).show();
                showGroups();
            }

            @Override
            protected String doInBackground(Void... s)
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id", String.valueOf(id));
                params.put("role", role);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.fetch_url, params);
                return res;
            }
        }
        AsyncGetGroups agg = new AsyncGetGroups();
        agg.execute();

    }

    private void showGroups(){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject object = new JSONObject(JSON_GROUP);
            JSONArray result = object.getJSONArray("result");

            for(int i = 0; i < result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String group_id = jo.getString("group_id");
                String group_name = jo.getString("group_name");
                String code = jo.getString("code");
                String description = jo.getString("description");
                String group_owner = jo.getString("group_owner");
                String isOkay = jo.getString("ok");
                HashMap<String,String> employees = new HashMap<>();
                employees.put("group_id",group_id);
                employees.put("group_name",group_name);
                employees.put("code",code);
                employees.put("description", description);
                employees.put("group_owner", group_owner);
                employees.put("ok", isOkay);
                list.add(employees);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                ChatListActivity.this, list, R.layout.chat_list,
                new String[]{"group_id", "group_name", "code", "description","group_owner"},
                new int[]{R.id.group_id, R.id.group_name, R.id.code, R.id.description, R.id.group_owner});

        listView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
