package com.example.prabin.agriculturearcgis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.prabin.agriculturearcgis.HelperClasses.ColorClass;
import com.example.prabin.agriculturearcgis.HelperClasses.ColorRGB;
import com.example.prabin.agriculturearcgis.HelperClasses.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Prabin on 6/19/2018.
 */

public class ProductionHandler {

    private Context mContext;
    private Button mBtnCropSelector;
    private MapView mMapView;

    public ProductionHandler(final Context mContext) {
        this.mContext = mContext;

        mBtnCropSelector = ((Activity) mContext).findViewById(R.id.main_button_crop_select);
        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);

        mBtnCropSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeInputFromUser();
            }
        });
    }

    private void takeInputFromUser() {

        final String[] crops = {"Wheat", "Maize", "Barley", "Millet"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Crops");
        builder.setItems(crops, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String crop = crops[i];
                mBtnCropSelector.setText(crop);
                ColorRGB color = new ColorRGB(40, 180, 50);

                new DrawPolygon(mContext).removePolygonAndTextOverlays();
                paintMapForCrop(crop, color);
            }
        });
        builder.show();
    }

    private void paintMapForCrop(String crop, final ColorRGB color) {

        FirebaseDatabase.getInstance().getReference()
                .child("production_data/" + crop.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, Long> data = (HashMap<String, Long>) dataSnapshot.getValue();

                        //data = HelperClass.sortByComparator(data);      //sort in ascending order by value

                        //for debug only
                        for (Map.Entry<String, Long> entry : data.entrySet()) {
                            String key = entry.getKey();
                            long value = entry.getValue();
                            Log.d("VALUES", key + "\t" + value);
                        }

                        long maxValue = HelperClass.maxValueHashMap(data);  //27856
                        long legendMaxValue = HelperClass.getNearestValueInThousand(maxValue);  //27000

                        //Each list holds data for particular range
                        List<String> list1 = new ArrayList<>();
                        List<String> list2 = new ArrayList<>();
                        List<String> list3 = new ArrayList<>();
                        List<String> list4 = new ArrayList<>();
                        List<String> list5 = new ArrayList<>();
                        List<String> list6 = new ArrayList<>();

                        for (Map.Entry<String, Long> entry : data.entrySet()) {

                            String key = entry.getKey();
                            long value = entry.getValue();

                            int factor = (int) legendMaxValue / 5;  //6750
                            if (value >= legendMaxValue) {  //value >= 27856
                                list1.add(key);
                                Log.d("Value_List_1", key + "\t" + value);
                            } else if (value >= legendMaxValue - factor && value < legendMaxValue) { //27856 > value > 21106
                                list2.add(key);
                                Log.d("Value_List_2", key + "\t" + value);
                            } else if (value >= legendMaxValue - 2 * factor && value < legendMaxValue - factor) {   //21106 > value > 13500
                                list3.add(key);
                                Log.d("Value_List_3", key + "\t" + value);
                            } else if (value >= legendMaxValue - 3 * factor && value < legendMaxValue - 2 * factor) { //13500 > value > 6750
                                list4.add(key);
                                Log.d("Value_List_4", key + "\t" + value);
                            } else if (value <= legendMaxValue - 4 * factor && value > 0) { //value < 6750
                                list5.add(key);
                                Log.d("Value_List_5", key + "\t" + value);
                            } else if (value == 0) {
                                list6.add(key);     //value = 0
                                Log.d("Value_List_6", key + "\t" + value);
                            }
                        }


                        //Six color shades, color shade at 6 is white
                        List<Integer> colorShades = ColorClass.getColorShades(color);

                        //draw polygons
                        new DrawPolygon(mContext).setDistricts(list1, colorShades.get(0), true);
                        new DrawPolygon(mContext).setDistricts(list2, colorShades.get(1), true);
                        new DrawPolygon(mContext).setDistricts(list3, colorShades.get(2), true);
                        new DrawPolygon(mContext).setDistricts(list4, colorShades.get(3), true);
                        new DrawPolygon(mContext).setDistricts(list5, colorShades.get(4), true);
                        new DrawPolygon(mContext).setDistricts(list6, colorShades.get(5), true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
