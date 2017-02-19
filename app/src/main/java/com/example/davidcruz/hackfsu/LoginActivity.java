package com.example.davidcruz.hackfsu;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;

import java.util.HashMap;
import java.util.Map;

//auth0 sample for managing session
//https://github.com/auth0-samples/auth0-android-sample/tree/master/03-Session-Handling

public class LoginActivity extends AppCompatActivity {

    private Lock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth0 auth0 = new Auth0("BwiLoAzGcVFjwdfJ9JBIPbilN6O1geQE", "idcruz.auth0.com");
        //Request a refresh token along with the id token.
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "openid offline_access");
        lock = Lock.newBuilder(auth0, callback)
                .withAuthenticationParameters(params)
                //Add parameters to the build
                .build(this);

        if (CredentialsManager.getCredentials(this).getIdToken() == null) {
            startActivity(lock.newIntent(this));
            return;
        }

        AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
        aClient.tokenInfo(CredentialsManager.getCredentials(this).getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        Log.i("creds", CredentialsManager.getCredentials(getApplicationContext()).getIdToken());
                        App.user_id_token = CredentialsManager.getCredentials(getApplicationContext()).getIdToken();
                        startActivity(new Intent(getApplicationContext(), InterestsActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        CredentialsManager.deleteCredentials(getApplicationContext());
                        startActivity(lock.newIntent(LoginActivity.this));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        lock.onDestroy(this);
        lock = null;
    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            // Login Success
            App.user_id_token = credentials.getIdToken();
            CredentialsManager.saveCredentials(getApplicationContext(),credentials);
            Log.i("Auth credentials", credentials.getAccessToken());
            startActivity(new Intent(LoginActivity.this, InterestsActivity.class));
            finish();
        }

        @Override
        public void onCanceled() {
            // Login Cancelled response
        }

        @Override
        public void onError(LockException error){
            // Login Error response
            Toast.makeText(getApplicationContext(),"Nah fam", Toast.LENGTH_SHORT).show();
        }
    };
}
