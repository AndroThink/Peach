package com.androthink.peach;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class NetworkUtils {

    static final String CONNECTION_DEFAULT_TAG = "peachRequestTag";
    static final int READ_TIMEOUT = 10000;
    static int CONNECTION_TIMEOUT = 15000;

    static void setConnectionTimeout(int millisecond){
        CONNECTION_TIMEOUT = millisecond;
    }

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }else {
            return false;
        }
    }
}
