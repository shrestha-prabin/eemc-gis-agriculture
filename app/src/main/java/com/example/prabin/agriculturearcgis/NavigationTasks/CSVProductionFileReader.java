package com.example.prabin.agriculturearcgis.NavigationTasks;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prabin on 7/26/2018.
 */

public class CSVProductionFileReader {
    private InputStream inputStream;
    private List<String[]> resultList;

    public CSVProductionFileReader(Context context, String year) throws IOException {
        this.inputStream = context.getAssets().open("production_" + year + ".csv");
        this.read();
    }

    public List<String[]> read() {
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
        return resultList;
    }

    public HashMap<String, Long> getDataForCrop(String crop) {

        HashMap<String, Long> data = new HashMap<>();
        int targetCol = getPositionForColumn(crop);

        for(int i =1;i<resultList.size();i++) {

            String[] fileRow = resultList.get(i);

            String location = fileRow[0];
            Long productionValue = Long.parseLong(fileRow[targetCol]);

            data.put(location, productionValue);
        }
        return data;
    }

    private int getPositionForColumn(String crop) {
        int i = 0;

        String[] headerRow = resultList.get(0);
        for (String s : headerRow) {
            if (s.trim().equals(crop)) {
                return i;
            }
            i++;
        }
        return 1;
    }

}
