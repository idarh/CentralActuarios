/**
 * Created by Matadamas on 19/02/2016.
 */
package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;


import android.view.Menu;
import android.view.MenuInflater;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

import static android.support.v4.app.ActivityCompat.startActivity;

public class MyMarker extends Marker{
    private Context ctx;
    private String numBoleta;
    public MyMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset, String numBoleta) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.ctx = ctx;
        this.numBoleta = numBoleta;
    }

    public MyMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.ctx = ctx;
        this.numBoleta = null;
    }



    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        if (this.contains(layerXY, tapXY)){
        // Toast.makeText(ctx, "Marcador tap corto con latitud: " + tapLatLong.latitude + " y una longitud: " + tapLatLong.longitude, Toast.LENGTH_SHORT).show();
          if (numBoleta != null) {
              Intent i = new Intent(ctx, JustificarActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              i.putExtra("boleta",numBoleta);
              ctx.startActivity(i);
          }
     }
       // return true;
        return super.onTap(tapLatLong,layerXY, tapXY);
    }
/*
    @Override
    public boolean onTapLong(LatLong tapLatLong, Point layerXY, Point tapXY){
        if (this.contains(layerXY, tapXY)){
            Toast.makeText(ctx, "Marcador tap largo con latitud: " + tapLatLong.latitude + " y una longitud: " + tapLatLong.longitude, Toast.LENGTH_SHORT).show();
        }

        return super.onTap(tapLatLong,layerXY, tapXY);
    }
*/
}
