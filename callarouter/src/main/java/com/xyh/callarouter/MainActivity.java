package com.xyh.callarouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xyh.annotation.BindPath;
import com.xyh.arouter.ARouter;


@BindPath("member")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void call(View view) {
        ARouter.getInstance().navigation("member", null);
    }
}
