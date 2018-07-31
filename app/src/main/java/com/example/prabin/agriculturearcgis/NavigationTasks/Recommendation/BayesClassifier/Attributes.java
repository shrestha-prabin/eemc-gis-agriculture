package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.BayesClassifier;

/**
 * Created by Prabin on 7/26/2018.
 */

public class Attributes {

    double tempMin; 
    double tempMax;
    double rhMin;
    double rhMax;
    double rhAvg;
    double rain;

    public Attributes(double tempMin, double tempMax, double rhMin, double rhMax, double rhAvg, double rain) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.rhMin = rhMin;
        this.rhMax = rhMax;
        this.rhAvg = rhAvg;
        this.rain = rain;
    }
}
