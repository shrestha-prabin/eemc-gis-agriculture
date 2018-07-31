package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.BayesClassifier.Attributes;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.BayesClassifier.BayesClassifier;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.HumidityGraphFragment;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.RainfallGraphFragment;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.TemperatureGraphFragment;
import com.example.prabin.agriculturearcgis.R;

import java.io.IOException;

public class RecommendActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    String location = "Kathmandu";
    String year = "2017";

    ViewPager mViewPager;

    SeekBar seekTempMin, seekTempMax, seekHumidMin, seekHumidMax, seekHumidAvg, seekRainfall;
    TextView tvTempMin, tvTempMax, tvHumidMin, tvHumidMax, tvHumidAvg, tvRainfall;

    Button btnCheck;

    //values are 10 times the actual
    //to support the floating values
    double tempMin = 10, tempMax = 30, humidMin = 20, humidMax = 100, humidAvg = 70, rainfall = 200;

    TextView tvSuitableList, tvNotSuitableList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        mViewPager = findViewById(R.id.recommend_graphViewPager);
        mViewPager.setAdapter(new GraphViewPagerAdapter(getSupportFragmentManager(), location, year));

        showLocationSelectionDialog();
        showYearSelectionSpinner();

        seekTempMin = findViewById(R.id.r_seekMinTemp);
        seekTempMax = findViewById(R.id.r_seekMaxTemp);
        seekHumidMin = findViewById(R.id.r_seekMinHumid);
        seekHumidMax = findViewById(R.id.r_seekMaxHumid);
        seekHumidAvg = findViewById(R.id.r_seekAvgHumid);
        seekRainfall = findViewById(R.id.r_seekRainfall);

        tvTempMin = findViewById(R.id.r_tvMinTemp);
        tvTempMax = findViewById(R.id.r_tvMaxTemp);
        tvHumidMin = findViewById(R.id.r_tvMinHumid);
        tvHumidMax = findViewById(R.id.r_tvMaxHumid);
        tvHumidAvg = findViewById(R.id.r_tvAvgHumid);
        tvRainfall = findViewById(R.id.r_tvRainfall);
        updateTextViews();

        seekTempMin.setProgress((int) tempMin * 10);
        seekTempMax.setProgress((int) tempMax * 10);
        seekHumidMin.setProgress((int) humidMin * 10);
        seekHumidMax.setProgress((int) humidMax * 10);
        seekHumidAvg.setProgress((int) humidAvg * 10);
        seekRainfall.setProgress((int) rainfall * 10);

        seekTempMin.setOnSeekBarChangeListener(this);
        seekTempMax.setOnSeekBarChangeListener(this);
        seekHumidMin.setOnSeekBarChangeListener(this);
        seekHumidMax.setOnSeekBarChangeListener(this);
        seekHumidAvg.setOnSeekBarChangeListener(this);
        seekRainfall.setOnSeekBarChangeListener(this);

        tvSuitableList = findViewById(R.id.r_tvSuitableList);
        tvNotSuitableList = findViewById(R.id.r_tvNotSuitableList);

        progressBar = findViewById(R.id.r_progressbar);

        btnCheck = findViewById(R.id.r_btnCheck);
        btnCheck.setOnClickListener(this);
    }

    private void showLocationSelectionDialog() {

        Button btnLocationSelect = findViewById(R.id.recommend_location_select);
        btnLocationSelect.setText(location);

        btnLocationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] districts = getResources().getStringArray(R.array.province_3);

                AlertDialog.Builder builder = new AlertDialog.Builder(RecommendActivity.this);
                builder.setTitle("Province 3");
                builder.setItems(districts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location = districts[i];
                        btnLocationSelect.setText(location);
                        //mViewPager.getAdapter().notifyDataSetChanged();
                        mViewPager.setAdapter(new GraphViewPagerAdapter(getSupportFragmentManager(), location, year));

                    }
                });
                builder.show();


            }
        });
    }

    private void showYearSelectionSpinner() {

        String[] years = getResources().getStringArray(R.array.data_dates);

        Spinner spinYearSelect = findViewById(R.id.recommend_year_select);
        spinYearSelect.setSelection(4);
        spinYearSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = years[i];
                spinYearSelect.setSelection(i);
                mViewPager.setAdapter(new GraphViewPagerAdapter(getSupportFragmentManager(), location, year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = "2017";
                spinYearSelect.setSelection(4);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.r_seekMinTemp:
                tempMin = (double) i / 10;
                break;
            case R.id.r_seekMaxTemp:
                tempMax = (double) i / 10;
                break;

            case R.id.r_seekMinHumid:
                humidMin = (double) i / 10;
                break;
            case R.id.r_seekMaxHumid:
                humidMax = (double) i / 10;
                break;
            case R.id.r_seekAvgHumid:
                humidAvg = (double) i / 10;
                break;

            case R.id.r_seekRainfall:
                rainfall = (double) i / 10;
                break;
        }
        updateTextViews();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updateTextViews() {
        tvTempMin.setText("Min (" + tempMin + ")");
        tvTempMax.setText("Max (" + tempMax + ")");
        tvHumidMin.setText("Min (" + humidMin + ")");
        tvHumidMax.setText("Max (" + humidMax + ")");
        tvHumidAvg.setText("Avg (" + humidAvg + ")");
        tvRainfall.setText("Total (" + rainfall + ")");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.r_btnCheck) {
            progressBar.setVisibility(View.VISIBLE);
            String[] crops = getResources().getStringArray(R.array.crop_list);

            StringBuilder sbSuitableCrop = new StringBuilder();
            StringBuilder sbNotSuitableCrop = new StringBuilder();

            try {
                BayesClassifier classifier = new BayesClassifier(RecommendActivity.this);
                Attributes attr = new Attributes(tempMin, tempMax, humidMin, humidMax, humidAvg, rainfall);

                for (String crop : crops) {
                    boolean isSuitable = classifier.testForCrop(crop, attr);
                    if(isSuitable) {
                        sbSuitableCrop.append(crop + "\n");
                    } else {
                        sbNotSuitableCrop.append(crop + "\n");
                    }
                }
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            tvSuitableList.setText(sbSuitableCrop);
            tvNotSuitableList.setText(sbNotSuitableCrop);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class GraphViewPagerAdapter extends FragmentStatePagerAdapter {

        String location;
        String year;

        public GraphViewPagerAdapter(FragmentManager fm, String location, String year) {
            super(fm);
            this.location = location;
            this.year = year;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TemperatureGraphFragment.newInstance(location, year);
                case 1:
                    return HumidityGraphFragment.newInstance(location, year);
                case 2:
                    return RainfallGraphFragment.newInstance(location, year);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
