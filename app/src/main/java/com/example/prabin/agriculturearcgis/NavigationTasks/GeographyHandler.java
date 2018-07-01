package com.example.prabin.agriculturearcgis.NavigationTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.example.prabin.agriculturearcgis.R;

/**
 * Created by Prabin on 6/19/2018.
 */

public class GeographyHandler {

    private Context mContext;
    private Button mBtnGeoPropertySelector;
    private  Button mBtnLegendToggle;

    public GeographyHandler(Context mContext) {
        this.mContext = mContext;
        mBtnGeoPropertySelector = ((Activity)mContext).findViewById(R.id.main_button_geo_property);
        mBtnLegendToggle = ((Activity)mContext).findViewById(R.id.main_button_legend_toggle);
        mBtnLegendToggle.setVisibility(View.GONE);

        mBtnGeoPropertySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeInputFromUser();
            }
        });
    }

    private void takeInputFromUser() {

        final String[] properties = {"Soil", "Temperature", "Humidity", "Water Availability"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Geo-Property");
        builder.setItems(properties, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBtnGeoPropertySelector.setText(properties[i]);

            }
        });
        builder.show();
    }
}
