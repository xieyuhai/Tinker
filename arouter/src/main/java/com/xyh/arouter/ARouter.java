package com.xyh.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class ARouter {

    private ARouter() {
        map = new HashMap<>();
    }

    private static ARouter sRouter = new ARouter();

    public static ARouter getInstance() {
        return sRouter;
    }

    private Context mContext;

    public void init(Context mContext) {
        this.mContext = mContext;
    }

    private Map<String, Class<? extends Activity>> map;

    /**
     * 存储activity
     *
     * @param key
     * @param clazz
     */
    public void addActivity(String key, Class<? extends Activity> clazz) {
        if (key != null && clazz != null) {
            map.put(key, clazz);
        }
    }

    public void navigation(String key, Bundle bundle) {
        Class<? extends Activity> aClass = map.get(key);

        if (aClass == null) {
            return;
        }
        Intent intent = new Intent(mContext, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
