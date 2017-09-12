package com.zzu.wyz.activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zzu.wyz.R;
import com.zzu.wyz.utils.MyIntentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的使用
 */
public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RecyclerViewActivity";
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mButton = (Button) super.findViewById(R.id.btn_test);
        mButton.setOnClickListener(this);



    }

    private void initThreadPool() {

        Runnable command = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
            }
        };

      int CPU_COUNT = Runtime.getRuntime().availableProcessors();//获取CPU核心数信息
        Log.d(TAG, "initThreadPool: CPU_COUNT="+CPU_COUNT);
       int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));//AsyncTask计算核心线程数量
        Log.d(TAG, "initThreadPool: CORE_POOL_SIZE="+CORE_POOL_SIZE);
        //只拥有核心线程
//        ExecutorService fixedPoolThread = Executors.newFixedThreadPool( 4);
//        fixedPoolThread.execute(command);

//        //只有非核心线程
        ExecutorService cachedPoolThread = Executors.newCachedThreadPool();
        cachedPoolThread.execute(command);
//        //核心线程固定，非核心没有限制
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
//       //2000ms后执行该线程任务
//        scheduledThreadPool.schedule(command,2000, TimeUnit.MILLISECONDS);
//        //延迟10ms后每隔1000ms执行一次任务
//        scheduledThreadPool.scheduleAtFixedRate(command,10,1000,TimeUnit.MILLISECONDS);
//        //只有一个核心线程
//        ExecutorService singlePoolThread = Executors.newSingleThreadExecutor();
//        singlePoolThread.execute(command);
    }

    private void initService() {
        Intent service = new Intent(this,MyIntentService.class);
        service.putExtra("wyz","com.zzu.wyz.action0");
        startService(service);
        service.putExtra("wyz","com.zzu.wyz.action1");
        startService(service);
        service.putExtra("wyz","com.zzu.wyz.action2");
        startService(service);
    }


    private void initData() {

        //线程采用串行方式进行执行
//        new MyAsyncTask("Thread1").execute("");
//        new MyAsyncTask("Thread2").execute("");
//        new MyAsyncTask("Thread3").execute("");
//        new MyAsyncTask("Thread4").execute("");
//        new MyAsyncTask("Thread5").execute("");

          //线程采用并行方式进行执行
        new MyAsyncTask("Thread1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        new MyAsyncTask("Thread2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        new MyAsyncTask("Thread3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        new MyAsyncTask("Thread4").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        new MyAsyncTask("Thread5").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
//        IntentService intentService;
//        HandlerThread handlerThread;
//        ActivityManager activityManager;
        //ThreadPoolExecutor threadPoolExecutor;
        //ExecutorService fixed;
    }




//AsyncTask串行、并行测试
    private static class MyAsyncTask extends AsyncTask<String,Integer,String>{

        private String name = "AsyncTask";

        public MyAsyncTask(String name){
            super();
            this.name = name;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return name;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.e(TAG,s+" finish " +dateFormat.format(new Date()));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test:
//                initData();
//                initService();
                initThreadPool();
                break;
                default:
                    break;
        }
    }



}
