package com.example.prabin.agriculturearcgis.HelperClasses;


import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabin on 6/21/2018.
 */

public class ColorClass {

    public static List<Integer> getColorShades(ColorRGB color) {

        int alpha = 255;
        int red = color.red;
        int green = color.green;
        int blue = color.blue;

        List<Integer> colors = new ArrayList<>();

        int rFactor = Math.round((255 - red) / 6);
        int gFactor = Math.round((255 - green) / 6);
        int bFactor = Math.round((255 - blue) / 6);

        for (int i = 1; i <= 6; i++) {

            int r = red + rFactor * i;
            int g = green + gFactor * i;
            int b = blue + bFactor * i;

            colors.add(Color.argb(alpha, r, g, b));

            Log.d("ColorClass_Colors", r + "\t" + g + "\t" + b);
        }

        return colors;
    }
}
