package com.xyh.glide;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {


    private ImageView imageView;

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            if (permission.shouldShowRequestPermissionRationale) {
                                return;
                            }
                        }
                        Glide.get(MainActivity.this).
                                load("http://p0.meituan.net/wedding/b7d4d179cdd8cf03b72beeff53525811711103.jpg%40watermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20").
                                into(imageView);
                    }
                });
    }
}
