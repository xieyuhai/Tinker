package com.xyh.callarouter;

import android.app.Application;

import com.xyh.arouter.ARouter;

public class ARoterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
