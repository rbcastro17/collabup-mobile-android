package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ResetOutPasswordActivity extends AppCompatActivity {

    private EditText code, newpassword,confirmnewpassword;
    private Button confirm;
    private android.content.Context ctx;
    private String strCode,strNewPassword,strConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_out_password);
    code = (EditText) findViewById(R.id.resetcode);
    newpassword = (EditText) findViewById(R.id.newpassword);
    confirmnewpassword = (EditText) findViewById(R.id.confirmnewpassword);
    confirm = (Button) findViewById(R.id.changepassword);
        ctx = this;

    confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            strCode = convertAndTrim(code);
            strNewPassword = convertAndTrim(newpassword);
            strConfirmPassword = convertAndTrim(confirmnewpassword);
            if(strCode.isEmpty()){
                code.setError("Please enter your code to proceed");
            }
            if(strNewPassword.isEmpty()){
                newpassword.setError("This field is required");
            }
            if(strConfirmPassword.isEmpty()){
                confirmnewpassword.setError("This field is required");
            }
            if(!strNewPassword.equals(strConfirmPassword)){
                confirmnewpassword.setError("This password did not match the new one.");
            }
            if(!(strCode.isEmpty() && strNewPassword.isEmpty() &&strConfirmPassword.isEmpty()
                    &&(!strNewPassword.equals(strConfirmPassword)))) {
        changepassword();
            }
        }
    });

    }
    private String convertAndTrim(EditText e){
        return e.getText().toString().trim();
    }

    private void changepassword(){

        class AsyncChangePassword extends AsyncTask<Void,Void,String>{
        ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
            loading.show();
            loading.setMessage("Loading...\n Please wait...");
            loading.setTitle("Changing Your Password");
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("code", strCode);
                params.put("newpassword", strNewPassword);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.change_password_url, params);
                return res;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                if(result.equalsIgnoreCase("success")){
                    Toast.makeText(ctx, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ctx, LoginActivity.class);
                    startActivity(i);
                }else{
                 Toast.makeText(ctx, "Password Reset Failed ", Toast.LENGTH_SHORT).show();
                }

            }
        }
    AsyncChangePassword aa = new AsyncChangePassword();
        aa.execute();
    }
}
