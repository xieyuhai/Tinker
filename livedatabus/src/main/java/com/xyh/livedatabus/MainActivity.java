package com.xyh.livedatabus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 只有当activity 显示状态 能够收到
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        LiveDataBus.getInstance()
                .with("user", UserBean.class)
                .postValue(new UserBean("xyh", 16));

    }

    public void jumpActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
