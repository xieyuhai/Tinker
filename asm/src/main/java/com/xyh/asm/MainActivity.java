package com.xyh.asm;

import android.os.Trace;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

//    @Injecttime
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Trace.beginSection("savedInstanceState");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Trace.endSection();
    }
}
