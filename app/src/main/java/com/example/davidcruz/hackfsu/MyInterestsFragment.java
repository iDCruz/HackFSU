package com.example.davidcruz.hackfsu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyInterestsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Button button;
    private ProgressDialog progressDialog;


    public MyInterestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_my_interests, container, false);
        button = (Button) view.findViewById(R.id.find_food);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"button clicked", Toast.LENGTH_LONG).show();
                volleyJsonObjectRequest("http://sample-env.vffm7ydwsk.us-west-2.elasticbeanstalk.com/api/suggestion?lat=28.0221939086914&long=-82.541389465332&history=false");
            }
        });
        return view;

    }


    public void volleyStringRequest(String url) {
        String REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        progressDialog.setMessage("Loaging...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });

        InterestVolleySingleton.getInstance(getContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(" Fragment Object", response.toString());

                        LayoutInflater li = LayoutInflater.from(getContext());
                        //showDialogView = li.inflate(R.layout.show_dialog, null);
                        //outputTextView = (TextView)showDialogView.findViewById(R.id.text_view_dialog);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        //alertDialogBuilder.setView(showDialogView);
                        alertDialogBuilder
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                                .setCancelable(false)
                                .create();
                        //outputTextView.setText(response.toString());
                        alertDialogBuilder.show();
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley log", "Error: " + error.getMessage());
                progressDialog.hide();
            }
        })
        {@Override
        public Map<String, String> getHeaders(){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("Authorization", "Bearer " + App.user_id_token);
            return parameters;
        }};

        // Adding JsonObject request to request queue
        InterestVolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
