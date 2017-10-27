package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAnnouncementActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listView;

    private String JSON_STRING, user_id;
    private EditText txtBody, txtTitle;
    String body, title;

    private Button btnPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        user_id = String.valueOf(pref.getInt("id", 0));
        Toast.makeText(AdminAnnouncementActivity.this, user_id, Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        setTitle("Announcements");
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
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
        txtBody = (EditText) findViewById(R.id.txtBody);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
    }
    private void showAnnouncement(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String body = jo.getString(Config.TAG_BODY);
                String id = jo.getString(Config.TAG_ID);

                HashMap<String,String> data = new HashMap<>();
                data.put(Config.TAG_BODY,body);
                data.put(Config.TAG_ID,id);

                list.add(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                AdminAnnouncementActivity.this, list, R.layout.list_announcements,
                new String[]{Config.TAG_BODY},
                new int[]{R.id.body});

        listView.setAdapter(adapter);

    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminAnnouncementActivity.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showAnnouncement();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALLANNOUNCEMENT);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    private void addPost(){

        title = txtTitle.getText().toString().trim();
        body = txtBody.getText().toString().trim();

        class AddPost extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminAnnouncementActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //        Toast.makeText(ActivityAnnouncement.this,body+title,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("body",body);
                params.put("title",title);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADDANNOUNCEMENT, params);
                return res;
            }

        }

        AddPost ae = new AddPost();
        ae.execute();
        startActivity(new Intent(AdminAnnouncementActivity.this,AdminAnnouncementActivity.class));
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
        getMenuInflater().inflate(R.menu.admin_announcement, menu);
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

        if (id == R.id.navigation_home) {
            startActivity(new Intent(this,AdminHomeActivity.class));
        } else if (id == R.id.nav_admins) {
            startActivity(new Intent(this,ActivityAdmins.class));
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(this,ActivityUsers.class));
        } else if (id == R.id.nav_audit) {
            startActivity(new Intent(this,AuditTrailActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == btnPost){
            addPost();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ManageAnnouncements.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String empId = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.ID, empId);
        startActivity(intent);
    }
}
