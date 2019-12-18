package com.xyh.callarouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xyh.annotation.BindPath;

@BindPath("call/jump")
public class JumpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
    }
}
