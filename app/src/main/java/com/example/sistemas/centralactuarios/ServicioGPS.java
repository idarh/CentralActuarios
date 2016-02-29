package com.example.sistemas.centralactuarios;

/**
 * Created by Matadamas on 26/02/2016.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class ServicioGPS extends Service implements LocationListener{

    private final Context context;
    double lat;
    double lng;
    long time;
    boolean gpsEnable;
    private LocationManager locationManager;
    private Location location;
    private Criteria criteria;
    private String bestProvider;

    private Intent i;
    private IntentFilter filter;

    public ServicioGPS(Context context) {
        this.context = context;
    }

    public ServicioGPS() {
        this.context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        i = new Intent();
        filter = new IntentFilter();
        filter.addAction("com.example.sistemas.centralactuarios.CHANGE_LOCATION_INTENT");
        Toast.makeText(this.getApplicationContext(),"onCreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId){
        getLocation();
        Toast.makeText(this.getApplicationContext(),"Servicio creado",Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    public void onDestroy(){
        stopSelf();
        Toast.makeText(this.getApplicationContext(),"Servicio destruido",Toast.LENGTH_SHORT).show();
    }

    // Metodos implementados por parte del manejador GPS
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            time = location.getTime();

            Log.d("Tag ", "Coordenadas actuales. Lat = " + Double.toString(lat) + ", Lng = " + Double.toString(lng) + "Hora: " + Long.toString(time));

            //se lanza el intent sobre un bradcast para ser escuchado en las actividades dela app
            i.setAction("com.example.sistemas.centralactuarios.CHANGE_LOCATION_INTENT");
            i.putExtra("latitud", lat);
            i.putExtra("longitud", lng);
            i.putExtra("hora",time);
            sendBroadcast(i);//se envia el evento en un broadcast

            Log.d("Tag ", "Broadcast enviado");
        }
        else{
            Log.d("Tag ", "No existe proovedor de GPS");
            Toast.makeText(context, "Se perdió la conexión con el satélite.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //metodo que captura las coordenadas actuales
    public void getLocation(){
        try {
            locationManager =(LocationManager)this.context.getSystemService(LOCATION_SERVICE);//nos conectamos aun servicio del sistema, en este caso al servicio de ubicación
            gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // se captura el estado del proveedor GPS (activo o inactivo)
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria,true));
        }catch (Exception e){}

        if(gpsEnable){ //si el proveedor de gps está activo
            //se hace la petición de las actualizacion en locaciones cada minuto (1000*60)  o cada 10 metros
            //locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 60, 10, this);//quitar en caso de que solo se tenga el GPS de la tablet
            locationManager.requestLocationUpdates(bestProvider,1000*60,10,this);
            //se obtiene la última posición conocida dada por el proveedor
            //location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);//descomentar cuando solo existe el GPS de la tablet
            //location = new Location();
            location = locationManager.getLastKnownLocation(bestProvider);

            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                Log.d("Tag ", "Coordenadas iniciales. Lat = " + Double.toString(lat) + ", Lng = " + Double.toString(lng));
            }
            else{
                Log.d("Tag ", "No hay conexión con el satélite.");
                Toast.makeText(context, "Se perdió la conexión con el satélite.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
