package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import static org.mapsforge.map.android.graphics.AndroidGraphicFactory.createInstance;

public class MapActivity extends Activity {

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;

    private BroadcastReceiver receiver;
    private IntentFilter filter = new IntentFilter("com.example.sistemas.centralactuarios.CHANGE_LOCATION_INTENT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(getApplication());
        setContentView(R.layout.activity_map);



        // Create a mapView and give it some properties
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mapView.setClickable(true);

        //se crea el cache de tamaño adecuado

        tileCache = AndroidUtil.createTileCache(this,"mapcache",
                mapView.getModel().displayModel.getTileSize(),1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        mapView.getModel().mapViewPosition.setZoomLevel((byte) 15);
        mapView.getMapZoomControls().setZoomLevelMin((byte) 14);
        mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

        String filepath = Environment.getExternalStorageDirectory() + "/maps/comp_centro.map";
       // String filepath = "storage/emulated/sdcard1/maps/com_centro.map";
        File mapaCache = new File(filepath);
        //se renderiza la capa usando el tema interno
        tileRendererLayer = new TileRendererLayer(tileCache,
                mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);

        tileRendererLayer.setMapFile(mapaCache);

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        //solo una capa esta asociada con el mapView al comenzar el renderizado
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        //se activa el evento clickable del mapa
        mapView.setBuiltInZoomControls(true);
        mapView.getMapScaleBar().setVisible(false);

        mapView.getModel().mapViewPosition.setCenter(new LatLong(17.0706371, -96.7392065));
        mapView.getLayerManager().getLayers().add(addMarker(17.0706333, -96.737565, "green"));
        mapView.getLayerManager().getLayers().add(addMarker(17.0768011, -96.7442327,"green"));

        List<LatLong> lista = new ArrayList<LatLong>();
        lista.add(new LatLong(17.0706333, -96.737565));
        lista.add(new LatLong(17.0768011, -96.7442329));
        mapView.getLayerManager().getLayers().add(new PaintRout().addRoute(lista,5, Color.GREEN));


  //Se configura el receptor para los broadcast de cambio de ubicación y seguimiento en el mapa.
          receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mapView.getModel().mapViewPosition.setCenter(new LatLong(intent.getDoubleExtra("latitud",17.0706371), intent.getDoubleExtra("longitud",-96.7392065)));
               // mapView.getLayerManager().getLayers().add(addMarker(intent.getDoubleExtra("latitud",17.0706371), intent.getDoubleExtra("longitud",-96.7392065),"center"));
                Toast.makeText(context, "broadcast recibido en el mapa", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Broadcast recibido en el mapa");
            }
        };

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Activity destruida", Toast.LENGTH_SHORT).show();
    }


    private Marker addMarker(double lat, double lng, String type_marker)
    {
            MyMarker marker = new MyMarker(this, new LatLong(lat, lng),
                    AndroidGraphicFactory.convertToBitmap(icon_select(type_marker)), 0, 0);

                return  marker;
    }

    private Drawable icon_select(String type_marker){
        Drawable icon_image = null;
        switch (type_marker) {
            case "green":
            icon_image = getResources().getDrawable(R.mipmap.ic_launcher_green_marker);
                break;
            case "red":
                icon_image = getResources().getDrawable(R.mipmap.ic_launcher_green_marker);
                break;
            case "center":
                icon_image = getResources().getDrawable(R.mipmap.ic_launcher_current_position);
                break;
            case "default":
                icon_image = getResources().getDrawable(R.mipmap.ic_launcher_green_marker);
                break;
        }
        return icon_image;
    }

    @Override
    protected void onResume() {

        // Registra el receptor del broadcast si la actividad está al frente
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {

        // Deja de escuchar al broadcast si la aplicación esta en pausa.
        this.unregisterReceiver(receiver);
        super.onPause();
    }

}
