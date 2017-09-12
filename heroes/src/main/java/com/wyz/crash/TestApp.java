package com.wyz.crash;

import android.app.Application;
import android.util.Log;

/**
 * Created by wangyongzheng on 2017/8/23.
 */

public class TestApp extends Application {
    private static final String TAG = "TestApp";

    private static TestApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //为应用设置处理异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);

        Log.i(TAG, "onCreate: 异常监听方法调用成功");
    }

    public static TestApp getInstance(){
        return sInstance;
    }
}
