package com.example.prabin.agriculturearcgis.NavigationTasks.Infrastructure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.content.ContextCompat;

import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.example.prabin.agriculturearcgis.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabin on 7/31/2018.
 */

public class DrawPoints {

    private Context mContext;
    private MapView mMapView;
    InfrastructureCSVReader file;

    public DrawPoints(Context mContext) {
        this.mContext = mContext;
        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);

        try {
            file = new InfrastructureCSVReader(mContext);
        } catch (IOException e) {
            Toast.makeText(mContext, "Infrastructure file not found", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void drawPoints(String type, boolean showNames) {

        GraphicsOverlay symbolOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(symbolOverlay);
        List<Graphic> symbolGraphics = new ArrayList<>();

        GraphicsOverlay locationNameOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(locationNameOverlay);
        List<Graphic> nameGraphics = new ArrayList<>();

        GraphicsOverlay mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        PictureMarkerSymbol symbol = getSymbolForType(type);

        List<Infrastructure> infrastructList = file.getInfrastructureInfo(type);

        for (Infrastructure i : infrastructList) {
            Point location = new Point(i.getLongitude(), i.getLatitude(), SpatialReferences.getWgs84());
            Graphic graphic = new Graphic(location, symbol);
            symbolGraphics.add(graphic);

            TextSymbol textSymbol = new TextSymbol(16, "  "+i.getName(), Color.WHITE,
                    TextSymbol.HorizontalAlignment.LEFT, TextSymbol.VerticalAlignment.BOTTOM);

            nameGraphics.add(new Graphic(location, textSymbol));
        }
        symbolOverlay.getGraphics().addAll(symbolGraphics);
        if (showNames) locationNameOverlay.getGraphics().addAll(nameGraphics);
    }

    private PictureMarkerSymbol getSymbolForType(String type) {

        BitmapDrawable drawable;

        switch (type) {
            case "market":
                drawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.market);
                break;

            case "banks":
                drawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.bank);
                break;

            case "suppliers":
                drawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.supplier);
                break;

            default:
                drawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.arcgisruntime_location_display_compass_symbol);
        }
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        symbol.setHeight(30);
        symbol.setWidth(30);

        return symbol;
    }
}
