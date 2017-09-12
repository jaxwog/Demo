package com.wyz.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.CPU_ABI;

/**
 * Created by wangyongzheng on 2017/8/23.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/CrashTest/wyz/";

    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static final boolean DEBUG = true;

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context context;

    private CrashHandler(){

    }

    public static CrashHandler getsInstance(){
        return sInstance;
    }

    public void init(Context context){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context.getApplicationContext();
    }


    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统会自动调用该方法
     * @param t 出现未捕获异常的线程
     * @param e 未捕获的异常，得到异常信息
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSDCard(e);//导出异常信息到sd卡中
            uploadExceptionToServer();//上传异常信息到服务器
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        e.printStackTrace();

        //如果系统提供了默认的异常处理则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler!= null){
            mDefaultCrashHandler.uncaughtException(t,e);
        }else {
            Process.killProcess(Process.myPid());
        }

    }


    private void dumpExceptionToSDCard(Throwable ex) throws PackageManager.NameNotFoundException {
        //判断sd卡是否存在
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
           if (DEBUG){
               Log.w(TAG, "sdcard not found,skip dumo exception");
               return;
           }
        }

        //判断文件是否存在，如果不存在则创建路径
        File dir = new File(PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        //获取当前时间并进行格式化操作
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        File file = new File(PATH+FILE_NAME+time+FILE_NAME_SUFFIX);//文件信息
        //定义文件输出流并进行打印
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
            printWriter.println(time);//打印时间
            dumpPhoneInfo(printWriter);
            printWriter.println();
            ex.printStackTrace(printWriter);//打印堆栈信息
            printWriter.close();

        } catch (FileNotFoundException e) {
            Log.e(TAG, "dumpExceptionToSDCard: crash info failed");
        }

    }

    private void dumpPhoneInfo(PrintWriter printWriter) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);

        //软件版本号及版本名字
        printWriter.print("APP Version:");
        printWriter.print(pi.versionName);
        printWriter.print("_");
        printWriter.println(pi.versionCode);

        //Android系统版本号及SDK号
        printWriter.print("OS Version:");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);

        //手机制造商
        printWriter.print("Vendor:");
        printWriter.println(Build.MANUFACTURER);

        //手机型号
        printWriter.print("Model:");
        printWriter.println(Build.MODEL);

        //CPU架构
        printWriter.print("CPU ABI:");
        printWriter.println(CPU_ABI);


    }
    private void uploadExceptionToServer(){
        //上传日志信息到服务器
    }

}
