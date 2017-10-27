package com.example.rafae.collabup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private android.content.Context ctx;

    private Button next;

    private EditText email,username, password, confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = this;
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validLength()){
                    password.setError("Password length is too short");
                }
                if(!passwordMatched()){
                    confirmpassword.setError("Password did not match");
                }
                if(validLength() && passwordMatched()){
                    goToSecondPart();
                }
            }
        });
    }

    private boolean validLength(){
        return password.getText().toString().trim().length() >= 6;
    }

    private boolean passwordMatched(){
        String pass1 = password.getText().toString().trim();
        String pass2 = confirmpassword.getText().toString().trim();
        if(pass1.equals(pass2)){
            return true;
        }else{
            return false;
        }
    }

    private void goToSecondPart(){
        Intent i = new Intent(ctx, RegisterPart2Activity.class);
        i.putExtra("email", email.getText().toString().trim());
        i.putExtra("username", username.getText().toString().trim());
        i.putExtra("password", password.getText().toString().trim());
        startActivity(i);
    }

}
