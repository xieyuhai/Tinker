package com.xyh.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.xyh.utils.MD5;

import java.lang.ref.SoftReference;

public class BitmapRequest {
    private String url;
    private Context context;
    private RequestListener requestListener;
    private int resId;
    private SoftReference<ImageView> mViewReference;
    private String urlMd5;
    private boolean isCache;




    public boolean isCache() {
        return isCache;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public int getResId() {
        return resId;
    }

    public SoftReference<ImageView> getmViewReference() {
        return mViewReference;
    }

    public ImageView getImageView() {
        return mViewReference.get();
    }

    public String getUrl() {
        return url;
    }

    public BitmapRequest(Context context) {
        this.context = context;
    }

    public BitmapRequest load(String url) {
        this.url = url;
        this.urlMd5 = MD5.getStringMD5(url);
        return this;
    }

    public BitmapRequest loading(int resId) {
        this.resId = resId;
        return this;
    }

    public BitmapRequest listener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public void into(ImageView imageView) {
        this.mViewReference = new SoftReference<ImageView>(imageView);
        imageView.setTag(this.urlMd5);
        RequestManager.getInstance().addBitmapRequest(this);
    }

    interface RequestListener {
        boolean onSuccessFul(ImageView iv, Bitmap bitmap);

        boolean onFailFul(ImageView iv);
    }
}
