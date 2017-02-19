package com.example.davidcruz.hackfsu;

/**
 * Created by Junior on 2/19/2017.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class InterestVolleySingleton {

    private static InterestVolleySingleton appSingletonInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private  InterestVolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();


    }

    public static synchronized InterestVolleySingleton getInstance(Context context){
        if(appSingletonInstance == null) {
            appSingletonInstance = new InterestVolleySingleton(context);
        }
        return appSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
