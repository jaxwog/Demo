package zzu.wyz.demo.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by WYZ on 2016/1/21.
 * 在AndroidManifest中注册service
 */
public class MyserviceUtil extends Service {
/*

    //Binder()类实现了IBinder接口，创建IBinder
    private IBinder myBinder = new Binder() {
        @Override
        public String getInterfaceDescriptor() {
            //默认返回一个接口名称
            return "MyServiceUitl Class ..";
        }
    };
*/

    private IBinder myBinder = new BinderImpl();

    //实现了空接口，用来表示一种能力
    public class BinderImpl extends Binder implements IService{

        @Override
        public String getInterfaceDescriptor() {
            return "MyServiceUitl Class ..";
        }
    }

    /**
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("*** Service onBind()") ;
        return myBinder;
       // return null;//用来测试BroadcastReceiver启动Service，
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //解除绑定
        System.out.println("*** service onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println("*** service onRebind()");
       //一个新的客户端连接到服务器端，以前的连接都断掉
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
       System.out.println("*** service onDestroy()");
    }


    @Override
    public void onCreate() {
        System.out.println("*** service onCreate()");
    }

    /**
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("*** service onStartCommand()");
        //继续执行
        return Service.START_CONTINUATION_MASK;
    }
}
