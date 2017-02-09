
package com.example.xiongfeng.wave.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.xiongfeng.wave.R;
import com.example.xiongfeng.wave.utils.UiUtils;


/**
 * @author xiongfeng
 * @created 2015/9/30
 * 遮罩式水波纹移动效果，实质是两张图片实现，平行移动后面图层的图 让人感觉是水波纹在动 此种方法CPU计算量小 方便 但一定要注意bitmap的处理 否则内存容易溢出
 */
public class PorterDuffXfermodeView extends View {

    private static final int WAVE_TRANS_SPEED = 4;

    private Paint mBitmapPaint, mPicPaint;
    private int mTotalWidth, mTotalHeight;
    private int mCenterX, mCenterY;
    private int mSpeed;

    private Bitmap mSrcBitmap;
    private Rect mSrcRect, mDestRect;

    private PorterDuffXfermode mPorterDuffXfermode;
    private Bitmap mMaskBitmap;
    private Rect mMaskSrcRect, mMaskDestRect;
    private PaintFlagsDrawFilter mDrawFilter;

    private int mCurrentPosition;

    public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initBitmap();
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mSpeed = UiUtils.dipToPx(getContext(), WAVE_TRANS_SPEED);
        mDrawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.DITHER_FLAG);
        new Thread() {
            public void run() {
                while (true) {
                    //水波纹移动的核心代码
                    // 改变绘制的波浪的位置
                    mCurrentPosition += mSpeed;
                    if (mCurrentPosition >= mSrcBitmap.getWidth()) {
                        mCurrentPosition = 0;
                    }
                    try {
                        // 30毫秒重绘一次，可以控制这个值改变波纹速度，还可以将cpu空出来，供其他部分使用
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }

                    postInvalidate();
                }

            };
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setDrawFilter(mDrawFilter);
        canvas.drawColor(Color.TRANSPARENT);

        // 设定要绘制水波纹的区域
        mSrcRect.set(mCurrentPosition, 0, mCurrentPosition + mCenterX, mTotalHeight-50);
        // 绘制波纹一小块，即波纹图片的一部分
        canvas.drawBitmap(mSrcBitmap, mSrcRect, mDestRect, mBitmapPaint);

        // 设置上面的遮挡图  让人只能看到后面波纹的一部分，产生水波纹效果
        canvas.drawBitmap(mMaskBitmap, mMaskSrcRect, mMaskDestRect,
                mBitmapPaint);
        mBitmapPaint.setXfermode(null);
//        canvas.restoreToCount(sc);
    }

    // 初始化bitmap
    @SuppressWarnings("deprecation")
	private void initBitmap() {
        mSrcBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.loading_1))
                .getBitmap();
        mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.loding_1))
                .getBitmap();
    }

    // 初始化画笔
    private void initPaint() {

        mBitmapPaint = new Paint();
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);

        mPicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPicPaint.setDither(true);
        mPicPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mCenterX = mTotalWidth / 2;
        mCenterY = mTotalHeight / 2;

        mSrcRect = new Rect();
//        mDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);

        int maskWidth = mMaskBitmap.getWidth();
        int maskHeight = mMaskBitmap.getHeight();
        mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);
//        mMaskDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
        mMaskDestRect = new Rect(mCenterX-maskWidth/2, mCenterY-maskHeight/2, mCenterX+maskWidth/2, mCenterY+maskHeight/2);
        
        mDestRect = new Rect(mCenterX-maskWidth/2, mCenterY-maskHeight/2, mCenterX+maskWidth/2, mCenterY+maskHeight/2);
    }

}
