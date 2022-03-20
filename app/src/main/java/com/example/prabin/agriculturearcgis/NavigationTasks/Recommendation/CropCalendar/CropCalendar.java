package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.CropCalendar;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prabin.agriculturearcgis.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class CropCalendar extends AppCompatActivity {

    HorizontalBarChart cropCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_calendar);

        cropCalendar = findViewById(R.id.crop_calendar_bar);
        customizeChart();
        populateData();
    }

    private void customizeChart() {

        cropCalendar.getDescription().setEnabled(false);
        cropCalendar.setTouchEnabled(false);
        cropCalendar.getDescription().setTextSize(18);
        cropCalendar.setScaleEnabled(false);
        cropCalendar.getLegend().setEnabled(false);


        cropCalendar.getAxisLeft().setEnabled(false);
        YAxis yAxis = cropCalendar.getAxisRight();
        yAxis.setLabelCount(12);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                switch ((int) value) {
                    case 0:
                        return "Jan";
                    case 1:
                        return "Feb";
                    case 2:
                        return "Mar";
                    case 3:
                        return "Apr";
                    case 4:
                        return "May";
                    case 5:
                        return "Jun";
                    case 6:
                        return "Jul";
                    case 7:
                        return "Aug";
                    case 8:
                        return "Sept";
                    case 9:
                        return "Oct";
                    case 10:
                        return "Nov";
                    case 11:
                        return "Dec";
                }
                return "";
            }
        });

        XAxis xAxis = cropCalendar.getXAxis();
            xAxis.setGranularity(1);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] crops = getApplicationContext().getResources().getStringArray(R.array.crop_list);

                return crops[(int)value-1];
            }
        });
    }

    private void populateData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        yVals1.add(new BarEntry(1, new float[]{5, 5, 2, 0}));   //rice
        yVals1.add(new BarEntry(2, new float[]{0, 4, 6, 2}));  //wheat
        yVals1.add(new BarEntry(3, new float[]{1, 7, 4, 0}));      //maize
        yVals1.add(new BarEntry(4, new float[]{4, 5, 3, 0}));      //barley
        yVals1.add(new BarEntry(5, new float[]{6, 4, 2, 0}));      //millet

        BarDataSet set1;

        set1 = new BarDataSet(yVals1, "");
        set1.setDrawIcons(false);
        set1.setDrawValues(false);

        int green = Color.rgb(0, 200, 83);
        int none = Color.parseColor("#d4d4d4");
        set1.setColors(new int[]{none, green, none, green});
        set1.setStackLabels(new String[]{"", "Suitable Period", ""});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.4f);

        cropCalendar.setData(data);
    }
}