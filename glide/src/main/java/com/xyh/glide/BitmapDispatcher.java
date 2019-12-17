package com.xyh.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xyh.utils.CloseUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispatcher extends Thread {

    private LinkedBlockingQueue<BitmapRequest> requestQueue;


    public BitmapDispatcher(LinkedBlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void run() {
        super.run();

        while (!isInterrupted()) {
//   队列中获取请求
            BitmapRequest br;

            try {
                br = requestQueue.take();
                //先设置占位图片
                showLoadingImage(br);
                //    网络加载图片
                Bitmap bitmap = findBitmap(br);

//                把图片设置到imageView控件

                showImageView(br, bitmap);

                //设置是否缓存
                if (br.isCache()) {

                } else {

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageView(final BitmapRequest br, final Bitmap bitmap) {
        if (bitmap != null && br.getImageView() != null
                && br.getUrlMd5().equals(br.getImageView().getTag())
        ) {
            final ImageView iv = br.getImageView();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bitmap);

                    if (br.getRequestListener() != null) {
                        BitmapRequest.RequestListener requestListener = br.getRequestListener();
                        requestListener.onSuccessFul(iv, bitmap);
                    }
                }
            });
        }
    }

    private Bitmap findBitmap(BitmapRequest br) {
        Bitmap bitmap = downloadFromInternet(br.getUrl());
        return bitmap;
    }

    private Bitmap downloadFromInternet(String netUrl) {
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(netUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            is = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(is);
        }
        return null;
    }

    private void showLoadingImage(BitmapRequest br) {
        if (br.getResId() > 0 && br.getImageView() != null) {
            int resId = br.getResId();
            ImageView imageView = br.getImageView();
            imageView.setImageResource(resId);
        }
    }
}
