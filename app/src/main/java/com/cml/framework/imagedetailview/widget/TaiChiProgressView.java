package com.cml.framework.imagedetailview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by cmlBeliever on 2016/8/31.
 */
public class TaiChiProgressView extends SurfaceView implements SurfaceHolder.Callback {

    private float maxOutRadius;
    private Paint outPaint;
    private float maxInnerRadius;
    private float spacing;
    private float progress;
    private float maxProgress;

    private int bgColor;
    private int leftTaichiColor;
    private int rightTaichiColor;

    /**
     * 太极最小半径
     */
    private float size = 20;
    private float rotateOffset;
    private boolean isExpand = true;

    private Handler handler;

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            if (null != canvas) {
                canvas.drawColor(getBgColor());
                drawTaichi(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
            if (isExpand) {
                spacing += 1;
            } else {
                spacing -= 1;
            }

            progress++;
            spacing =10;
//            if (spacing >= maxOutRadius - size) {
//                isExpand = false;
//                spacing = maxOutRadius - size;
//            } else {
//                if (spacing <= 0) {
//                    spacing = 0;
//                    isExpand = true;
//                }
//            }
            if (progress > maxProgress) {
                progress = 0;
            }
            rotateOffset++;
            handler.postDelayed(this, 500);
        }
    };

    public TaiChiProgressView(Context context) {
        super(context);
        this.init();
    }

    public TaiChiProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TaiChiProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private SurfaceHolder holder;

    private void init() {
        holder = getHolder();
        holder.addCallback(this);

        outPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outPaint.setStyle(Paint.Style.FILL);
        outPaint.setStrokeWidth(1);

        HandlerThread thread = new HandlerThread("tiachi");
        thread.start();
        handler = new Handler(thread.getLooper());
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        handler.post(drawRunnable);
    }


    private void drawOutCircle(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, size, outPaint);
    }

    private void drawTaichi(Canvas canvas) {
        outPaint.setColor(getRightTaichiColor());
        float sinSpacing = (float) (spacing * Math.sin(Math.PI / 180 * (90 + rotateOffset)));
        float cosSpacing = (float) (spacing * Math.cos(Math.PI / 180 * (90 + rotateOffset)));


        //右侧半圆
        canvas.drawArc(new RectF(getWidth() / 2 - size + sinSpacing, getHeight() / 2 - size - cosSpacing, getWidth() / 2 + size + sinSpacing, getHeight() / 2 + size - cosSpacing), 270 + rotateOffset, 180, false, outPaint);
        //左边部分
        outPaint.setColor(getLeftTaichiColor());
        //左侧半圆
        canvas.drawArc(new RectF(getWidth() / 2 - size - sinSpacing, getHeight() / 2 - size + cosSpacing, getWidth() / 2 + size - sinSpacing, getHeight() / 2 + size + cosSpacing), 90 + rotateOffset, 180, false, outPaint);

        outPaint.setColor(getRightTaichiColor());

        float r = size / 2;

        float angle = (float) (-Math.PI / 180 * rotateOffset);
        float xd = (float) (r * Math.sin(angle));
        float yd = (float) (r * Math.cos(angle));
        //左下圆
        canvas.drawArc(new RectF(getWidth() / 2 - r + xd + sinSpacing, getHeight() / 2 - r + yd - cosSpacing, getWidth() / 2 + r + xd + sinSpacing, getHeight() / 2 + r + yd - cosSpacing), 89 + rotateOffset, 182, false, outPaint);

        outPaint.setColor(getBgColor());
        canvas.drawArc(new RectF(getWidth() / 2 - r - xd + sinSpacing, getHeight() / 2 - r - yd - cosSpacing, getWidth() / 2 + r - xd + sinSpacing, getHeight() / 2 + r - yd - cosSpacing), 269 + rotateOffset, 182, false, outPaint);
//
        outPaint.setColor(getLeftTaichiColor());
        //右上圆
        xd = -xd;
        yd = -yd;
        //不是90度开始是为了解决连接处的白线
        canvas.drawArc(new RectF(getWidth() / 2 - r + xd - sinSpacing, getHeight() / 2 - r + yd + cosSpacing, getWidth() / 2 + r + xd - sinSpacing, getHeight() / 2 + r + yd + cosSpacing), 269 + rotateOffset, 181, false, outPaint);
        outPaint.setColor(getBgColor());
        if (spacing >= xd / 2) {
            canvas.drawArc(new RectF(getWidth() / 2 - r - xd - sinSpacing, getHeight() / 2 - r - yd + cosSpacing, getWidth() / 2 + r - xd - sinSpacing, getHeight() / 2 + r - yd + cosSpacing), 90 + rotateOffset, 181, false, outPaint);
        }

        Log.i("test", "angel:" + rotateOffset + ",space:" + spacing);

    }
    //--------------------可自由旋转----
    //顺时针转动
//        canvas.rotate(-getProgressPercent() * 360, getWidth() / 2, getHeight() / 2);

//    outPaint.setColor(Color.BLUE);
//    //右侧半圆
//    canvas.drawArc(new RectF(getWidth() / 2 - size, getHeight() / 2 - size, getWidth() / 2 + size, getHeight() / 2 + size), 270 + rotateOffset, 180, false, outPaint);
//    //左边部分
//    outPaint.setColor(Color.GREEN);
//    //左侧半圆
//    canvas.drawArc(new RectF(getWidth() / 2 - size, getHeight() / 2 - size, getWidth() / 2 + size, getHeight() / 2 + size), 90 + rotateOffset, 180, false, outPaint);
//
////        //左下圆
////        canvas.drawArc(new RectF(getWidth() / 2 - size / 2, getHeight() / 2, getWidth() / 2 + size / 2, getHeight() / 2 + size), 0, 360, false, outPaint);
////
//    outPaint.setColor(Color.BLUE);
//
//    float r = size / 2;
////        canvas.drawCircle((float) (getWidth() / 2 + r * Math.sin(Math.PI / 180 * rotateOffset)), (float) (getHeight() / 2 - r * Math.cos(Math.PI / 180 * rotateOffset)), r, outPaint);
//
//    float angel = (float) (-Math.PI / 180 * rotateOffset);
//    float xd = (float) (r * Math.sin(angel));
//    float yd = (float) (r * Math.cos(angel));
//    //左下圆
//    canvas.drawArc(new RectF(getWidth() / 2 - r + xd, getHeight() / 2 - r + yd, getWidth() / 2 + r + xd, getHeight() / 2 + r + yd), 89 + rotateOffset, 182, false, outPaint);
//
//    outPaint.setColor(Color.GREEN);
//    //右上圆
//    xd = -xd;
//    yd = -yd;
//    //不是90度开始是为了解决连接处的白线
//    canvas.drawArc(new RectF(getWidth() / 2 - r + xd, getHeight() / 2 - r + yd, getWidth() / 2 + r + xd, getHeight() / 2 + r + yd), 269 + rotateOffset, 181, false, outPaint);

//--------------------

    public float getProgressPercent() {
        return progress / maxProgress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }


    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public int getLeftTaichiColor() {
        return leftTaichiColor;
    }

    public void setLeftTaichiColor(int leftTaichiColor) {
        this.leftTaichiColor = leftTaichiColor;
    }

    public int getRightTaichiColor() {
        return rightTaichiColor;
    }

    public void setRightTaichiColor(int rightTaichiColor) {
        this.rightTaichiColor = rightTaichiColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        handler.getLooper().quit();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        maxOutRadius = size / 2;
        maxInnerRadius = maxOutRadius / 2;
        if (this.size > maxOutRadius) {
            this.size = maxOutRadius;
        }
    }
}
