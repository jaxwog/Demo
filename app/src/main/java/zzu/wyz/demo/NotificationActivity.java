package zzu.wyz.demo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * PendingIntent基本用法，发送短信
 */
public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //初步使用PendingIntent，需要注意SDK版本的变化
       // this.testNotification();
        this.sendSMSDemo();


    }

    private void sendSMSDemo(){
        String content = "剑圣，我的剑，就是你的剑 ；德邦，犯我德邦者，随远必诛 ；"
                + "邪恶小法师 ，如果我俩角色互换，我会让你看看什么叫残忍！"
                + "战争之王：能死在我的脚下是上天给予你的恩赐。好运姐：我有两把枪，一把叫射，另一把叫…啊。"
                + "狗头：生与死轮回不止，我们生他们死；很多人都喜欢打飞机，这对飞机来说很不公平";// 超过了70个字
        SmsManager smsManager = SmsManager.getDefault();//得到SmsManager的默认实例

        PendingIntent sentIntent = PendingIntent.getActivity(this, 0,
                super.getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

        if (content.length() > 70) { // 大于70个字，拆分
            List<String> msgs = smsManager.divideMessage(content); // 拆分,返回的是ArrayList<String>
            Iterator<String> iter = msgs.iterator();
            while (iter.hasNext()) {
                String msg = iter.next();
                //(String destinationAddress接收的地址, String scAddress短信中心号码，null表示采用系统默认,
                // String text短信文本,PendingIntent sentIntent不是空，成功或失败,
                // PendingIntent deliveryIntent如果不为空，则交给接收者处理)
                smsManager.sendTextMessage("15639707315", null, msg, sentIntent, null);
                Toast.makeText(this, "短信发送完成", Toast.LENGTH_SHORT).show();
            }
        } else {
            smsManager.sendTextMessage("15639707315", null, content, sentIntent, null);
            Toast.makeText(this, "短信发送完成", Toast.LENGTH_SHORT).show();
        }


}



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void testNotification(){
    // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
            new Intent(this, ActivityGroupActivity.class), 0);
    // 通过Notification.Builder来创建通知，注意API Level
    // API11之后才支持
    Notification notify2 = new Notification.Builder(this)
            .setSmallIcon(R.drawable.pic_m) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                    // icon)
            .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                    // bar上显示的提示文字
            .setContentTitle("Notification Title")// 设置在下拉status
                    // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
            .setContentText("This is the notification message")// TextView中显示的详细内容
            .setContentIntent(pendingIntent2) // 关联PendingIntent
            .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
            .build();//需要注意build()是在API level 16之后可以用
           // .getNotification(); // 已经废弃，但是通用
    // 16及之后增加的，在API11中可以使用getNotificatin()来代替
    notify2.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
           // FLAG_AUTO_CANCEL;
    manager.notify(0, notify2);

    // 清除id为NOTIFICATION_FLAG的通知
    // manager.cancel(NOTIFICATION_FLAG);
    // 清除所有的通知
    // manager.cancelAll();
}


    private void firstPendingIntent(){
         /*此方法在sdk11版本已经----------废弃
    初步使用PendingIntent表示的是将要发生的操作，所谓的将要发生的Intent
        指的是在当前的Activity不立即使用此Intent进行处理，
        而将此Intent封装后传递给其他的Activity程序，
        而其他的Activity程序在需要使用此Intent时才进行操作。
    */
        //notification类代表持续notification如何使用NotificationManager显示给用户。

        //NotificationManager类通知用户的事件发生。这是你如何告诉用户在后台发生了一些变化。

        //取得系统服务ACTIVITY_SERVICE
        NotificationManager notificationManager = (NotificationManager)super
                .getSystemService(ACTIVITY_SERVICE);

        //Unflatten the notification from a parcel.从一个包裹Unflatten通知
        //下面的构造方法已经过时，并且立即显示
        Notification notification = new Notification(R.drawable.pic_m,
                "来至赵阳光的消息",System.currentTimeMillis());

        //检索PendingIntent将开始一个新的活动,像调用Context.startActivity(intent)。
        //intent:Intent是启动Activity，flag，添加标识
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, super.getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

        //此方法已经过时，并且不能使用
        /*notification.setLatestEventInfo(this, "郑州大学",
                "郑州大学工学院（www.zzu.edu.cn）", contentIntent);*/
        notificationManager.notify("ZZU-WYZ", R.drawable.pic_m, notification);


    }

}
