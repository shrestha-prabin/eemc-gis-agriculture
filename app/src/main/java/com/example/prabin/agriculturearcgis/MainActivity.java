package com.example.prabin.agriculturearcgis;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private ArcGISMap mMap;
    private AHBottomNavigation mBottomNavigation;

    private Button mBtnLayerSelector;
    private Button mBtnLegendSelector;
    private Button mBtnCropSelector;
    private Button mBtnGeoPropertySelector;

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

        mBtnLayerSelector = findViewById(R.id.main_button_layer_select);
        mBtnLegendSelector = findViewById(R.id.main_button_legend_toggle);
        mBtnCropSelector = findViewById(R.id.main_button_crop_select);
        mBtnGeoPropertySelector = findViewById(R.id.main_button_geo_property);

        customizeBottomNavigation();
        addItemsBottomNavigation();
        handleBottomNavigationTabs();

        //remove esri footer
        mMapView.setAttributionTextVisible(false);

        //hide legend
        findViewById(R.id.main_legend).setVisibility(View.GONE);

        //hide selector buttons of navigation except for first
        mBtnGeoPropertySelector.setVisibility(View.GONE);

        mMapView.addMapRotationChangedListener(new MapRotationChangedListener() {
            @Override
            public void mapRotationChanged(MapRotationChangedEvent mapRotationChangedEvent) {
                mMapView.setViewpointRotationAsync(0);
            }
        });
        /*
        mMapView.addNavigationChangedListener(new NavigationChangedListener() {
            @Override
            public void navigationChanged(NavigationChangedEvent navigationChangedEvent) {
                //mMapView.setViewpointGeometryAsync(createEnvelope(), 2);
            }
        });*/

        mMap = new ArcGISMap(Basemap.createLightGrayCanvas());
        mMap.setMinScale(10000000);//zoom out scale
        mMap.setMaxScale(400000);//zoom in scale

        mMapView.setMap(mMap);

        mMapView.setViewpointGeometryAsync(createEnvelope(), 2);
        //mMapView.setViewpoint(new Viewpoint(new Point(28.42039,84.12781, SpatialReferences.getWgs84()), 10000000));


        GraphicsOverlay overlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(overlay);

        String[] locationList = {District.BHAKTAPUR.name(), District.CHITWAN.name(), District.DHADING.name(), District.DOLAKHA.name(),
                District.KATHMANDU.name(), District.KAVREPALANCHOWK.name(), District.LALITPUR.name(), District.MAKWANPUR.name(),
                District.NUWAKOT.name(), District.RAMECHHAP.name(), District.RASUWA.name(), District.SINDHULI.name(), District.SINDUPALCHOWK.name()};

        List<String> location = new ArrayList<>(Arrays.asList(locationList));
        new DrawPolygon(this).setDistricts(location, Color.rgb(80,90,100), true, Color.WHITE);

        mBtnLayerSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeBaseMapDialog();
            }
        });

        new ProductionHandler(this);
        new GeographyHandler(this);
    }

    private void showChangeBaseMapDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_basemap_select);
        dialog.setTitle("Select Basemap");
        dialog.setCancelable(true);

        Button baseMap1, baseMap2, baseMap3, baseMap4;
        TextView baseMap1Name, baseMap2Name, baseMap3Name,baseMap4Name;

        baseMap1 = dialog.findViewById(R.id.dialog_basemap_1);
        baseMap2 = dialog.findViewById(R.id.dialog_basemap_2);
        baseMap3 = dialog.findViewById(R.id.dialog_basemap_3);
        baseMap4 = dialog.findViewById(R.id.dialog_basemap_4);

        baseMap1Name = dialog.findViewById(R.id.dialog_basemap_1_tv);
        baseMap2Name = dialog.findViewById(R.id.dialog_basemap_2_tv);
        baseMap3Name = dialog.findViewById(R.id.dialog_basemap_3_tv);
        baseMap4Name = dialog.findViewById(R.id.dialog_basemap_4_tv);

        baseMap1Name.setText("Light Gray Canvas");
        baseMap2Name.setText("Topographic");
        baseMap3Name.setText("Imagery");
        baseMap4Name.setText("Open Street Map");

        baseMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setBasemap(Basemap.createLightGrayCanvas());
                dialog.dismiss();
            }
        });

        baseMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setBasemap(Basemap.createTopographic());
                dialog.dismiss();
            }
        });

        baseMap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setBasemap(Basemap.createImagery());
                dialog.dismiss();
            }
        });

        baseMap4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setBasemap(Basemap.createOpenStreetMap());
                dialog.dismiss();
            }
        });

        dialog.show();
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

                //Hide all views first and making those visible which are necessary for given navigation item
                mBtnCropSelector.setVisibility(View.GONE);
                mBtnLegendSelector.setVisibility(View.GONE);
                mBtnGeoPropertySelector.setVisibility(View.GONE);


                switch (position) {
                    case 0:
                        mBtnCropSelector.setVisibility(View.VISIBLE);
                        mBtnLegendSelector.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mBtnGeoPropertySelector.setVisibility(View.VISIBLE);
                        mBtnLegendSelector.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        //loadFeatureShapeFile();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void loadFeatureShapeFile() {
        final ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable("assets/trans_pt.shp");
        shapefileFeatureTable.loadAsync();
        shapefileFeatureTable.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (shapefileFeatureTable.getLoadStatus() == LoadStatus.LOADED) {
                    // create a feature layer to display the shapefile
                    FeatureLayer shapefileFeatureLayer = new FeatureLayer(shapefileFeatureTable);

                    // add the feature layer to the map
                    mMapView.getMap().getOperationalLayers().add(shapefileFeatureLayer);

                    // zoom the map to the extent of the shapefile
                    //mMapView.setViewpointAsync(new Viewpoint(shapefileFeatureLayer.getFullExtent()));
                } else {
                    String error = "Shapefile feature table failed to load: " + shapefileFeatureTable.getLoadError().toString();
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                }
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
