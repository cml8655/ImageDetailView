package com.cml.framework.imagedetailview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by cmlBeliever on 2016/8/24.
 * 实现图片查看以及关闭动画功能
 */
public class ImageDetailView extends RelativeLayout {

    //############################################  成员属性    ############################################
    private ViewAdapter viewAdapter;
    private View convertView;
    private ViewGroup decorView;

    //############################################  构造方法    ############################################

    public ImageDetailView(Context context) {
        super(context);
        this.init();
    }

    public ImageDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ImageDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    //############################################  初始化   ############################################

    private void init() {

    }


    public void attachToActivity(Activity activity) {
        decorView = (ViewGroup) activity.getWindow().getDecorView();
    }

    public void attachView(View view) {

    }

    public void show(Activity activity, View fromView) {
        assertHasViewAdapter();
        decorView = (ViewGroup) activity.getWindow().getDecorView();


    }

    private void assertHasViewAdapter() {
        if (null == viewAdapter) {
            throw new IllegalArgumentException("请先设置viewAdapter，详见方法setViewAdapter");
        }
    }

    //############################################  动画   ############################################


    //############################################  Adapter   ############################################

    public interface ViewAdapter {
        /**
         * 提供需要显示的view
         *
         * @param convertView
         * @return
         */
        View getView(View convertView);
    }

    //############################################  工具类  ############################################
    public static class ViewUtil {
        /**
         * 从view中获取当前view的截屏信息
         *
         * @param view
         * @return
         */
        public static Bitmap getViewBitmap(View view) {

            //ImageView 直接获取图片信息
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                if (null != imageView.getDrawable() && imageView.getDrawable() instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    return bitmapDrawable.getBitmap();
                }
            }

            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap cacheBitmap = view.getDrawingCache();
            Bitmap targetBitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, cacheBitmap.getWidth(), cacheBitmap.getHeight());
            view.destroyDrawingCache();

            return targetBitmap;
        }
    }


    //############################################  get/set   ############################################


    public ViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    public void setViewAdapter(ViewAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }
}
