package com.example.davidcruz.hackfsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity {

    ArrayList<String> interests;
    private Button readybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interests = new ArrayList<>();
        readybtn = (Button) findViewById(R.id.btn_submitSurvey);
        readybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Eating_Choice.class);
                startActivity(intent);
            }
        });
    }

    public void onCheckboxClicked(View view){
        CheckBox chkClicked = (CheckBox)view;

        if(chkClicked.isChecked()){
            interests.add(chkClicked.getText().toString());
        }
    }

    public void onSubmitInterests(View view){
        JSONArray jsonArray = new JSONArray(interests);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_token", App.user_id_token);
            jsonObject.put("interests", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", jsonObject.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "url from 1", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
