package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Matadamas on 26/02/2016.
 */
public class IniciarRuta extends Activity {

    Button btnIniciar;
    Button btnTerminar;
    TextView lblCoordenada;

    private String latitud;
    private String longitud;
    private IntentFilter intentFilter;

    private AlertDialog alert;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inciar_ruta);
        context = getApplicationContext();

        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnTerminar = (Button)findViewById(R.id.btnDetener);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean gpsEnable;
                try {
                    LocationManager locationManager =(LocationManager)context.getSystemService(LOCATION_SERVICE);//nos conectamos aun servicio del sistema, en este caso al servicio de ubicación
                    gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // se captura el estado del proveedor GPS (activo o inactivo)

                    if(!gpsEnable) { //si el proveedor de GPS está inactivo.

                        AlertDialog.Builder builder = new AlertDialog.Builder(IniciarRuta.this);
                        builder.setMessage("Por favor active el servicio de ubicación de foma manual");
                        builder.setTitle("Selección");
                        builder.setCancelable(false);
                        builder.setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        activarGPS();
                                    }
                                });
                        alert = builder.create();
                        alert.show();
                    } else{
                        startService();
                        //onDestroy();
                    }
                }catch (Exception e){
                    Log.d("Exception controlada: ", e.toString());
                }
            }
        });

        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void startService()
    {
        if (alert != null){
            alert.dismiss();
        }
        Intent service = new Intent(this, ServicioGPS.class);
        startService(service);
    }


    private void stopService()
    {
        Intent service = new Intent(this, ServicioGPS.class);
        stopService(service);
    }

    //se abre la pantalla de configuración del servicio de ubicación
    private void activarGPS(){
        try{
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        catch(Exception e){ }
    }

}
