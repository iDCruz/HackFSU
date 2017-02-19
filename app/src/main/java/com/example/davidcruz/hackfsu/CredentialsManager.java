package com.example.davidcruz.hackfsu;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.result.Credentials;

//auth0 github sample for managing session
//https://github.com/auth0-samples/auth0-android-sample/tree/master/03-Session-Handling

public class CredentialsManager {

    final static String REFRESH_TOKEN = "AUTH0_REFRESH_TOKEN";
    final static String ACCESS_TOKEN = "AUTH0_ACCESS_TOKEN";
    final static String ID_TOKEN = "AUTH0_ID_TOKEN";
    final static String CREDENTIAL_TYPE = "AUTH0_CREDENTIAL_TYPE";

    public static void saveCredentials(Context context, Credentials credentials){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        sharedPref.edit()
                .putString(ID_TOKEN, credentials.getIdToken())
                .putString(REFRESH_TOKEN, credentials.getRefreshToken())
                .putString(ACCESS_TOKEN, credentials.getAccessToken())
                .putString(CREDENTIAL_TYPE, credentials.getType())
                .commit();
    }

    public static Credentials getCredentials(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        Credentials credentials = new Credentials(
                sharedPref.getString(ID_TOKEN, null),
                sharedPref.getString(ACCESS_TOKEN, null),
                sharedPref.getString(CREDENTIAL_TYPE, null),
                sharedPref.getString(REFRESH_TOKEN, null));

        return credentials;
    }

    public static void deleteCredentials(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.auth0_preferences), Context.MODE_PRIVATE);

        sharedPref.edit()
                .putString(ID_TOKEN, null)
                .putString(REFRESH_TOKEN, null)
                .putString(ACCESS_TOKEN, null)
                .putString(CREDENTIAL_TYPE, null)
                .commit();
    }




}
