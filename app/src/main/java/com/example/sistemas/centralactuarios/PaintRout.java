package com.example.sistemas.centralactuarios;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Polyline;

import java.util.List;

/**
 * Created by Matadamas on 20/02/2016.
 */
public class PaintRout {


    public Polyline addRoute(List<LatLong> lista, int  tamLinea, int color){
        // instantiating the paint object
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(tamLinea);
        paint.setStyle(Style.STROKE);

// instantiating the polyline object
        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);

// set lat lng for the polyline
        List<LatLong> coordinateList = polyline.getLatLongs();
        for (LatLong coord:lista) {
            coordinateList.add(coord);
        }
        return polyline;
    }

}
