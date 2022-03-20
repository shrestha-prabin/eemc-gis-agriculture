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

public class TemperatureGraphFragment extends Fragment {

    LineChart temperatureLineChart;
    String location;
    String year;

    public TemperatureGraphFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_temperature_graph, container, false);
        location = getArguments().getString("location").toLowerCase();
        year = getArguments().getString("year");

        temperatureLineChart = v.findViewById(R.id.graph_temperature_linechart);
        customizeChart();
        try {
            populateData();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Temperature data not found for " + location + "/" + year, Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void customizeChart() {
        temperatureLineChart.getDescription().setText("Monthly Temperature (°C)");
        temperatureLineChart.setScaleYEnabled(false);
        temperatureLineChart.setScaleXEnabled(false);
        temperatureLineChart.getAxisRight().setEnabled(false);
        temperatureLineChart.setMaxVisibleValueCount(1);

        XAxis xAxis = temperatureLineChart.getXAxis();
        xAxis.setValueFormatter(new MonthNameValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(temperatureLineChart); // For bounds control?
        temperatureLineChart.setMarker(mv);
    }


    private void populateData() throws IOException {

        ArrayList<Entry> eMinTemp = new ArrayList<>();
        ArrayList<Entry> eMaxTemp = new ArrayList<>();

        CSVFileReader temperatureFile;
        temperatureFile = new CSVFileReader(getContext(), location);

        double[] temperatureMinData = temperatureFile.getDataFromFile(year, "temp_min");
        double[] temperatureMaxData = temperatureFile.getDataFromFile(year, "temp_max");

        for (int i = 0; i < 12; i++) {
            eMinTemp.add(new Entry(i + 1, (float) temperatureMinData[i]));
            eMaxTemp.add(new Entry(i + 1, (float) temperatureMaxData[i]));
        }

        LineDataSet dsMinTemp = new LineDataSet(eMinTemp, "Min");
        dsMinTemp.setColors(getResources().getColor(R.color.graph_blue));
        dsMinTemp.setLineWidth(4f);
        dsMinTemp.setDrawCircleHole(false);
        dsMinTemp.setDrawCircles(false);

        LineDataSet dsMaxTemp = new LineDataSet(eMaxTemp, "Max");
        dsMaxTemp.setColors(getResources().getColor(R.color.graph_orange));
        dsMaxTemp.setLineWidth(4f);
        dsMaxTemp.setDrawCircleHole(false);
        dsMaxTemp.setDrawCircles(false);

        LineData data = new LineData(dsMinTemp, dsMaxTemp);
        temperatureLineChart.setData(data);
        temperatureLineChart.invalidate();
    }

    public static TemperatureGraphFragment newInstance(String location, String year) {
        TemperatureGraphFragment fragment = new TemperatureGraphFragment();
        Bundle b = new Bundle();
        b.putString("location", location);
        b.putString("year", year);
        fragment.setArguments(b);
        return fragment;
    }

}
