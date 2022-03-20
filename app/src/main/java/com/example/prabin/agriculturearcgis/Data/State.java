package com.example.prabin.agriculturearcgis.Data;

import android.util.Log;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;

/**
 * Created by Prabin on 6/18/2018.
 */

public enum State {

    STATE_1("Purbanchal"),
    STATE_2("Mithila"),
    STATE_3("Nepalmandal"),
    STATE_4("Gandaki"),
    STATE_5("Lumbini"),
    STATE_6("Karnali"),
    STATE_7("Sudur-Paschim");

    private String name;

    State(String name) {
        this.name = name;
    }

    public String stateName() {
        return name;
    }

    public Point namePosition() {

        int x, y;
        switch (this) {

            case STATE_1:
                x = 86;
                y = 27;
                break;

            case STATE_2:
                x = 1;
                y = 1;
                break;

            case STATE_3:
                x = 86;
                y = 27;
                break;

            case STATE_4:
                x = 1;
                y = 1;
                break;

            case STATE_5:
                x = 1;
                y = 1;
                break;

            case STATE_6:
                x = 1;
                y = 1;
                break;

            case STATE_7:
                x = 1;
                y = 1;
                break;

            default:
                x = 0;
                y = 0;
                break;
        }

        return new Point(x, y);

    }

    public Envelope envelope() {

        double x1, y1, x2, y2;

        switch (this) {


            case STATE_1:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case STATE_2:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case STATE_3:
                x1 = 83.63892;
                y1 = 28.74840;
                x2 = 86.96777;
                y2 = 26.31311;
                break;

            case STATE_4:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case STATE_5:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;
            case STATE_6:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;
            case STATE_7:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            default:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;
        }

        return new Envelope(x1, y1, x2, y2, SpatialReferences.getWgs84());
    }

    public String[] getDistricts() {

        String[] districtList;

        switch (this) {
            case STATE_3:
                districtList = new String[]{District.BHAKTAPUR.name(), District.CHITWAN.name(), District.DHADING.name(),
                        District.DOLAKHA.name(), District.KATHMANDU.name(), District.KAVREPALANCHOWK.name(),
                        District.LALITPUR.name(), District.MAKWANPUR.name(), District.NUWAKOT.name(),
                        District.RASUWA.name(), District.RAMECHHAP.name(), District.SINDHULI.name(),
                        District.SINDUPALCHOWK.name()};
                break;
            default:
                Log.e("State_List", "Error");
                districtList = null;
        }

        return districtList;
    }

}
