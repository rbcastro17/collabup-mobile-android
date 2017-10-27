package com.example.rafae.collabup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ConfirmAccountActivity extends AppCompatActivity {

    private EditText code;
    private Button send;
   Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);
        ctx = this;
        code = (EditText) findViewById(R.id.code);
        send = (Button) findViewById(R.id.btnactivate);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String checkCode= code.getText().toString().trim();
                if(checkCode.isEmpty()){
                    code.setError("Enter your code to continue.");
                }else {
                    activateaccount();
                }
            }
        });
    }
    private void activateaccount(){
        final String strCode = code.getText().toString().trim();
        class AsyncActivateUser extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();
                params.put("code", strCode);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.activate_account_url, params);
            return res;
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                if(result.equalsIgnoreCase("success")){
                    Toast.makeText(ctx,"Activation Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(ctx, "Invalid Code", Toast.LENGTH_LONG).show();
                }
            }
        }
    AsyncActivateUser aau = new AsyncActivateUser();
        aau.execute();}
@Override
    public void onBackPressed(){
    Intent i = new Intent(ctx, LoginActivity.class);
    startActivity(i);
    Toast.makeText(ctx, "Transaction Canceled", Toast.LENGTH_SHORT).show();
    finish();
}
}
