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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity {

    ArrayList<String> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        Log.i("active", "interests activity");

        interests = new ArrayList<>();
    }

    public void onCheckboxClicked(View view){
        CheckBox chkClicked = (CheckBox)view;

        if(chkClicked.isChecked()){
            interests.add(chkClicked.getText().toString());
        }
    }

    public void onSubmitInterests(View view){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONArray jsonArray = new JSONArray(interests);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("interests", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", jsonObject.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://hackfsu-env.us-west-2.elasticbeanstalk.com/api/user", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("response", error.toString());
            }
        })
        {@Override
        public Map<String, String> getHeaders(){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("Authorization", "Bearer " + App.user_id_token);
            return parameters;
        }};

        requestQueue.add(request);

        Intent intent = new Intent(getApplicationContext(), Eating_Choice.class);
        startActivity(intent);

    }
}
