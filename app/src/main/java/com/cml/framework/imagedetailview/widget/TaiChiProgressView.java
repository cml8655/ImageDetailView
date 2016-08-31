package com.cml.framework.imagedetailview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
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

    /**
     * 太极最小半径
     */
    private float size = 20;
    private float rotateOffset;

    private Handler handler;

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            if (null != canvas) {
                canvas.drawColor(Color.YELLOW);
                drawTaichi(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
            spacing += 10;
            progress++;
            if (spacing >= maxOutRadius - size) {
                spacing = 0;
            }
            if (progress > maxProgress) {
                progress = 0;
            }
            rotateOffset++;
            handler.postDelayed(this, 50);
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
        outPaint.setColor(Color.BLUE);
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

        //顺时针转动
//        canvas.rotate(-getProgressPercent() * 360, getWidth() / 2, getHeight() / 2);

        outPaint.setColor(Color.BLUE);
        //右侧半圆
        canvas.drawArc(new RectF(getWidth() / 2 - size, getHeight() / 2 - size, getWidth() / 2 + size, getHeight() / 2 + size), 270 + rotateOffset, 180, false, outPaint);
        //左边部分
        outPaint.setColor(Color.GREEN);
        //左侧半圆
        canvas.drawArc(new RectF(getWidth() / 2 - size, getHeight() / 2 - size, getWidth() / 2 + size, getHeight() / 2 + size), 90 + rotateOffset, 180, false, outPaint);

//        //左下圆
//        canvas.drawArc(new RectF(getWidth() / 2 - size / 2, getHeight() / 2, getWidth() / 2 + size / 2, getHeight() / 2 + size), 0, 360, false, outPaint);
//
        outPaint.setColor(Color.BLUE);

        canvas.save();
        outPaint.setColor(Color.RED);
//        canvas.drawCircle((float) (getWidth() / 2 + (size / 2) * Math.sin(rotateOffset / Math.PI)), (float) (getHeight() / 2 - (size / 2) * Math.cos(rotateOffset / Math.PI)), size / 2, outPaint);
//        //右上圆
        float r = size / 2;
        float angel = (float) ((90 - rotateOffset) / Math.PI);
        float xd = (float) (r * Math.sin(angel));
        float yd = (float) (r * Math.cos(angel));

        canvas.drawArc(new RectF(getWidth() / 2 - r + xd, getHeight() / 2 - r + yd, getWidth() / 2 + r + xd, getHeight() / 2 + r + yd), 90 + rotateOffset, 180, false, outPaint);
        canvas.restore();
    }
    //------------------------
//    canvas.drawArc(new RectF(getWidth() / 2 - size + xRotateOffset, getHeight() / 2 - size + yRotateOffset, getWidth() / 2 + size + xRotateOffset, getHeight() / 2 + size + yRotateOffset), 270 + rotateOffset, 180 + rotateOffset, false, outPaint);
//    //左边部分
//    outPaint.setColor(Color.GREEN);
//    //左侧半圆
//    canvas.drawArc(new RectF(getWidth() / 2 - size, getHeight() / 2 - size, getWidth() / 2 + size, getHeight() / 2 + size), 90, 180, false, outPaint);
//        //左下圆
//        canvas.drawArc(new RectF(getWidth() / 2 - size / 2, getHeight() / 2, getWidth() / 2 + size / 2, getHeight() / 2 + size), 0, 360, false, outPaint);
//
//        outPaint.setColor(Color.BLUE);
//        //右上圆
//        canvas.drawArc(new RectF(getWidth() / 2 - size / 2, getHeight() / 2 - size, getWidth() / 2 + size / 2, getHeight() / 2), 90, 180, false, outPaint);
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
