package com.example.prabin.agriculturearcgis.HelperClasses;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Prabin on 3/15/2018.
 */

public class JsonReader {

    public JSONObject getJsonFromAssets(Context context, String path) {

        String json = null;
        JSONObject obj = null;

        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            obj = new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
