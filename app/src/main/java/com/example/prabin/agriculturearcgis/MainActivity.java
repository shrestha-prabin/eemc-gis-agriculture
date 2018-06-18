package com.example.prabin.agriculturearcgis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapRotationChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapRotationChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.NavigationChangedEvent;
import com.esri.arcgisruntime.mapping.view.NavigationChangedListener;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    private Envelope createEnvelope() {

        Envelope envelope = new Envelope(78.72803, 30.93050, 89.63745, 26.10612, SpatialReferences.getWgs84());

        return envelope;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.main_mapview);

        //remove esri footer
        mMapView.setAttributionTextVisible(false);
        mMapView.setViewpointGeometryAsync(createEnvelope(), 2);

        mMapView.addMapRotationChangedListener(new MapRotationChangedListener() {
            @Override
            public void mapRotationChanged(MapRotationChangedEvent mapRotationChangedEvent) {
                mMapView.setViewpointRotationAsync(0);
            }
        });

        mMapView.addNavigationChangedListener(new NavigationChangedListener() {
            @Override
            public void navigationChanged(NavigationChangedEvent navigationChangedEvent) {
                //mMapView.setViewpointGeometryAsync(createEnvelope(), 2);
            }
        });

        ArcGISMap mMap = new ArcGISMap(Basemap.createLightGrayCanvas());

        //mMap.setMinScale(10000000);
        //mMap.setMaxScale(4000000);

        mMapView.setMap(mMap);

//        mMapView.setViewpoint(new Viewpoint(new Point(28.42039,84.12781, SpatialReferences.getWebMercator()), 10000000));


        GraphicsOverlay overlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(overlay);

        District[] locationList = {District.BHAKTAPUR, District.CHITWAN, District.DHADING, District.DOLAKHA,
                District.KATHMANDU, District.KAVREPALANCHOWK, District.LALITPUR, District.MAKWANPUR,
                District.NUWAKOT, District.RAMECHHAP, District.RASUWA, District.SINDHULI, District.SINDUPALCHOWK};

        new DrawPolygon(this).setDistricts(locationList, true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }
}
