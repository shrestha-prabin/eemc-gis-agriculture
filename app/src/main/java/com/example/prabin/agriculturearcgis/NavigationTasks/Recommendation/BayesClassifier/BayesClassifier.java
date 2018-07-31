package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.BayesClassifier;

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

public class BayesClassifier {
    private InputStream inputStream;
    private List<String[]> dataList;

    private String testCrop;

    public BayesClassifier(Context context) throws IOException {
        this.inputStream = context.getAssets().open("train_data.csv");
        this.read();
    }

    public void read() {
        dataList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                dataList.add(row);
            }
        } catch (IOException e) {
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public boolean testForCrop(String testCrop, Attributes attr) {

        this.testCrop = "plant_" + testCrop.toLowerCase();

        double testTempMax = attr.tempMax;
        double testTempMin = attr.tempMin;
        double testRHMax = attr.rhMax;
        double testRHMin = attr.rhMin;
        double testRHAvg = attr.rhAvg;
        double testRain = attr.rain;

        double p_maxTemp_yes = probabilityDistribution("max_temp", "yes", testTempMax);
        double p_minTemp_yes = probabilityDistribution("min_temp", "yes", testTempMin);
        double p_maxRH_yes = probabilityDistribution("rh_max", "yes", testRHMax);
        double p_minRH_yes = probabilityDistribution("rh_min", "yes", testRHMin);
        double p_avgRH_yes = probabilityDistribution("rh_avg", "yes", testRHAvg);
        double p_rain_yes = probabilityDistribution("rainfall", "yes", testRain);

        double p_maxTemp_no = probabilityDistribution("max_temp", "no", testTempMax);
        double p_minTemp_no = probabilityDistribution("min_temp", "no", testTempMin);
        double p_maxRH_no = probabilityDistribution("rh_max", "no", testRHMax);
        double p_minRH_no = probabilityDistribution("rh_min", "no", testRHMin);
        double p_avgRH_no = probabilityDistribution("rh_avg", "no", testRHAvg);
        double p_rain_no = probabilityDistribution("rainfall", "no", testRain);

        double posterior_yes = 0.5 * p_maxTemp_yes * p_minTemp_yes * p_maxRH_yes * p_minRH_yes * p_avgRH_yes * p_rain_yes;
        double posterior_no = 0.5 * p_maxTemp_no * p_minTemp_no * p_maxRH_no * p_minRH_no * p_avgRH_no * p_rain_no;

        if (posterior_yes >= posterior_no) {
            Log.d("RESULT", testCrop + " Positive");
            return true;
        } else {
            Log.d("RESULT", testCrop + " Negative");
            return false;
        }
    }

    private double probabilityDistribution(String attribute, String decision, Double testValue) {
        int attrIndex = getIndex(attribute);
        int decisionIndex = getIndex(testCrop);

        List<Double> attrWithDecision = new ArrayList<>();

        for (String[] row : dataList) {
            if (row[decisionIndex].equals(decision)) {
                attrWithDecision.add(Double.parseDouble(row[attrIndex]));
            }
        }

        double mean = calculateMean(attrWithDecision);
        double variance = calculateVariance(attrWithDecision);

        return 1 / (Math.sqrt(2 * Math.PI * variance))
                * Math.exp((-1 * Math.pow(testValue - mean, 2)) / (2 * variance));
    }

    private double calculateMean(List<Double> dataList) {

        double sum = 0;
        double n = dataList.size();
        for (Double d : dataList) {
            sum += d;
        }
        return sum / n;
    }

    private double calculateVariance(List<Double> dataList) {

        double sumX = 0;
        double sumX2 = 0;

        double n = dataList.size();

        for (Double x : dataList) {
            sumX += x;
            sumX2 += x * x;
        }
        return (sumX2 - Math.pow(sumX, 2) / n) / (n - 1);
    }

    private int getIndex(String title) {
        String[] titles = dataList.get(0);
        for (int i = 0; i < titles.length; i++) {
            if (titles[i].equals(title)) {
                return i;
            }
        }
        return 0;
    }
}
