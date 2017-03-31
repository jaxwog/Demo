package zzu.wyz.demo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by WYZ on 2016/1/22.
 * 已经在AndroidManifest文件中注册了广播接收者，
 * <intent-filter>
 *     <action android:name="android.intent.action.EDIT"/>
 * </intent-filter>
 * 基类代码,将收到发送的sendBroadcast(intent)。
 * 你不能启动弹出对话框实现onReceive()。
 */

    public class BroadcastReceiverUtil extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

       /* //通过代码配置实现广播,判断指定的action
        if ("com.zzu.wyz.LOVE".equals(intent.getAction())){
            String msg = intent.getStringExtra("msg");//取得附加信息
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }*/

        context.startService(new Intent(context,MyserviceUtil.class));



    }


/*
    public BroadcastReceiverUtil(){
        System.out.println("** 每次广播都会实例化一个新的广播组件进行操作。");
    }

    *//**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.被接收到的Intent
     *//*
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"广播已经启动",Toast.LENGTH_SHORT).show();
    }
    */
}
