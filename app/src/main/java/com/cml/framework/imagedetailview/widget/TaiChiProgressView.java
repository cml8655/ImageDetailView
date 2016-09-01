package com.cml.framework.imagedetailview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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

    private int bgColor;
    private int leftTaichiColor;
    private int rightTaichiColor;

    /**
     * 太极最小半径
     */
    private float size = 20;
    private float rotateOffset;
    private boolean isExpand = true;
    private boolean expandAble = true;
    private float tmp;

    private Handler handler;

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas();
            if (null != canvas) {
//                canvas.drawCircle(getWidth() / 2, getHeight() / 2, maxOutRadius, outPaint);
                canvas.drawColor(getBgColor());
                drawTaichi(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            if (expandAble) {
                if (isExpand) {
                    spacing += 10;
                } else {
                    spacing -= 10;
                }

                if (spacing >= maxOutRadius - size) {
                    isExpand = false;
                    spacing = maxOutRadius - size;
                    expandAble = false;
                    tmp = 20;
                } else {
                    if (spacing <= 120) {
                        tmp = 20;
                        expandAble = false;
                        spacing = 120;
                        isExpand = true;
                    }
                }
            } else {
//                spacing = 0;
                if (tmp > 0) {
                    tmp--;
                } else {
                    expandAble = true;
                }
            }

            progress++;

            if (progress > maxProgress) {
                progress = 0;
            }
            rotateOffset += 10;
            handler.postDelayed(this, 10);
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
        outPaint.setTextSize(44);

        HandlerThread thread = new HandlerThread("tiachi");
        thread.start();
        handler = new Handler(thread.getLooper());
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        handler.post(drawRunnable);
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

        int bgLayer = canvas.saveLayer(new RectF(0, 0, getWidth(), getHeight()), outPaint, Canvas.ALL_SAVE_FLAG);

        float r = size / 2;

        float angle = (float) (-Math.PI / 180 * rotateOffset);
        float xd = (float) (r * Math.sin(angle));
        float yd = (float) (r * Math.cos(angle));
        //左下圆
        canvas.drawArc(new RectF(getWidth() / 2 - r + xd + sinSpacing, getHeight() / 2 - r + yd - cosSpacing, getWidth() / 2 + r + xd + sinSpacing, getHeight() / 2 + r + yd - cosSpacing), 89 + rotateOffset, 181, false, outPaint);

        outPaint.setColor(getBgColor());
        canvas.drawArc(new RectF(getWidth() / 2 - r - xd + sinSpacing, getHeight() / 2 - r - yd - cosSpacing, getWidth() / 2 + r - xd + sinSpacing, getHeight() / 2 + r - yd - cosSpacing), 269 + rotateOffset, 181, false, outPaint);
//
        outPaint.setColor(getLeftTaichiColor());
        //右上圆
        xd = -xd;
        yd = -yd;
        //不是90度开始是为了解决连接处的白线
        canvas.drawArc(new RectF(getWidth() / 2 - r + xd - sinSpacing, getHeight() / 2 - r + yd + cosSpacing, getWidth() / 2 + r + xd - sinSpacing, getHeight() / 2 + r + yd + cosSpacing), 269 + rotateOffset, 181, false, outPaint);

        outPaint.setColor(getBgColor());
        outPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawArc(new RectF(getWidth() / 2 - r - xd - sinSpacing, getHeight() / 2 - r - yd + cosSpacing, getWidth() / 2 + r - xd - sinSpacing, getHeight() / 2 + r - yd + cosSpacing), 90 + rotateOffset, 181, false, outPaint);
        outPaint.setXfermode(null);
        canvas.restoreToCount(bgLayer);

        outPaint.setColor(Color.WHITE);
        if (spacing >= 120) {
            String txt = spacing + "";
            Rect txtBounds = new Rect();
            outPaint.getTextBounds(txt, 0, txt.length(), txtBounds);
            canvas.drawText(spacing + "", getWidth() / 2 - txtBounds.width() / 2, getHeight() / 2 - txtBounds.height() / 2, outPaint);
//            canvas.drawCircle(getWidth() / 2, getHeight() / 2, 150, outPaint);
        }


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
