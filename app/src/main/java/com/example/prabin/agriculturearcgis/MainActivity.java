package com.example.prabin.agriculturearcgis;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapRotationChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapRotationChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.NavigationChangedEvent;
import com.esri.arcgisruntime.mapping.view.NavigationChangedListener;
import com.example.prabin.agriculturearcgis.Data.District;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private AHBottomNavigation mBottomNavigation;

    private Envelope createEnvelope() {

        Envelope envelope = new Envelope(78.72803, 30.93050, 89.63745, 26.10612, SpatialReferences.getWgs84());

        return envelope;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.main_mapview);
        mBottomNavigation = findViewById(R.id.main_bottom_navigation);

        customizeBottomNavigation();
        addItemsBottomNavigation();

        //remove esri footer
        mMapView.setAttributionTextVisible(false);

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
        mMap.setMinScale(10000000);//zoom out scale
        mMap.setMaxScale(400000);//zoom in scale

        mMapView.setMap(mMap);

        mMapView.setViewpointGeometryAsync(createEnvelope(), 2);
        //mMapView.setViewpoint(new Viewpoint(new Point(28.42039,84.12781, SpatialReferences.getWgs84()), 10000000));


        GraphicsOverlay overlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(overlay);

        District[] locationList = {District.BHAKTAPUR, District.CHITWAN, District.DHADING, District.DOLAKHA,
                District.KATHMANDU, District.KAVREPALANCHOWK, District.LALITPUR, District.MAKWANPUR,
                District.NUWAKOT, District.RAMECHHAP, District.RASUWA, District.SINDHULI, District.SINDUPALCHOWK};

        new DrawPolygon(this).setDistricts(locationList, true);

    }

    private void customizeBottomNavigation() {

        mBottomNavigation.setAccentColor(android.R.color.white);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mBottomNavigation.setColored(true);
        mBottomNavigation.setTranslucentNavigationEnabled(false);
        //mBottomNavigation.setBehaviorTranslationEnabled(false);
    }

    private void addItemsBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_item_1, R.drawable.nav_ic_distribution, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_item_2, R.drawable.nav_ic_geography, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_item_3, R.drawable.nav_ic_road, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_item_4, R.drawable.nav_ic_weather, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.nav_item_5, R.drawable.nav_ic_bulb, R.color.colorPrimary);

        mBottomNavigation.addItem(item1);
        mBottomNavigation.addItem(item2);
        mBottomNavigation.addItem(item3);
        mBottomNavigation.addItem(item4);
        mBottomNavigation.addItem(item5);
    }

    private void handleBottomNavigationTabs() {


        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (!wasSelected) {

                    switch (position) {

                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
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
