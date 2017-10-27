package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterPart2Activity extends AppCompatActivity {
   private EditText firstname, middlename, lastname;
   private CheckBox agree;
   private TextView termsandagreement;
    private String email, username, password, txtlastname, txtmiddlename,txtfirstname;
   private Context ctx;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_part2);
        ctx = this;
        Intent i = getIntent();

        email = i.getStringExtra("email");
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

        Toast.makeText(ctx,email+username+password  , Toast.LENGTH_LONG).show();

        firstname = (EditText) findViewById(R.id.firstname);
        middlename = (EditText) findViewById(R.id.middlename);
        lastname = (EditText) findViewById(R.id.lastname);
        agree = (CheckBox) findViewById(R.id.agree);
        termsandagreement = (TextView) findViewById(R.id.termsandcondition);
        termsandagreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndCondition();
            }
        });
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtfirstname = firstname.getText().toString().trim();
                txtmiddlename = middlename.getText().toString().trim();
                txtlastname = lastname.getText().toString().trim();
                    authenticateRegistration();
                if(txtfirstname.isEmpty()){
                    firstname.setError("Input field is required");
                }
                if(txtmiddlename.isEmpty()){
                    middlename.setError("Input field is required");
                }
                if(txtlastname.isEmpty()){
                    lastname.setError("Input field is required");
                }
                }
        });

    }
    private boolean isOkToProceed(){
        if(!(txtfirstname.isEmpty() && txtmiddlename.isEmpty() && txtlastname.isEmpty())){
            return true;
        }else{
            return false;
        }
    }

    private void authenticateRegistration(){
        if(!agree.isChecked()){
            agree.setError("You need to Agree to continue");
        }
        if(agree.isChecked() && isOkToProceed()){
            register();
        }
    }
    private void showTermsAndCondition(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Terms and Condition");
        builder.setMessage("The terms and Condition string resource Value");
        builder.show();
        final AlertDialog cancel = builder.show();
        builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            agree.setChecked(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            cancel.dismiss();
            }
        });
    }

    private void register(){

        class AsyncRegister extends AsyncTask<Void, Void, String> {
            ProgressDialog loading = new ProgressDialog(ctx);
            @Override
            protected void onPreExecute(){
                loading.setTitle("Registering");
                loading.setMessage(" Please wait");
                loading.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String > params = new HashMap<>();

                  params.put("email",email);
                  params.put("username",username);
                  params.put("password",password);
                  params.put("first_name",txtfirstname);
                  params.put("middle_name",txtmiddlename);
                  params.put("last_name",txtlastname);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.register_url, params);
                return res;
            }

            @Override
            protected void onPostExecute(String result){
                    loading.dismiss();
                        Toast.makeText(ctx, "Registration Success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ctx, LoginActivity.class);
                        startActivity(i);
                        finish();

                }
            }

    AsyncRegister ar = new AsyncRegister();
        ar.execute();
    }

    private void checkStatusOfRegistration(String status){
        if(status.equalsIgnoreCase("success")){
            AlertDialog.Builder build = new AlertDialog.Builder(ctx);
            build.setTitle("Successfully Registered");
            build.setMessage("Please confirm your account first to Continue");
            build.show();
            final AlertDialog later = build.show();
            build.setPositiveButton("Confirm Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = null;
                    finish();
                    Intent i = new Intent(ctx, ConfirmAccountActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                    overridePendingTransition(R.animator.slide_in_right,R.animator.slide_out_left);
                }
            });
            build.setNegativeButton("Confirm Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    later.dismiss();
                    finish();
                }
            });
        }
    }


}
