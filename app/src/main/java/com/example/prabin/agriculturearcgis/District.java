package com.example.prabin.agriculturearcgis;

import android.support.annotation.NonNull;
import android.util.Log;

import com.esri.arcgisruntime.geometry.Point;

/**
 * Created by Prabin on 6/16/2018.
 */

public enum District {

    BHAKTAPUR,
    CHITWAN,
    DHADING,
    DOLAKHA,
    KATHMANDU,
    KAVREPALANCHOWK,
    LALITPUR,
    MAKWANPUR,
    NUWAKOT,
    RAMECHHAP,
    RASUWA,
    SINDHULI,
    SINDUPALCHOWK;

    Point namePosition() {
        int x, y;
        switch (this) {
            case BHAKTAPUR:
                x = 9510;
                y = 3207;
                break;
            case CHITWAN:
                x = 9405;
                y = 3195;
                break;
            case DHADING:
                x = 9450;
                y = 3245;
                break;
            case DOLAKHA:
                x = 9600;
                y = 3215;
                break;
            case KATHMANDU:
                x = 9505;
                y = 3215;
                break;
            case KAVREPALANCHOWK:
                x = 9530;
                y = 3190;
                break;
            case LALITPUR:
                x = 9500;
                y = 3185;
                break;
            case MAKWANPUR:
                x = 9460;
                y = 3180;
                break;
            case NUWAKOT:
                x = 9485;
                y = 3230;
                break;
            case RAMECHHAP:
                x = 9580;
                y = 3175;
                break;
            case RASUWA:
                x = 9510;
                y = 3275;
                break;
            case SINDHULI:
                x = 9560;
                y = 3150;
                break;
            case SINDUPALCHOWK:
                x = 9545;
                y = 3240;
                break;
            default:
                x = 0;
                y = 0;
                break;
        }
        return new Point(x * 1000, y * 1000);
    }
}
