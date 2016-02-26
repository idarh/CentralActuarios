package com.example.sistemas.centralactuarios;

/**
 * Created by Matadamas on 26/02/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Matadamas on 26/02/2016.
 */
public class MyReceiverLocation extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "broadcast recibido", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "Broadcast recibido");
    }
}