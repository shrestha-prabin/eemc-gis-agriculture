package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabin on 7/26/2018.
 */

public class CSVFileReader  {
    private InputStream inputStream;
    private List<String[]> resultList;

    public CSVFileReader(Context context, String location) throws IOException {
        this.inputStream = context.getAssets().open("data4graph_" + location + ".csv");
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

    public double[] getDataFromFile(String year, String type) {

        double[] data =  new double[12];
        int targetRow = getPositionForRow(year, type);

        for(int i = 0;i<12;i++) {
            data[i] = Double.parseDouble(resultList.get(targetRow)[i+3]);
        }
        return data;
    }

    public int getPositionForRow(String year, String type) {

        int i = 0;
        for (String[] row : resultList) {
            if(row.length > 10 && row[1].equals(year) && row[2].equals(type)) {
                return i;
            }
            i++;
        }
        return 0;
    }

}
