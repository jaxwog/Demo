package com.wyz.window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangyongzheng on 2017/8/14.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
     //onReceive不能做耗时操作，参考值10s以内
        String action = intent.getAction();
        Log.d(TAG, "onReceive action: ="+action);
        //do some works
    }
}
