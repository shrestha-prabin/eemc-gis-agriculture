package com.example.prabin.agriculturearcgis.NavigationTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.prabin.agriculturearcgis.Data.DistrictData;
import com.example.prabin.agriculturearcgis.DrawPolygon;
import com.example.prabin.agriculturearcgis.HelperClasses.ColorClass;
import com.example.prabin.agriculturearcgis.HelperClasses.ColorRGB;
import com.example.prabin.agriculturearcgis.HelperClasses.HelperClass;
import com.example.prabin.agriculturearcgis.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private Button mBtnLegendToggle;
    private Button mBtnNameToggle;
    private MapView mMapView;

    public ProductionHandler(final Context mContext) {
        this.mContext = mContext;

        mBtnCropSelector = ((Activity) mContext).findViewById(R.id.main_button_crop_select);
        mBtnLegendToggle = ((Activity) mContext).findViewById(R.id.main_button_legend_toggle);
        mBtnLegendToggle.setVisibility(View.GONE);
        mBtnNameToggle = ((Activity)mContext).findViewById(R.id.main_button_name_toggle);

        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);

        mBtnCropSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeInputFromUser();
            }
        });

        mBtnLegendToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLegend();
            }
        });
        
        mBtnNameToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Toggle Name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLegend() {
        if (((Activity) mContext).findViewById(R.id.main_legend).getVisibility() == View.VISIBLE) {
            ((Activity) mContext).findViewById(R.id.main_legend).setVisibility(View.GONE);
        } else {
            ((Activity) mContext).findViewById(R.id.main_legend).setVisibility(View.VISIBLE);
        }
    }

    private void takeInputFromUser() {

        final String[] crops = mContext.getResources().getStringArray(R.array.crop_list);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Crops");
        builder.setItems(crops, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String crop = crops[i];
                mBtnCropSelector.setText(crop);
                ColorRGB color = new ColorRGB(64, 178, 49);

                new DrawPolygon(mContext).removePolygonAndTextOverlays();
                paintMapForCrop(crop, color);
                mBtnLegendToggle.setVisibility(View.VISIBLE);
            }
        });
        builder.show();
    }

    private void paintMapForCrop(String crop, final ColorRGB color) {

        final DatabaseReference productionDataRef = FirebaseDatabase.getInstance().getReference()
                .child("production_data/" + crop.toLowerCase());
        productionDataRef.keepSynced(true);

        productionDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    HashMap<String, Long> data = (HashMap<String, Long>) dataSnapshot.getValue();
                    //data = HelperClass.sortByComparator(data);      //sort in ascending order by value

                    //for debug only
                    /*for (Map.Entry<String, Long> entry : data.entrySet()) {
                        String key = entry.getKey();
                        long value = entry.getValue();
                        Log.d("VALUES", key + "\t" + value);
                    }*/

                    long maxValue = HelperClass.maxValueHashMap(data);  //27856
                    long legendMaxValue = HelperClass.getNearestValueInThousand(maxValue);  //27000
                    int factor = (int) legendMaxValue / 5;  //5400

                    long legendLv6 = legendMaxValue;                //27000
                    long legendLv5 = legendMaxValue - factor;       //21600
                    long legendLv4 = legendMaxValue - 2 * factor;    //16200
                    long legendLv3 = legendMaxValue - 3 * factor;    //10800
                    long legendLv2 = legendMaxValue - 4 * factor;    //5400
                    long legendLv1 = legendMaxValue - 5 * factor;     //0

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

                        if (value >= legendLv6) {                                                //value >= 27000
                            list1.add(key);
                        } else if (value >= legendLv5 && value < legendLv6) {                    //27000 >= value > 21600
                            list2.add(key);
                        } else if (value >= legendLv4 && value < legendLv5) {                   //21600 >= value > 16200
                            list3.add(key);
                        } else if (value >= legendLv3 && value < legendLv4) {                   //16200 >= value > 10800
                            list4.add(key);
                        } else if (value <= legendLv2 && value > legendLv1) {                    //0 < value <= 5400
                            list5.add(key);
                        } else if (value == legendLv1) {                                          //value = 0
                            list6.add(key);
                        }
                    }

                    //Six color shades, color shade at 6th is white
                    List<Integer> colorShades = ColorClass.getColorShades(color);

                    //draw polygons
                    new DrawPolygon(mContext).setDistricts(list1, colorShades.get(0), true, Color.BLACK);
                    new DrawPolygon(mContext).setDistricts(list2, colorShades.get(1), true, Color.BLACK);
                    new DrawPolygon(mContext).setDistricts(list3, colorShades.get(2), true, Color.BLACK);
                    new DrawPolygon(mContext).setDistricts(list4, colorShades.get(3), true, Color.BLACK);
                    new DrawPolygon(mContext).setDistricts(list5, colorShades.get(4), true, Color.BLACK);
                    new DrawPolygon(mContext).setDistricts(list6, colorShades.get(5), true, Color.BLACK);

                    showLegend(legendLv6, legendLv5, legendLv4, legendLv3, legendLv2, legendLv1, colorShades);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showLegend(long lv6, long lv5, long lv4, long lv3, long lv2, long lv1, List<Integer> colorShades) {

        ImageView iv1, iv2, iv3, iv4, iv5, iv6;
        TextView tv1, tv2, tv3, tv4, tv5, tv6;

        iv1 = ((Activity) mContext).findViewById(R.id.legend_iv_1);
        iv2 = ((Activity) mContext).findViewById(R.id.legend_iv_2);
        iv3 = ((Activity) mContext).findViewById(R.id.legend_iv_3);
        iv4 = ((Activity) mContext).findViewById(R.id.legend_iv_4);
        iv5 = ((Activity) mContext).findViewById(R.id.legend_iv_5);
        iv6 = ((Activity) mContext).findViewById(R.id.legend_iv_6);

        tv1 = ((Activity) mContext).findViewById(R.id.legend_tv_1);
        tv2 = ((Activity) mContext).findViewById(R.id.legend_tv_2);
        tv3 = ((Activity) mContext).findViewById(R.id.legend_tv_3);
        tv4 = ((Activity) mContext).findViewById(R.id.legend_tv_4);
        tv5 = ((Activity) mContext).findViewById(R.id.legend_tv_5);
        tv6 = ((Activity) mContext).findViewById(R.id.legend_tv_6);

        iv1.setBackgroundColor(colorShades.get(5));
        iv2.setBackgroundColor(colorShades.get(4));
        iv3.setBackgroundColor(colorShades.get(3));
        iv4.setBackgroundColor(colorShades.get(2));
        iv5.setBackgroundColor(colorShades.get(1));
        iv6.setBackgroundColor(colorShades.get(0));

        tv6.setText("Above " + lv6);
        tv5.setText(lv5 + " - " + lv4);
        tv4.setText(lv4 + " - " + lv3);
        tv3.setText(lv3 + " - " + lv2);
        tv2.setText("Less than " + lv2);
        tv1.setText("No Production");
        ((Activity) mContext).findViewById(R.id.main_legend).setVisibility(View.VISIBLE);
    }

}
