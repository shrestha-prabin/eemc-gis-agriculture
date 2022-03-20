package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.prabin.agriculturearcgis.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;

public class HumidityGraphFragment extends Fragment {

    LineChart humidityLineChart;
    String location;
    String year;

    public HumidityGraphFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_humidity_graph, container, false);
        humidityLineChart = v.findViewById(R.id.graph_humidity_linechart);

        location = getArguments().getString("location").toLowerCase();
        year = getArguments().getString("year");

        customizeChart();
        try {
            populateData();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Humidity data not found for " + location + "/" + year, Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void customizeChart() {
        humidityLineChart.setScaleYEnabled(false);
        humidityLineChart.setScaleXEnabled(false);
        humidityLineChart.getAxisRight().setEnabled(false);
        humidityLineChart.setMaxVisibleValueCount(1);

        XAxis xAxis = humidityLineChart.getXAxis();
        xAxis.setValueFormatter(new MonthNameValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(humidityLineChart); // For bounds control?
        humidityLineChart.setMarker(mv);
    }


    private void populateData() throws IOException {

        ArrayList<Entry> eMinHumidity = new ArrayList<>();
        ArrayList<Entry> eMaxHumidity = new ArrayList<>();
        ArrayList<Entry> eAvgHumidity = new ArrayList<>();

        CSVFileReader humidityFile;
        humidityFile = new CSVFileReader(getContext(), location);

        double[] humidityMinData = humidityFile.getDataFromFile(year, "humidity_min");
        double[] humidityMaxData = humidityFile.getDataFromFile(year, "humidity_max");
        double[] humidityAvgData = humidityFile.getDataFromFile(year, "humidity_avg");

        for (int i = 0; i < 12; i++) {
            eMinHumidity.add(new Entry(i + 1, (float) humidityMinData[i]));
            eMaxHumidity.add(new Entry(i + 1, (float) humidityMaxData[i]));
            eAvgHumidity.add(new Entry(i + 1, (float) humidityAvgData[i]));
        }

        LineDataSet dsMinHumidity = new LineDataSet(eMinHumidity, "Min");
        dsMinHumidity.setColors(getResources().getColor(R.color.graph_blue));
        dsMinHumidity.setLineWidth(4f);
        dsMinHumidity.setDrawCircleHole(false);
        dsMinHumidity.setDrawCircles(false);

        LineDataSet dsMaxHumidity = new LineDataSet(eMaxHumidity, "Max");
        dsMaxHumidity.setColors(getResources().getColor(R.color.graph_orange));
        dsMaxHumidity.setLineWidth(4f);
        dsMaxHumidity.setDrawCircleHole(false);
        dsMaxHumidity.setDrawCircles(false);

        LineDataSet dsAvgHumidity = new LineDataSet(eAvgHumidity, "Avg");
        dsAvgHumidity.setColors(getResources().getColor(R.color.graph_green));
        dsAvgHumidity.setLineWidth(4f);
        dsAvgHumidity.setDrawCircleHole(false);
        dsAvgHumidity.setDrawCircles(false);

        LineData data = new LineData(dsMinHumidity, dsMaxHumidity, dsAvgHumidity);
        humidityLineChart.setData(data);
        humidityLineChart.getDescription().setText("Monthly Average Humidity (%)");
        humidityLineChart.invalidate();
    }

    public static Fragment newInstance(String location, String year) {
        HumidityGraphFragment fragment = new HumidityGraphFragment();
        Bundle b = new Bundle();
        b.putString("location", location);
        b.putString("year", year);
        fragment.setArguments(b);
        return fragment;
    }
}
