package com.example.sistemas.centralactuarios;

/**
 * Created by Matadamas on 26/02/2016.
 */

//Toast.makeText(this, getIntent().getStringExtra("boleta"), Toast.LENGTH_SHORT).show();
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.OutputStreamWriter;

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

    private String archivo = "archivo_prueba_ruta.cjo";
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
           if (!crearArchivo(archivo))
               Toast.makeText(this.getApplicationContext(),"No se pudo crear el archivo.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId){
        getLocation();
        //Toast.makeText(this.getApplicationContext(),"Servicio creado",Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    public void onDestroy(){
        stopSelf();
       // Toast.makeText(this.getApplicationContext(),"Servicio destruido",Toast.LENGTH_SHORT).show();
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
            i.putExtra("hora", time);
            sendBroadcast(i);//se envia el evento en un broadcast
            writeData(archivo, Double.toString(lat) + "\t" + Double.toString(lng) + "\t" + Long.toString(time));
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
            locationManager.requestLocationUpdates(bestProvider,1000,1,this);
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



    private boolean creaDirectorio() {
        try{
            File dir = new File(Context.MODE_PRIVATE + "/MisRutas/");
            //Se crea el directorio en caso de no existir
            if(!dir.exists()){
                dir.mkdir();
            }
            }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean crearArchivo(String nameFile) {
        try {
            // Creamos un objeto OutputStreamWriter, que será el que nos permita
            // escribir en el archivo de texto. Si el archivo no existía se creará automáticamente.
            // La ruta en la que se creará el archivo será /ruta de nuestro programa/data/data/
            File file = new File(Context.MODE_PRIVATE  +"/"+ nameFile);
            if(!file.exists()) {
                OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        openFileOutput(nameFile, Context.MODE_PRIVATE));
                // Cerramos el flujo de escritura del archivo, este paso es obligatorio,
                // de no hacerlo no se podrá acceder posteriormente al archivo.
                outSWMensaje.flush();
                outSWMensaje.close();
                return true;
            }
            else
                return true;
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
            return false;
        }
    }


    private void writeData(String nombre, String string){

        try{
            OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        openFileOutput(nombre, Context.MODE_APPEND));
                // Cerramos el flujo de escritura del archivo, este paso es obligatorio,
                // de no hacerlo no se podrá acceder posteriormente al archivo.
                outSWMensaje.append(string);
                outSWMensaje.append("\r\n");
                outSWMensaje.flush();
                outSWMensaje.close();
            Log.d("TAG", "ESCRITURA CORRECTA EN EL ARCHIVO");
        }catch(Exception e){
            Log.d("TAG",e.toString() + " ---ERROR EN LA ESCRITURA");
            e.printStackTrace();

        }

    }



}
