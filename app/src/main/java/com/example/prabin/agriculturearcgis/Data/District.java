package com.example.prabin.agriculturearcgis.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;

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

    public Point namePosition() {
        double x, y;
        switch (this) {
            case BHAKTAPUR:
                x = 85.441398;
                y = 27.677074;
                break;
            case CHITWAN:
                x = 84.397460;
                y = 27.538103;
                break;
            case DHADING:
                x = 84.900669;
                y = 27.789098;
                break;
            case DOLAKHA:
                x = 86.240614;
                y = 27.770542;
                break;
            case KATHMANDU:
                x = 85.389680;
                y = 27.752374;
                break;
            case KAVREPALANCHOWK:
                x = 85.62315212;
                y = 27.54538605;
                break;
            case LALITPUR:
                x = 85.338210;
                y = 27.492351;
                break;
            case MAKWANPUR:
                x = 85.0550812;
                y = 27.43653885;
                break;
            case NUWAKOT:
                x = 85.223430;
                y = 27.884976;
                break;
            case RAMECHHAP:
                x = 86.012949;
                y = 27.411731;
                break;
            case RASUWA:
                x = 85.459565;
                y = 28.200199;
                break;
            case SINDHULI:
                x = 85.779532;
                y = 27.270524;
                break;
            case SINDUPALCHOWK:
                x = 85.7063055;
                y = 27.9259127;
                break;
            default:
                x = 0;
                y = 0;
                break;
        }
        return new Point(x, y, SpatialReferences.getWgs84());
    }

    public Envelope envelope() {
        /*x1,y1             */
        /*                  */
        /*                  */
        /*             x2,y2*/

        double x1, y1, x2, y2;

        switch (this) {

            case BHAKTAPUR:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case CHITWAN:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case DHADING:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case DOLAKHA:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case KATHMANDU:
                x1 = 85.16670;
                y1 = 27.84758;
                x2 = 85.58281;
                y2 = 27.5435;
                break;

            case KAVREPALANCHOWK:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case LALITPUR:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case MAKWANPUR:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case NUWAKOT:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case SINDHULI:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case RASUWA:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case RAMECHHAP:
                x1 = 1;
                y1 = 1;
                x2 = 2;
                y2 = 2;
                break;

            case SINDUPALCHOWK:
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
        }

        return new Envelope(x1, y1, x2, y2, SpatialReferences.getWgs84());
    }
}
