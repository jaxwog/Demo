package com.wyz.binderpool;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wangyongzheng on 2017/4/27.
 */

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_CONPUTE = 0;
    public static final int BINDER_PASSWORD = 1;

    private IBinderPool mBinderPool;
    private Context mContext;
    private static volatile BinderPool sInstance;
    private CountDownLatch mBinderPoolDounLatch;

    private BinderPool(Context context){
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if (sInstance == null){
            synchronized (BinderPool.class){
                if (sInstance == null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    @SuppressLint("WrongConstant")
    private synchronized void connectBinderPoolService() {
        mBinderPoolDounLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);

        try {
            mBinderPoolDounLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);

            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBinderPoolDounLatch.countDown();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };





private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
    @Override
    public void binderDied() {
        Log.d(TAG, "binderDied");
        mBinderPool.asBinder().unlinkToDeath(deathRecipient,0);
        mBinderPool = null;
        connectBinderPoolService();
    }
};



        public IBinder queryBinder(int binderCode){
            IBinder binder = null;
            try{
                if (mBinderPool!=null){
                    binder = mBinderPool.queryBinder(binderCode);
                }
            }catch (RemoteException e){
                e.printStackTrace();
            }
            return binder;
        }


    public static class  BinderPoolImpl extends IBinderPool.Stub{

        public BinderPoolImpl(){
            super();
        }

        @Override
        public IBinder queryBinder(int bindercode) throws RemoteException {
            IBinder binder = null;

            switch (bindercode){
                case BINDER_CONPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_PASSWORD:
                    binder = new PassWordImpl();
                    break;
                default:
                    break;
            }

            return binder;
        }
    }

}
