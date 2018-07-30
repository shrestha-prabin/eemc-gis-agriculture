package com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.HumidityGraphFragment;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.RainfallGraphFragment;
import com.example.prabin.agriculturearcgis.NavigationTasks.Recommendation.GraphView.TemperatureGraphFragment;
import com.example.prabin.agriculturearcgis.R;

public class RecommendActivity extends AppCompatActivity {

    String location = "Kathmandu";

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        mViewPager = findViewById(R.id.recommend_graphViewPager);
        mViewPager.setAdapter(new GraphViewPagerAdapter(getSupportFragmentManager(), location));

        showLocationSelectionDialog();
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
                       mViewPager.setAdapter(new GraphViewPagerAdapter(getSupportFragmentManager(), location));

                    }
                });
                builder.show();


            }
        });
    }

    private class GraphViewPagerAdapter extends FragmentStatePagerAdapter {

        String location;

        public GraphViewPagerAdapter(FragmentManager fm, String location) {
            super(fm);
            this.location = location;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return TemperatureGraphFragment.newInstance(location);
                case 1: return HumidityGraphFragment.newInstance(location);
                case 2: return RainfallGraphFragment.newInstance(location);
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
