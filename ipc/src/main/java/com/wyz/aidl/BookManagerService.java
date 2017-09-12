package com.wyz.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wangyongzheng on 2017/4/25.
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    //ConcurrentHashMap
    //高效并发处理 “只初始化一次” 的功能要求
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean(false);

   // private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<IOnNewBookArrivedListener>();

     private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            //添加权限认证，此时运行在Binder线程池中
            int check = checkCallingOrSelfPermission("com.wyz.ipc.permission.ACCESS_BOOK_SERVICE");
            Log.d(TAG, "onTransact: check = "+check);
            if (check==PackageManager.PERMISSION_DENIED){
                return false;
            }

            String packageName = null;
            String [] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages!=null && packages.length>0){
                packageName = packages[0];
            }
            Log.d(TAG, "onTransact  packageName:"+packageName);
            //包名验证
            if (!packageName.startsWith("com.wyz")){
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
//            if (true){
//               // throw new RuntimeException("oh my god");
//                throw new NullPointerException("oh my god");
//            }
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);

        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)){
//                mListenerList.add(listener);
//            }else {
//                Log.d(TAG, "registerListener: 客户端已经注册监听");
//            }
//            Log.d(TAG, "registerListener: size = "+ mListenerList.size());
            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG, "registerListener: ,current size:"+N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)){
//                mListenerList.remove(listener);
//                Log.d(TAG, "unregisterListener:客户端注销监听成功");
//            }else{
//                Log.d(TAG, "unregisterListener: 该客户端没有注册，无法注销");
//            }
//            Log.d(TAG, "unregisterListener: size = "+mListenerList.size());
           boolean success = mListenerList.unregister(listener);
           if (success){
               Log.d(TAG, "unregisterListener: success");
           }else {
               Log.d(TAG, "unregisterListener: not found ,can't unregister");
           }
           final int N = mListenerList.beginBroadcast();
           mListenerList.finishBroadcast();
            Log.d(TAG, "unregisterListener:current size: "+N);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"IOS"));

        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.wyz.ipc.permission.ACCESS_BOOK_SERVICE");
        Log.d(TAG, "onBind: check = "+check);
        if (check == PackageManager.PERMISSION_DENIED){
            return null;
        }
        return mBinder;
    }


    private class ServiceWorker implements Runnable {

        @Override
        public void run() {

            while (!mAtomicBoolean.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "newBook##" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException{
        mBookList.add(book);

//        Log.d(TAG, "onNewBookArrived: listeners size = "+mListenerList.size());
//        for (int i = 0;i<mListenerList.size();i++){
//            IOnNewBookArrivedListener listener = mListenerList.get(i);
//            listener.onNewBookArrived(book);
//        }

        final int N = mListenerList.beginBroadcast();
        for (int i=0;i<N;i++){
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener!=null){
                try {
                    listener.onNewBookArrived(book);//回调客户端的方法
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        }

        mListenerList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mAtomicBoolean.set(true);
        super.onDestroy();
    }
}
