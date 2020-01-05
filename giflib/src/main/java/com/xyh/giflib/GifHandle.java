package com.xyh.giflib;

import android.graphics.Bitmap;

public class GifHandle {


    static {
        System.loadLibrary("native-lib");
    }

    private volatile long gifInfo;

    public GifHandle(String path) {
        gifInfo = openFile(path);
    }

    public synchronized int getWidth() {
        return getNativeWidth(gifInfo);
    }

    public synchronized int getHeight() {
        return getNativeHeight(gifInfo);
    }

    public synchronized int getLength() {
        return getNativeLength(gifInfo);
    }


    public long renderFrame(Bitmap bitmap, int index) {
        return renderFrame(gifInfo, bitmap, index);
    }

    public void recycleGif() {
        recycleGif(gifInfo);
    }

    /**
     * 绘制图像帧
     *
     * @param gifInfo
     * @param bitmap
     * @param index
     * @return
     */
    private native long renderFrame(long gifInfo, Bitmap bitmap, int index);

    private native void recycleGif(long gifInfo);

    //获取图像数量
    private native int getNativeLength(long gifInfo);

    //获取图像宽
    private native int getNativeWidth(long gifInfo);

    //获取图像高
    private native int getNativeHeight(long gifInfo);

    private native long openFile(String path);
}
