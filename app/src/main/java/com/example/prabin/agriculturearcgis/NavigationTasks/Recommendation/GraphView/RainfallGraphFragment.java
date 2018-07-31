package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prabin.agriculturearcgis.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class RainfallGraphFragment extends Fragment {

    BarChart rainfallBarChart;
    String location;
    String year;

    public RainfallGraphFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rainfall_graph, container, false);

        rainfallBarChart = v.findViewById(R.id.graph_rainfall_barchart);
        location = getArguments().getString("location").toLowerCase();
        year = getArguments().getString("year").toLowerCase();

        customizeChart();
        try {
            populateData();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Rainfall data not found for " + location + "/" + year , Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void customizeChart() {
        rainfallBarChart.getDescription().setEnabled(false);
        rainfallBarChart.setScaleXEnabled(false);
        rainfallBarChart.setScaleYEnabled(false);
        rainfallBarChart.getAxisRight().setEnabled(false);
        rainfallBarChart.setMaxVisibleValueCount(1);
        rainfallBarChart.setDrawGridBackground(false);

        XAxis xAxis = rainfallBarChart.getXAxis();
        xAxis.setValueFormatter(new MonthNameValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12);

        RainfallValueFormatter valueFormat = new RainfallValueFormatter();
        YAxis yAxis = rainfallBarChart.getAxisLeft();
//        yAxis.setValueFormatter(valueFormat);
    }

    private void populateData() throws IOException {

        CSVFileReader temperatureFile;
        temperatureFile = new CSVFileReader(getContext(), location);

        double[] rainfallData = temperatureFile.getDataFromFile(year, "rain_total");

        /*ArrayList<BarEntry> eRainData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            eRainData.add(new BarEntry(i + 1, (float) rainfallData[i]));
        }

        BarDataSet rainSet = new BarDataSet(eRainData, "Total Rainfall (mm)");

        ArrayList<IBarDataSet> rainDataSet = new ArrayList<>();
        rainDataSet.add(rainSet);

        BarData rainData = new BarData(rainDataSet);
        rainData.setBarWidth(1f);
        rainfallBarChart.setData(rainData);*/

        ArrayList<BarEntry> eRainData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            eRainData.add(new BarEntry(i + 1, (float) rainfallData[i]));
        }

        BarDataSet dataSet = new BarDataSet(eRainData, "Total Rainfall (mm)");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        rainfallBarChart.setData(data);
    }

    public static Fragment newInstance(String location, String year) {
        RainfallGraphFragment fragment = new RainfallGraphFragment();
        Bundle b = new Bundle();
        b.putString("location", location);
        b.putString("year", year);
        fragment.setArguments(b);
        return fragment;
    }

    private class RainfallValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return value + " mm";
        }
    }
}
