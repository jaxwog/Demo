package com.wyz.utils;

import android.os.Environment;

/**
 * Created by wangyongzheng on 2017/4/13.
 */

public class MyConstants {
    public static final String IPC_PATH = Environment.getExternalStorageDirectory().getPath()+"/wyz/IPC/";
//    public static final String IPC_PATH = "/storage/sdcard0/wyz/IPC/";
    public static final String FILE_PATH = IPC_PATH+ "userCache";

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;

}
