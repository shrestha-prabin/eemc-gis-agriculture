package com.example.prabin.agriculturearcgis.NavigationTasks.Infrastructure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.prabin.agriculturearcgis.DrawPolygon;
import com.example.prabin.agriculturearcgis.HelperClasses.HelperClass;
import com.example.prabin.agriculturearcgis.R;

import java.util.ArrayList;

/**
 * Created by Prabin on 7/1/2018.
 */

public class InfrastructuresHandler {

    private MapView mMapView;
    private Context mContext;
    private Button mBtnInfrastructureSelector, mBtnNamesToggle;

    private boolean showNames = true;
    private ArrayList<String> selectedItems = new ArrayList<>();

    public InfrastructuresHandler(Context mContext) {
        this.mContext = mContext;

        mMapView = ((Activity) mContext).findViewById(R.id.main_mapview);
        mBtnInfrastructureSelector = ((Activity) mContext).findViewById(R.id.main_button_infrastructure);
        mBtnNamesToggle = ((Activity) mContext).findViewById(R.id.main_button_name_toggle);

        mBtnInfrastructureSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeInputFromUser();
            }
        });

        mBtnNamesToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNames = !showNames;
                updateMap();
            }
        });
    }

    private void takeInputFromUser() {

        final String[] items = new String[]{"Roads", "Rivers", "Irrigation Canals", "Suppliers", "Market", "Organizations", "Agricultural Banks"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Items to be Displayed");

        ArrayList<Boolean> checkedItems = new ArrayList<>();

        //preserve selected items state in the dialog
        for (String s : items) {
            boolean flag = false;
            for (String i : selectedItems) {
                if (s.equals(i)) {
                    checkedItems.add(true);         //if
                    flag = true;        //checks string from "items" with "checkedItems", if matches add 'true' to list and sets flag
                    break;
                }
            }
            if (!flag) {                //if flag was not set, no match occurred, hence add 'false' to the list
                checkedItems.add(false);            //else
            }
        }
        builder.setMultiChoiceItems(items, HelperClass.toPrimitiveArray(checkedItems), new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    selectedItems.add(items[i]);
                } else if (selectedItems.contains(items[i])) {
                    selectedItems.remove(items[i]);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateMap();
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateMap() {
        new DrawPolygon(mContext).removePolygonAndTextOverlays();

        for (String s : selectedItems) {

            switch (s) {
                case "Roads":
                    break;

                case "Rivers":
                    break;

                case "Irrigation Canals":
                    break;

                case "Suppliers":
                    new DrawPoints(mContext).drawPoints("suppliers", showNames);
                    break;

                case "Market":
                    new DrawPoints(mContext).drawPoints("market", showNames);
                    break;

                case "Organizations":
                    break;

                case "Agricultural Banks":
                    new DrawPoints(mContext).drawPoints("banks", showNames);
                    break;

                default:
                    break;
            }
        }
    }
}
