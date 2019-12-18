package com.xyh.livedatabus;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        LiveDataBus.getInstance()
                .with("user", UserBean.class)
                .observe(this, new Observer<UserBean>() {
                    @Override
                    public void onChanged(@Nullable UserBean userBean) {
                        Toast.makeText(SecondActivity.this, userBean.getName() + "=" + userBean.getAge(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
