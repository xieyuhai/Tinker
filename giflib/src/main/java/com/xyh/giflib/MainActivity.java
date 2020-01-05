package com.xyh.giflib;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

//    static {
//        System.loadLibrary("native-lib");
//    }

    private ImageView gifImageView;

    private static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "gif.gif";

    private GifHandle gifHandle;
    private Bitmap bitmap;
    private int maxIndex;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gifImageView = findViewById(R.id.gifImageView);

//        Toast.makeText(this, "" + getString(), Toast.LENGTH_SHORT).show();


        gifHandle = new GifHandle(path);

        int width = gifHandle.getWidth();
        int height = gifHandle.getHeight();

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        maxIndex = gifHandle.getLength();

//        //调用绘制，并返回 延迟时间
//        long delay_time = gifHandle.renderFrame(bitmap, currentIndex);
//
//        gifImageView.setImageBitmap(bitmap);
//
//        if (handler != null) {
//            handler.sendEmptyMessageDelayed(1, delay_time);
//        }

//        load();

    }


    private void load() {
        //调用绘制，并返回 延迟时间
        long delay_time = gifHandle.renderFrame(bitmap, currentIndex);

        gifImageView.setImageBitmap(bitmap);

        if (handler != null) {
            handler.sendEmptyMessageDelayed(1, delay_time);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //调用绘制，并返回 延迟时间
            currentIndex++;

            if (currentIndex >= maxIndex) {
                currentIndex = 0;
            }

//            long delay_time = gifHandle.renderFrame(bitmap, currentIndex);
//            gifImageView.setImageBitmap(bitmap);
            load();
        }
    };

    public void loadGif(View view) {
        load();
    }

//    public native String getString();


    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (gifHandle != null) {
            gifHandle.recycleGif();
        }
        super.onDestroy();
    }
}
