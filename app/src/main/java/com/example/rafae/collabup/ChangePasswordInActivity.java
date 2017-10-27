package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ChangePasswordInActivity extends AppCompatActivity {

    Context ctx;
    private Button change;
    private EditText oldpass,newpass,confirmpass;
    private String strPass, certPass;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_in);
        ctx= this;

        newpass = (EditText) findViewById(R.id.changenewpass);
        oldpass = (EditText) findViewById(R.id.oldpassword);
        confirmpass = (EditText) findViewById(R.id.changeconfirmnewpass);
        change = (Button) findViewById(R.id.btnchangein);
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        id = String.valueOf(pref.getInt("id", 1));
        certPass = pref.getString("password", "Password");
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOld,strNew,strChange;
                strNew = newpass.getText().toString().trim();
                strChange = confirmpass.getText().toString().trim();
                strOld = oldpass.getText().toString().trim();
               if(!strOld.equals(certPass)){
                   oldpass.setError("Password did not match the current password");
               }
                if(strOld.equals(strNew)){
                    newpass.setError("New password  must not be same to old password");
                }
                if(strNew.isEmpty()){
                    newpass.setError("Input Field is required");
                }
                if(strChange.isEmpty()){
                    confirmpass.setError("Input Field is required");
                }
                if(!(strNew.equals(strChange))){
                    confirmpass.setError("Password did not match");
                }
                if((!strNew.isEmpty())&& (!strChange.isEmpty())&& strNew.equals(strChange)
                        && strOld.equals(certPass) &&(!strNew.equals(strOld))){
                    changepassword();
                }
            }
        });
    }
    private void changepassword(){
        strPass = newpass.getText().toString().trim();
        class AsyncChangePassword extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                loading.setTitle("Changing your current password");
                loading.setMessage("Please wait...");
                loading.show();
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("password",strPass);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.change_password_in_url, params);
                return res;
                   }

                   @Override
            protected void onPostExecute(String result){
                       super.onPostExecute(result);
                       loading.dismiss();
                       if(result.equalsIgnoreCase("success")){
                           Toast.makeText(ctx, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                           Intent i = new Intent(ctx, HomeActivity.class);
                           startActivity(i);
                           finish();

                       }else {
                           Toast.makeText(ctx, "Password Reset Fail", Toast.LENGTH_SHORT).show();
                       }
                   }
        }
    AsyncChangePassword aa = new AsyncChangePassword();
        aa.execute();
    }
}
