package com.kirer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 屏幕显示相关的工具类
 */
public class DisplayUtils {

    /**
     * 屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int width(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 屏幕高度
     *
     * @param activity
     * @return
     */
    public static int height(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 获取状态栏高度
     * 注: 该方法在onCreate中获取值为0
     *
     * @param activity
     * @return
     */
    public static int statusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取状态栏高度
     * 注: 该方法在onCreate中获取值为0
     *
     * @param resources
     * @return
     */
    public static int statusBarHeight(Resources resources) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = resources.getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取标题栏高度
     *
     * @param activity
     * @return
     */
    public static int titleBarHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight(activity);
        return titleBarHeight;
    }

    /**
     * 是否为平板
     *
     * @param context
     * @return
     */
    public static boolean tablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int ScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static int ScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * ****************************
     * 各大设备密度判断
     * ****************************
     */
//    public static boolean isLdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_ldpi);
//    }
//    public static boolean isMdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_mdpi);
//    }
//    public static boolean isTVdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_tvdpi);
//    }
//    public static boolean isHdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_hdpi);
//    }
//    public static boolean isXHdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_xhdpi);
//    }
//    public static boolean isXXHdpi() {
//        return $.sAppContext.getResources().getBoolean(R.bool.less_xxhdpi);
//    }
}
