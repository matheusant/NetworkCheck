package com.example.connectdesktop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

public class MyService extends Service {

    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodUpdate);
        return START_STICKY;
    }

    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    private Runnable periodUpdate = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(periodUpdate, 1*1000 - SystemClock.elapsedRealtime()%1000);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(NetworkCheckActivity.BroadcastStringForAction);
            broadcastIntent.putExtra("online_status", ""+isOnline(MyService.this));
            sendBroadcast(broadcastIntent);
        }
    };
}
