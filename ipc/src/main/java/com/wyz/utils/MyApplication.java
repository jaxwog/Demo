package com.wyz.utils;

import android.app.Application;
import android.os.Process;
import android.util.Log;

/**
 * Created by wangyongzheng on 2017/4/12.
 */

public class MyApplication extends Application{
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = MyUtils.getProcessName(getApplicationContext(),
                Process.myPid());
        Log.d(TAG, "application start, process name:" + processName);

    }
}
