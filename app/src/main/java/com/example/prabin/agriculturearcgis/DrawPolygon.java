package com.example.prabin.agriculturearcgis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.util.ListenableList;
import com.example.prabin.agriculturearcgis.Data.District;
import com.example.prabin.agriculturearcgis.HelperClasses.HelperClass;
import com.example.prabin.agriculturearcgis.HelperClasses.JsonReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Prabin on 6/16/2018.
 */

public class DrawPolygon {

    private Context mContext;
    private MapView mMapView;

    private static final String TAG = "DRAW_POLYGON";

    public DrawPolygon(Context mContext) {
        this.mContext = mContext;
        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);
    }

    public void setPolygon(String location) {

        int borderColor = Color.rgb(100, 100, 100);

        GraphicsOverlay overlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(overlay);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.DASH_DOT, borderColor, 2);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
                Color.argb(0, 0, 0, 0), lineSymbol);
        try {
            //create polygon by fetching the json form assets
            Polygon polygon = (Polygon) Polygon.fromJson(new JsonReader()
                    .getJsonFromAssets(mContext, location + ".json").toString());
            overlay.getGraphics().add(new Graphic(polygon, fillSymbol));

        } catch (NullPointerException e) {
            Log.e(TAG, location + " polygon not found");
        }

    }

    public void setDistricts(List<String> locationList, int fillColor, boolean showNames, int textColor) {

        if (mMapView == null) {
            return;
        }

        GraphicsOverlay polygonOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(polygonOverlay);
        List<Graphic> polygonGraphics = new ArrayList<>();

        GraphicsOverlay locationNameOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(locationNameOverlay);
        List<Graphic> textGraphics = new ArrayList<>();

        for (String location : locationList) {
            int borderColor = generateRandomColor();

            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, borderColor, 1);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, fillColor, lineSymbol);//fill color

            String name = location.toLowerCase();//files are named in lowercase
            try {
                //create polygon by fetching the json form assets
                Polygon polygon = (Polygon) Polygon.fromJson(new JsonReader()
                        .getJsonFromAssets(mContext, name + ".json").toString());

                polygonGraphics.add(new Graphic(polygon, fillSymbol));
                //polygonOverlay.getGraphics().add(new Graphic(polygon, fillSymbol));

                if (showNames) {
                    District locationEnum = District.valueOf(location.toUpperCase());   //enum values are in upper case,
                    // enum used since location position is in enum declaration
                    Point pt = locationEnum.namePosition();
                    TextSymbol textSymbol = new TextSymbol(10, HelperClass.toSentenceCase(name), textColor,
                            TextSymbol.HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

                    textGraphics.add(new Graphic(pt, textSymbol));
                }

            } catch (NullPointerException e) {
                Log.e(TAG, name + " polygon not found");
            }
        }

        polygonOverlay.getGraphics().addAll(polygonGraphics);
        locationNameOverlay.getGraphics().addAll(textGraphics);
    }

    private int generateRandomColor() {

        int r = new Random().nextInt(200) + 20;
        int g = new Random().nextInt(200) + 20;
        int b = new Random().nextInt(200) + 20;

        return Color.rgb(r, g, b);
    }

    public void removePolygonAndTextOverlays() {

        ListenableList<GraphicsOverlay> overlays = mMapView.getGraphicsOverlays();
        //Toast.makeText(mContext, "Overlay count: " + overlays.size(), Toast.LENGTH_SHORT).show();

        while (overlays.size() >= 4) {
            overlays.remove(overlays.size() - 1);     //remove top until size is less than 5, 4 are defined initially
        }                                                //basemap, country map, district map, district name
    }
}
