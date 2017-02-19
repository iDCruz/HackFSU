package com.example.davidcruz.hackfsu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyInterestsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleClient;
    private LocationRequest mLocationRequest;
    private Boolean mRequestLocationUpdates;
    Location mLastLocation;

    private Uri gmmIntentUri;
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
        if (mGoogleClient == null) {
            mGoogleClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleClient.connect();
        createLocationRequest();
        button = (Button) view.findViewById(R.id.find_food);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"button clicked", Toast.LENGTH_LONG).show();

                volleyJsonObjectRequest("http://sample-env.vffm7ydwsk.us-west-2.elasticbeanstalk.com/api/suggestion?lat=" + String.valueOf(mLastLocation.getLatitude())+ "&long=" + String.valueOf(mLastLocation.getLongitude()) + "&history=false");

            }
        });
        return view;

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mRequestLocationUpdates = true;
        Log.d("MAP_API", "Location Request created yo!!!");
    }

    @Override
    public void onStart() {
        mGoogleClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleClient, this
        );
    }


    public void volleyStringRequest(String url) {
        String REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
        progressDialog.setMessage("Loaging...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        InterestVolleySingleton.getInstance(getContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void volleyJsonObjectRequest(String url) {

        String REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";
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
                        try {
                            if(mLastLocation != null) {
                                gmmIntentUri = Uri.parse("google.navigation:q=" + response.get("address").toString().replaceAll("\\s+", "+"));
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        } catch(Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley log", "Error: " + error.getMessage());
                progressDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("Authorization", "Bearer " + App.user_id_token);
                return parameters;
            }
        };

        // Adding JsonObject request to request queue
        InterestVolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
