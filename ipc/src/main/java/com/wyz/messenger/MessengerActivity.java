package com.wyz.messenger;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wyz.ipc.R;
import com.wyz.utils.MyConstants;

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MessengerActivity";
    Button mButton;
    TextView mTextView;
    private Messenger mService;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_SERVICE:
                    Log.i(TAG, "receive msg from Service=" + msg.getData().getString("reply"));

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        init();
    }

    private void init() {
        mButton = (Button) findViewById(R.id.btn_send);
        mTextView = (TextView) findViewById(R.id.tv_msg);

        mButton.setOnClickListener(this);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        mService = new Messenger(service);
            Log.d(TAG, "绑定服务");
            Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg","服务器你好，我是客户端");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
                Log.d(TAG, "消息已经发送");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                sendToService();
                break;
                default:

                    break;
        }
    }



    @SuppressLint("WrongConstant")
    void sendToService(){
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }


}
