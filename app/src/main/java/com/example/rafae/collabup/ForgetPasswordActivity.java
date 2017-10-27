package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity {

    private android.content.Context ctx;
    private EditText email;
    private Button send;
    private TextView gotooutresetpassword;
    String txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ctx = this;
        email = (EditText) findViewById(R.id.txtsendEmail);
        send = (Button) findViewById(R.id.btnsendforget);
        gotooutresetpassword = (TextView) findViewById(R.id.gotooutresetpassword);
        gotooutresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(ctx, ResetOutPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }

private void sendCode(){
     txtEmail = email.getText().toString().trim();
    class AysncSendCode extends AsyncTask<Void,Void,String>{

        ProgressDialog loading = new ProgressDialog(ctx);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loading.setTitle("Sending Your Code");
            loading.setMessage("Please wait...");
            loading.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String,String> params = new HashMap<>();
            params.put("email", txtEmail);
            RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Config.send_forget_code_url,params);
            return res;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            loading.dismiss();
            Toast.makeText(ctx, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    AysncSendCode ah = new AysncSendCode();
    ah.execute();
}

    @Override
    public void onBackPressed(){
    Intent i = new Intent(ctx, LoginActivity.class);
        startActivity(i);
        finish();
}
}
