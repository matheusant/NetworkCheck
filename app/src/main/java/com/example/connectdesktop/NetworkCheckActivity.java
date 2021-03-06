package com.example.connectdesktop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.connectdesktop.databinding.ActivityMainBinding;

public class NetworkCheckActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static final String BroadcastStringForAction = "checkinternet";
    TextView txtNotSubmit;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_check);

        txtNotSubmit = findViewById(R.id.txtNotSubmit);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

        if (isOnline(this)){
            Set_Visibilty_ON();
        }else {
            Set_Visibilty_OFF();
        }

    }
    //----------------------------------------------------------------------------------------------

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(BroadcastStringForAction)){
                if (intent.getStringExtra("online_status").equals("true")){
                    Set_Visibilty_ON();
                }else {
                    Set_Visibilty_OFF();
                }
            }

        }
    };

    public boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    public void Set_Visibilty_OFF(){
        txtNotSubmit.setText("NOT CONNECT");
        txtNotSubmit.setTextColor(Color.RED);
    }

    public void Set_Visibilty_ON(){
        txtNotSubmit.setText("CONNECT");
        txtNotSubmit.setTextColor(Color.GREEN);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

    //----------------------------------------------------------------------------------------------
}