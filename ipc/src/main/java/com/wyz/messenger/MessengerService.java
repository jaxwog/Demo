package com.wyz.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wyz.utils.MyConstants;

/**
 * Created by wangyongzheng on 2017/4/21.
 */

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";
private static class MessengerHandler extends Handler{
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MyConstants.MSG_FROM_CLIENT:
                Log.i(TAG, "handleMessage: msg from client ="+msg.getData().getString("msg"));
                Messenger client = msg.replyTo;
                Message relpyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                Bundle bundle = new Bundle();
                bundle.putString("reply", "嗯，你的消息我已经收到，稍后会回复你。");
                Log.d(TAG, "msg send to client = 嗯，你的消息我已经收到，稍后会回复你。 ");
                relpyMessage.setData(bundle);
                try {
                    client.send(relpyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

                default:
                    super.handleMessage(msg);
        }

    }
}
    private static Messenger mMessenger;

    static {
        mMessenger = new Messenger(new MessengerHandler());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
