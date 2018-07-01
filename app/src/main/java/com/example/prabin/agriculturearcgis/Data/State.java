package com.example.prabin.agriculturearcgis.Data;

import android.util.Log;

import com.esri.arcgisruntime.geometry.Point;

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

    public String[] getDistricts() {

        String[] districtList;

        switch (this) {
            case STATE_3:
                districtList = new String[]{District.BHAKTAPUR.name(), District.CHITWAN.name(), District.DHADING.name(),
                        District.DOLAKHA.name(), District.KATHMANDU.name(), District.KAVREPALANCHOWK.name(),
                        District.LALITPUR.name(), District.MAKWANPUR.name(), District.NUWAKOT.name(),
                        District.RAMECHHAP.name(), District.RAMECHHAP.name(), District.SINDHULI.name(),
                        District.SINDUPALCHOWK.name()};
                break;
            default:
                Log.e("State_List", "Error");
                districtList = null;
        }

        return districtList;
    }

}
