package com.henallux.khal.smartcity.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connexion {
    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null){
            NetworkInfo.State networkState = info.getState();
            return networkState.compareTo(NetworkInfo.State.CONNECTED) == 0;
        }
        return false;
    }
}
