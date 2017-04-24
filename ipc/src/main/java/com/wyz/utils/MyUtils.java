package com.wyz.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Created by wangyongzheng on 2017/4/12.
 */

public class MyUtils {
    //根据进程id获取当前进程的名称
    public static String getProcessName(Context context,int pid){
        @SuppressLint("WrongConstant") ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list==null){
            return null;
        }

        for (int i=0;i<list.size();i++){
            if (list.get(i).pid==pid){
                return list.get(i).processName;
            }
        }
//        for (ActivityManager.RunningAppProcessInfo procInfo : list) {
//            if (procInfo.pid == pid) {
//                return procInfo.processName;
//            }
//        }


       return null;
    }

    public static void closeFile(Closeable closeable){
        try {
            if (closeable!=null){
                closeable.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
