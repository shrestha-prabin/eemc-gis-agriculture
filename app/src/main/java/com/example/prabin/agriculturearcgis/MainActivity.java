package com.example.prabin.agriculturearcgis;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.UniqueValueRenderer;
import com.example.prabin.agriculturearcgis.Data.District;
import com.example.prabin.agriculturearcgis.Data.State;
import com.example.prabin.agriculturearcgis.NavigationTasks.GeographyHandler;
import com.example.prabin.agriculturearcgis.NavigationTasks.InfrastructuresHandler;
import com.example.prabin.agriculturearcgis.NavigationTasks.ProductionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private ArcGISMap mMap;

    private AHBottomNavigation mBottomNavigation;

    private Button mBtnLayerSelector;
    private Button mBtnCropSelector;
    private Button mBtnGeoPropertySelector;
    private Button mBtnInfrastructureSelector;

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
        mBtnCropSelector = findViewById(R.id.main_button_crop_select);
        mBtnGeoPropertySelector = findViewById(R.id.main_button_geo_property);
        mBtnInfrastructureSelector = findViewById(R.id.main_button_infrastructure);

        addItemsBottomNavigation();
        handleBottomNavigationTabs();

        //remove esri footer
        mMapView.setAttributionTextVisible(false);

        //hide legend
        findViewById(R.id.main_legend).setVisibility(View.GONE);

        //hide selector buttons of navigation except for first
        mBtnGeoPropertySelector.setVisibility(View.GONE);
        mBtnInfrastructureSelector.setVisibility(View.GONE);

        mMap = new ArcGISMap(Basemap.createLightGrayCanvas());
        mMap.setMinScale(10000000);//zoom out scale
        mMap.setMaxScale(400000);//zoom in scale
        mMapView.setMap(mMap);
        mMapView.setViewpointGeometryAsync(createEnvelope(), 2);
        //mMapView.setViewpoint(new Viewpoint(new Point(28.42039,84.12781, SpatialReferences.getWgs84()), 10000000));

        new DrawPolygon(this).setPolygon("map_nepal");
        showState3Map();

        mBtnLayerSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeBaseMapDialog();
            }
        });


        new ProductionHandler(this);
        new GeographyHandler(this);
        new InfrastructuresHandler(this);

        fabLocationPanActions();

    }

    private void showState3Map() {

        String[] locationList = State.STATE_3.getDistricts();

        List<String> location = new ArrayList<>(Arrays.asList(locationList));
        new DrawPolygon(this).setDistricts(location, Color.rgb(80, 90, 100), true, Color.WHITE);

    }

    private void showChangeBaseMapDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_basemap_select);
        dialog.setTitle("Select Basemap");
        dialog.setCancelable(true);

        Button baseMap1, baseMap2, baseMap3, baseMap4;
        TextView baseMap1Name, baseMap2Name, baseMap3Name, baseMap4Name;

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

    private void addItemsBottomNavigation() {

        //customize
        mBottomNavigation.setAccentColor(android.R.color.white);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mBottomNavigation.setColored(true);
        mBottomNavigation.setTranslucentNavigationEnabled(false);
        //mBottomNavigation.setBehaviorTranslationEnabled(false);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_item_1, R.drawable.nav_ic_distribution, R.color.nav_distribution);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_item_2, R.drawable.nav_ic_geography, R.color.nav_geography);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_item_3, R.drawable.nav_ic_road, R.color.nav_infrastructure);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_item_4, R.drawable.nav_ic_weather, R.color.nav_weather);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.nav_item_5, R.drawable.nav_ic_bulb, R.color.nav_guide);

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
                //Hide all views first
                //Make those visible which are necessary for given navigation item
                mBtnCropSelector.setVisibility(View.GONE);
                mBtnGeoPropertySelector.setVisibility(View.GONE);
                mBtnInfrastructureSelector.setVisibility(View.GONE);

                //hide legend on changing navigation item
                findViewById(R.id.main_legend).setVisibility(View.GONE);

                new DrawPolygon(MainActivity.this).removePolygonAndTextOverlays();

                switch (position) {
                    case 0:
                        mBtnCropSelector.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mBtnGeoPropertySelector.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mBtnInfrastructureSelector.setVisibility(View.VISIBLE);
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

    private void fabLocationPanActions() {

        FloatingActionButton fabLocation = findViewById(R.id.main_locationFAB);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //To be corrected
                /*try {
                    loadFeatureShapeFile();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }*/
                mMapView.setViewpointGeometryAsync(State.STATE_3.envelope(), 0);
                mMapView.setViewpointRotationAsync(0);
            }
        });
    }

    private void loadFeatureShapeFile() {

        String[] reqPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[0]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, reqPermission, 1);
        }

        String shapeFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/waterways/hotosm_npl_waterways_polygons.shp";
        Log.d("Feature", shapeFilePath);

        final ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(shapeFilePath);
        shapefileFeatureTable.loadAsync();
        shapefileFeatureTable.addDoneLoadingListener(() -> {
            if (shapefileFeatureTable.getLoadStatus() == LoadStatus.LOADED) {
                Toast.makeText(this, "Table Loaded", Toast.LENGTH_SHORT).show();

                FeatureLayer shapefileFeatureLayer = new FeatureLayer(shapefileFeatureTable);
                shapefileFeatureLayer.loadAsync();

                //shapefileFeatureLayer.setRenderer(new SimpleRenderer(new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10)));

                // add the feature layer to the map
                mMapView.getMap().getOperationalLayers().add(shapefileFeatureLayer);

                // zoom the map to the extent of the shapefile
                mMapView.setViewpointAsync(new Viewpoint(shapefileFeatureLayer.getFullExtent()));

            } else {
                String error = "Shapefile feature table failed to load: " + shapefileFeatureTable.getLoadError().toString();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
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
