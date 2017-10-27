package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Context ctx;
    private EditText email;
    private EditText password;
    private Button login, check_dialog;
    private TextView goToRegistration, goToForgetPassword;
    private String txtEmail,txtPassword;
    String JSON_RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ctx = this;
     //   SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
     //   String prefemail = pref.getString("email", null);
     //   if(prefemail.equalsIgnoreCase("signout")){

     //    autologin();
     //    }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        goToRegistration = (TextView) findViewById(R.id.gotoregister);
        goToForgetPassword = (TextView) findViewById(R.id.gotoforget);
        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strEmail = email.getText().toString();
                final String strPassword = password.getText().toString();

                if (!isValidEmail(strEmail)) {
                    email.setError(getString(R.string.string_error_email));
                }
                if (!isValidPassword(strPassword)) {
                    password.setError(getString(R.string.string_error_password));
                }
                if (isValidEmail(strEmail) && isValidPassword(strPassword)) {
                    login();
                }
            }
        });
       // test();
    goToRegistration.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoRegistration();
        }
    });
    goToForgetPassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(ctx, ForgetPasswordActivity.class);
            startActivity(i);
        }
    });
    }

    private void gotoRegistration(){
        Intent i = new Intent(ctx, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >=5 ) {
            return true;
        }
        return false;
    }

    private void test(){
        class AsyncCheck extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest(Config.connect_url);
            return res;
            }
            @Override
            protected void onPostExecute(String result){
                Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
              boolean ok = Boolean.parseBoolean(result);

                if(ok){
                    login.setEnabled(true);
                    Toast.makeText(ctx,"Connected",Toast.LENGTH_LONG).show();
            }else{

            }
            }
        }
        AsyncCheck a = new AsyncCheck();
        a.execute();

    }
    private void autologin(){
        Intent i = new Intent(ctx, HomeActivity.class);
        startActivity(i);
        finish();
    }
    private void login() {

         txtEmail = email.getText().toString();
         txtPassword = password.getText().toString();

        class AsyncLogin extends AsyncTask<Void, Void, String> {

            ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute() {
                loading.setTitle("Loading");
                loading.setMessage("Logging In\n Please wait...");
                loading.show();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", txtEmail);
                params.put("password", txtPassword);

                RequestHandler rh = new RequestHandler();

                String res = rh.sendPostRequest(Config.login_url, params);
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                loading.dismiss();
                JSON_RESULT = result;
               // Toast.makeText(ctx, JSON_RESULT, Toast.LENGTH_LONG).show();
                saveDataandLogin();

            }
            }
            AsyncLogin al = new AsyncLogin();
        al.execute();
        }

    private void saveDataandLogin() {
        try {

            JSONObject obj = new JSONObject(JSON_RESULT);
            JSONArray jsonArray = obj.getJSONArray("result");
            JSONObject o = jsonArray.getJSONObject(0);
            int id = o.getInt("id");
            String status = o.getString("status");
            String jsonEmail = o.getString("email");
            String jsonImage = o.getString("image");
            String jsonName = o.getString("name");
            String jsonPassword = o.getString("password");
            String role = o.getString("role");
            String usernamepref = o.getString("username");
            int active = o.getInt("active");
            if(status.equalsIgnoreCase("success") && active == 0){
                Intent i = new Intent(ctx, ConfirmAccountActivity.class);
                startActivity(i);
                finish();
            }
            else if(status.equalsIgnoreCase("success") && active == 1){
                if (role.equalsIgnoreCase("head") || role.equalsIgnoreCase("member")) {
                    SharedPreferences.Editor pref = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    pref.putInt("id", id);
                    pref.putString("password", txtPassword);
                    pref.putString("email", txtEmail);
                    pref.putString("image", jsonImage);
                    pref.putString("name", jsonName);
                    pref.putString("role", role);
                    pref.putString("username", usernamepref);
                    pref.apply();
                    Intent i = new Intent(ctx, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else if (role == "admin"){
                    SharedPreferences.Editor pref = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                    pref.putInt("id", id);
                    pref.putString("password", txtPassword);
                    pref.putString("email", txtEmail);
                    pref.putString("image", jsonImage);
                    pref.putString("name", jsonName);
                    pref.putString("role", role);
                    pref.apply();
                    Intent i = new Intent(ctx, AdminHomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }else{
                Toast.makeText(ctx, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e){

        }
    }

    }

