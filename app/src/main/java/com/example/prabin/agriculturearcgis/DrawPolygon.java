package com.example.prabin.agriculturearcgis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.example.prabin.agriculturearcgis.HelperClasses.HelperClass;
import com.example.prabin.agriculturearcgis.HelperClasses.JsonReader;

import java.util.Random;

/**
 * Created by Prabin on 6/16/2018.
 */

public class DrawPolygon {

    Context mContext;
    MapView mMapView;

    public DrawPolygon(Context mContext) {
        this.mContext = mContext;
        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);
    }

    public void setDistrict(String location) {

        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        int color = Color.rgb(r, g, b);
        int borderColor = color;

        r = new Random().nextInt(256);
        g = new Random().nextInt(256);
        b = new Random().nextInt(256);

        color = Color.rgb(r, g, b);
        int fillColor = color;

        GraphicsOverlay overlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(overlay);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, borderColor, 1);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, fillColor, lineSymbol);
        try {
            //create polygon by fetching the json form assets
            Polygon polygon = (Polygon) Polygon.fromJson(new JsonReader()
                    .getJsonFromAssets(mContext, location + ".json").toString());
            overlay.getGraphics().add(new Graphic(polygon, fillSymbol));

        } catch (NullPointerException e) {
            Log.e("DrawPolygon", location + " polygon not found");
        }

    }

    public void setDistricts(District[] locationList, boolean showNames) {

        GraphicsOverlay polygonOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(polygonOverlay);

        GraphicsOverlay locationNameOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(locationNameOverlay);

        for (District location : locationList) {
            int borderColor = generateRandomColor();
            int fillColor = generateRandomColor();

            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, borderColor, 1);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, fillColor, lineSymbol);

            String loc = location.name().toLowerCase();//files are named in lowercase
            try {
                //create polygon by fetching the json form assets
                Polygon polygon = (Polygon) Polygon.fromJson(new JsonReader()
                        .getJsonFromAssets(mContext, loc + ".json").toString());
                polygonOverlay.getGraphics().add(new Graphic(polygon, fillSymbol));

                if(showNames) {
                    Point pt = location.namePosition();
                    Log.d("xy", pt.getX() + " , " + pt.getY());

                    TextSymbol textSymbol = new TextSymbol(10, HelperClass.toSentenceCase(loc), Color.WHITE,
                            TextSymbol.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

                    Graphic gr = new Graphic(pt, textSymbol);
                    locationNameOverlay.getGraphics().add(gr);
                }

            } catch (NullPointerException e) {
                Log.e("DrawPolygon", loc + " polygon not found");
            }
        }
    }

    private int generateRandomColor() {

        int r = new Random().nextInt(200)+20;
        int g = new Random().nextInt(200)+20;
        int b = new Random().nextInt(200)+20;

        return Color.rgb(r, g, b);
    }
}
