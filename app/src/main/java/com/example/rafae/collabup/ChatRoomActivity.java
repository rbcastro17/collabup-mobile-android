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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    private String group_id, group_name, code, group_owner, final_code_chat_room, temp_key, username,ok ;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private Button btnSendMessage;
    private EditText messageTxt;
    private TextView messagefeed;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ctx = this;
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        SharedPreferences pref = getSharedPreferences("pref-"+group_id, MODE_PRIVATE);
        boolean go = pref.getBoolean("changeOk", true);


        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        messagefeed = (TextView) findViewById(R.id.messagefeed);
        messageTxt =(EditText) findViewById(R.id.txtMessageInput);


        group_name = i.getStringExtra("group_name");
        code = i.getStringExtra("code");
        group_owner = i.getStringExtra("owner");
        username = i.getStringExtra("username");

        if(go == true){
            ok = i.getStringExtra("ok");
        }else{
            ok = "yes";
        }
        final_code_chat_room = group_id+"-"+group_name+"-"+code;

        if(ok.equalsIgnoreCase("no")){
            setupChat();
        }

        setTitle("Group Chat - " + group_name);

        root = FirebaseDatabase.getInstance().getReference().child(final_code_chat_room);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", "@"+username);
                map2.put("msg",messageTxt.getText().toString().trim());
                message_root.updateChildren(map2);
                messageTxt.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            messagefeed.append(chat_user_name +" : "+chat_msg +" \n");
        }


    }

    private void setupChat(){
        class AsyncSet extends AsyncTask<Void, Void, String> {
            ProgressDialog loading = new ProgressDialog(ctx);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading.setTitle("Setting Up For First Use");
                loading.setMessage("Please wait");
                loading.show();
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... s) {
                SharedPreferences.Editor pref = getSharedPreferences("pref-"+group_id, MODE_PRIVATE).edit();
                pref.putBoolean("changeOk", false);
                pref.apply();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(final_code_chat_room,"");
                ok = "yes";
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> params = new HashMap<>();

                params.put("group_id", group_id);
                String res =  rh.sendPostRequest(Config.sendok_url, params);
                return res;
            }
        }
        AsyncSet aa = new AsyncSet();
        aa.execute();
    }

}
