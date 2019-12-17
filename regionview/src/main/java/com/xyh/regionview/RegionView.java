package com.xyh.regionview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

/**
 * 长图加载
 */
public class RegionView extends View implements GestureDetector.OnGestureListener {
    private int mImageWidth;
    private int mImageHeight;

    public RegionView(Context context) {
        this(context, null);
    }

    public RegionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    private Rect mRect;
    private BitmapFactory.Options mOption;
    private GestureDetector mGestureDector;
    private Scroller mScoller;
    private BitmapRegionDecoder mDecoder;


    public RegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //
        mRect = new Rect();
        //
        mOption = new BitmapFactory.Options();
        //手势识别
        mGestureDector = new GestureDetector(context, this);

        //滚动类
        mScoller = new Scroller(context);

    }

    //
    public void setImage(String path) {

    }

    public void setImage(int resId) {

    }

    public void setImage(InputStream is) {
        //获取图片的宽和高
        mOption.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, mOption);

        mImageWidth = mOption.outWidth;
        mImageHeight = mOption.outHeight;

        //开启复用
        mOption.inMutable = true;
        mOption.inPreferredConfig = Bitmap.Config.RGB_565;

        //关闭
        mOption.inJustDecodeBounds = true;

        //创建区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    private int mViewWidth;
    private int mViewHeight;
    private float mScale;
    private Bitmap mBitmap;//复用bitmap

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        //确定图片的加载区域

        mRect.top = 0;
        mRect.left = 0;
        mRect.right = mImageWidth;
        mScale = mViewWidth * 1.0f / mImageWidth;

//       (mViewHeight / mScale)  为 view 的高度
        mRect.bottom = (int) (mViewHeight / mScale);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDecoder == null) {
            return;
        }

        mOption.inBitmap = mBitmap;
        //设置解码区域，和 bitmap数据
        mBitmap = mDecoder.decodeRegion(mRect, mOption);

        //
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, matrix, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //将事件交给手势类处理
        return mGestureDector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //判断当前页面是否正在滑动
        if (!mScoller.isFinished()) {
            //强制停止
            mScoller.forceFinished(true);
        }
        return true;
    }

//    motionEvent 开始事件，获取坐标信息
//    motionEvnet1 获取当前事件 坐标信息

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvnet1, float distanceX, float distanceY) {

        mRect.offset(0, (int) distanceY);

//     处理图片到达顶部以及底部的情况
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int) (mViewHeight / mScale);
        }

        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }

        invalidate();

        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


//        (mViewHeight / mScale) 为 view 的高度

        mScoller.fling(0, mRect.top,
                0, (int) -velocityY, 0,
                //mImageHeight - (int) (mViewHeight / mScale) 图片的高 减去 view的高
                0, 0, mImageHeight - (int) (mViewHeight / mScale));
        return false;
    }


    //计算惯性结果
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScoller.isFinished()) {
            return;
        }
        if (mScoller.computeScrollOffset()) {
            mRect.top = mScoller.getCurrY();
            mRect.bottom = mRect.top + (int) (mViewHeight / mScale);

            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }


}
