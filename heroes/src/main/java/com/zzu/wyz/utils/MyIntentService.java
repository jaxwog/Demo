package com.zzu.wyz.utils;


import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyIntentService extends IntentService{
    private static final String TAG = "MyIntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getStringExtra("wyz");
        Log.d(TAG, "onHandleIntent: recive="+action);
        SystemClock.sleep(5*3000);
        if ("com.zzu.wyz.action0".equals(action)){
            Log.d(TAG, "onHandleIntent:handler task ");
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "IntentService onDestroy");
        super.onDestroy();
    }
}
