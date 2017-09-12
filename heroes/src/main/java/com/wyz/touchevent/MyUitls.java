package com.wyz.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by wangyongzheng on 2017/5/15.
 */

public class MyUitls {

    public static DisplayMetrics getScreenMetrics(Context context){
        @SuppressLint("WrongConstant")
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
