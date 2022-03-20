package com.example.prabin.agriculturearcgis.NavigationTasks.Infrastructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabin on 7/31/2018.
 */

public class InfrastructureCSVReader {
    private InputStream inputStream;
    private List<String[]> resultList;

    public InfrastructureCSVReader(Context context) throws IOException {
        this.inputStream = context.getAssets().open("infrastructure.csv");
        this.read();
    }

    public List<Infrastructure> getInfrastructureInfo(String type) {

        List<Infrastructure> list = new ArrayList<>();
        for(String[] row : resultList) {
            if(row.length > 3 /*Check row is not empty*/&& row[0].equals(type)) {
                Infrastructure i = new Infrastructure(row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3]));
                list.add(i);
            }
        }
        return list;
    }

    private void read() {
        resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }
}
