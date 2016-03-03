/**
 * Created by Matadamas on 19/02/2016.
 */
package com.example.sistemas.centralactuarios;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

public class MyMarker extends Marker {
private Context ctx;

    public MyMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.ctx = ctx;

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        /*if (this.contains(layerXY, tapXY)){



            View.OnCreateContextMenuListener vC = new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuInflater inflater = new MenuInflater(ctx);
                    inflater.inflate(R.menu.ctx_menu_edit_on_map,);
                }
           };*/




         //Toast.makeText(ctx, "Marcador con latitud: " + tapLatLong.latitude + " y una longitud: " + tapLatLong.longitude, Toast.LENGTH_SHORT).show();
         //return true;
    // }
        return super.onTap(tapLatLong,layerXY, tapXY);
    }


}
