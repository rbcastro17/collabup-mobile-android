package com.example.rafae.collabup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context ctx;
   // String id;
    private ImageView headerAvatar, toolbarImage;
    private TextView name, update_info, headerName, headerEmail, toolbarName, changepassword;
    private String role,avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ctx = this;
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        String email = pref.getString("email", "Error here");
        avatar = pref.getString("image", "avatar.png");
        String strName = pref.getString("name", "No Name");
        role = pref.getString("role", "member");
        toolbarImage = (ImageView) findViewById(R.id.toolbarImage);
        toolbarName = (TextView) findViewById(R.id.toolbarName);
        Toast.makeText(ctx, "Welcome back "+ strName, Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View h = navigationView.getHeaderView(0);
        headerEmail = (TextView) h.findViewById(R.id.headerEmail);
        headerName = (TextView)  h.findViewById(R.id.headerName);
        changepassword = (TextView) findViewById(R.id.inchangepassword);
        headerEmail.setText(email);
        headerName.setText(strName);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(ctx, ChangePasswordInActivity.class);
                startActivity(i);

            }
        });
        headerAvatar = (ImageView) h.findViewById(R.id.headerAvatar);
        Picasso.with(this)
                .load("http://"+Config.IP+"/images/avatar.png")
                .error(R.drawable.ic_menu_share)
                .placeholder(R.drawable.ic_menu_camera)
                .resize(150,150)
                .into(headerAvatar);
    toolbarName.setText(strName);

        Picasso.with(this)
                .load("http://"+Config.IP+"/images/"+ String.valueOf(avatar))
                .placeholder(R.mipmap.ic_launcher_round)
                .resize(100,100)
                .into(toolbarImage);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.About) {

        }
        else if (id == R.id.nav_announcement) {
            Intent i = new Intent(ctx, AnnouncementActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_events) {

        } else if (id == R.id.nav_groups) {
            Intent i = new Intent(ctx, GroupListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_proflie) {
            Intent i = new Intent(ctx, HomeActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.chat) {
            Intent i = new Intent(ctx, ChatListActivity.class);
            startActivity(i);
            finish();
        } else if(id == R.id.nav_activities){
            Intent i = new Intent(ctx, UserActivitiesActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPersonalInfo(){
        class AsyncGetInfo extends AsyncTask<Void,Void,String>{
        CustomProgressDialog loading = new CustomProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
                loading.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                loading.dismiss();
             try {
                 showPersonalInfo(result);
             }catch(NullPointerException e){

                 AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                 builder.setTitle("Connection Timed Out");
                 builder.setMessage("Try Refreshing the Application, \n See if it works");
                 builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         getPersonalInfo();
                     }
                 });
             }
             }
        }
    AsyncGetInfo agi = new AsyncGetInfo();
    agi.execute();
    }

    private void showPersonalInfo(String result){
        try{
            JSONObject obj = new JSONObject(result);
            JSONArray jsonArray = obj.getJSONArray("result");
            JSONObject o = jsonArray.getJSONObject(0);
            String image = o.getString("image");
            String firstname = o.getString("firstname");
            String lastname = o.getString("lastname");

            Picasso.with(ctx)
                    .load("http://"+Config.IP+"/images/"+image)
                    .resize(150,150)
                    .into(headerAvatar);
            name.setText(firstname+" "+lastname);
        }
        catch (JSONException e){

        }
    }
    private void gotoUpdateInfo(){
        Intent i = new Intent(ctx, UpdateProfileActivity.class);
        startActivity(i);
    }

    private void logout(){

        SharedPreferences.Editor pref = getSharedPreferences("user_data",MODE_PRIVATE).edit();
      //  pref.g("");
        Intent i = new Intent(ctx, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
