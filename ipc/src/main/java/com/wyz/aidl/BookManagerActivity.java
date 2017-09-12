package com.wyz.aidl;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wyz.ipc.R;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    Button button;
    Button unButton;
    private IBookManager mRemoteBookManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "receive new book: "+msg.obj);
                    break;
                default:
                    super.handleMessage(msg);

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);


        button = (Button) super.findViewById(R.id.btn_aidl);
        button.setOnClickListener(this);

        unButton = (Button) super.findViewById(R.id.btn_unaidl);
        unButton.setOnClickListener(this);

    }

    private Binder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied: Thread Name:"+Thread.currentThread().getName());
            if (mRemoteBookManager==null){
                return;
            }
            mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
            mRemoteBookManager = null;
            //TODO：重新绑定远程Service
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: binder:"+service);

            try {
                mRemoteBookManager = iBookManager;
                mRemoteBookManager.asBinder().linkToDeath(mDeathRecipient,0);
                List<Book> list = iBookManager.getBookList();
                Log.d(TAG, "query book: "+list.toString());

                iBookManager.addBook(new Book(3,"Android开发艺术探索"));

                List<Book> newList = iBookManager.getBookList();

                Log.i(TAG, "query book Type : "+ newList.getClass().getCanonicalName());
                Log.d(TAG, "query book: "+newList.toString());
                iBookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.d(TAG, "onServiceDisconnected: Thread Name:"+Thread.currentThread().getName());

        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener =new  IOnNewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {

            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };

   @SuppressLint("WrongConstant")
   private void onBindService(){
       Intent intent = new Intent(this,BookManagerService.class);
       bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
   }


   private void onButtonCopy(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               if (mRemoteBookManager!=null){
                   try {
                       List<Book> newList = mRemoteBookManager.getBookList();
                   }catch (RemoteException e){
                       e.printStackTrace();
                   }
               }
           }
       }).start();
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_aidl:
            onBindService();
                break;
            case R.id.btn_unaidl:
                unbind();
                break;
            default:
                break;
        }
    }

    private void unbind(){

       if (mRemoteBookManager!=null&&mRemoteBookManager.asBinder().isBinderAlive()){
           Log.i(TAG, "unbind: unregister listener:"+mOnNewBookArrivedListener);
           try {
               mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
           } catch (RemoteException e) {
               e.printStackTrace();
           }
       }
        unbindService(mConnection);
    }


    @Override
    protected void onDestroy() {

        unbind();
        super.onDestroy();
    }



}
