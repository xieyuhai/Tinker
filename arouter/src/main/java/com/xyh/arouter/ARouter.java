package com.xyh.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

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

        setup();
    }


    private void setup() {
        List<String> classList = getClassName(mContext.getPackageName());

        try {
            for (String s : classList) {
                Class<?> clazz = Class.forName(s);
                if (IRouter.class.isAssignableFrom(clazz)) {
                    IRouter iRouter = (IRouter) clazz.newInstance();
                    iRouter.putActivity();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
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

    /**
     * 通过包名获取所属包下所有类名
     *
     * @param packageName
     * @return
     */
    private List<String> getClassName(String packageName) {
        List<String> classList = new ArrayList<>();
        String path = null;

        try {
            //获取dex目录
            path = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), 0
            ).sourceDir;

            DexFile dexFile = new DexFile(path);

            //获得编译后的文件
            Enumeration enumeration = dexFile.entries();

            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();

                if (name.contains(packageName)) {
                    classList.add(name);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }
}
