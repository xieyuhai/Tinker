package com.xyh.regionview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private RegionView mRegionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegionView = findViewById(R.id.regionView);

        try {
            mRegionView.setImage(getResources().getAssets().open("big.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
