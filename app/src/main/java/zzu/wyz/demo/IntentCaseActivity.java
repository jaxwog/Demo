package zzu.wyz.demo;

import android.app.VoiceInteractor;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class IntentCaseActivity extends AppCompatActivity {

    private Button intentactionview,intentactiondial,intentactioncall,
            intentactionsendto,intentactionsend,intentactionsendemail,
            intentactionpick;
    private ImageButton intentactioncontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caseintent);
        this.init();

    }
    //定义声明view
    private void init(){
        this.intentactionview = (Button)super.findViewById(R.id.intentactionview);
        this.intentactiondial = (Button)super.findViewById(R.id.intentactiondial);
        this.intentactioncall = (Button)super.findViewById(R.id.intentactioncall);
        this.intentactionsendto = (Button)super.findViewById(R.id.intentactionsendto);
        this.intentactionsend = (Button)super.findViewById(R.id.intentactionsend);
        this.intentactionsendemail = (Button)super.findViewById(R.id.intentactionsendemail);
        this.intentactionpick = (Button)super.findViewById(R.id.intentactionpick);
        this.intentactioncontent= (ImageButton)super.findViewById(R.id.intentactioncontent);

        this.intentactionview.setOnClickListener(new OnclickParse());//打开网页
        this.intentactiondial.setOnClickListener(new OnclickParse());//拨打电话
        this.intentactioncall.setOnClickListener(new OnclickParse());//直接呼叫
        this.intentactionsendto.setOnClickListener(new OnclickParse());//发送短信
        this.intentactionsend.setOnClickListener(new OnclickParse());//发送彩信
        this.intentactionsendemail.setOnClickListener(new OnclickParse());//发送邮件
        this.intentactionpick.setOnClickListener(new OnclickParse());//通讯录
        this.intentactioncontent.setOnClickListener(new OnclickParse());//创建选择器
    }

    private class OnclickParse implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            System.out.println(R.id.intentactiondial
                    + "++++++++++++++++id=" + v.getId());

           switch (v.getId()){
               case R.id.intentactionview:
                   //打开网页
                   IntentCaseActivity.this.action_view();
                   break;
               case R.id.intentactiondial:
                   //拨打电话
                   IntentCaseActivity.this.action_dial();
                   break;
               case R.id.intentactioncall:
                   //直接呼叫
                   IntentCaseActivity.this.action_call();
                   break;
               case R.id.intentactionsendto:
                   //发送短信
                   IntentCaseActivity.this.action_sendto();
                   break;
               case R.id.intentactionsend:
                   //发送彩信
                   IntentCaseActivity.this.action_send();
                   break;
               case R.id.intentactionsendemail:
                   //发送邮件
                   IntentCaseActivity.this.action_sendemail();
                   break;
               case R.id.intentactionpick:
                   //发送邮件action_get_content
                   IntentCaseActivity.this.action_pick();
                   break;
               case R.id.intentactioncontent:
                   //选择器
                   IntentCaseActivity.this.action_get_content();
                   break;
               default:

                   break;


           }


        }
    }

    //打开网页
    private void action_view(){
        Uri uri = Uri.parse("http://www.baidu.com");// 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_VIEW);// 设置要操作的Action
        it.setData(uri);// 要设置的数据
        this.startActivity(it);
    }
    //拨打电话ACTION_CALL
    private void action_dial(){
        Uri uri = Uri.parse("tel:" + "15639707315");// 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_DIAL);// 设置要操作的Action
        it.setData(uri);// 要设置的数据
        this.startActivity(it);
    }
    //直接呼叫
    private void action_call(){
        Uri uri = Uri.parse("tel:" + "15639707315");// 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_CALL);// 设置要操作的Action
        it.setData(uri);// 要设置的数据
        this.startActivity(it);
    }

    //发送短信
    private void action_sendto(){
        Uri uri = Uri.parse("smsto:" + "15639707315");// 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_SENDTO);// 设置要操作的Action
        it.putExtra("sms_body","你反馈的问题我们已经帮你核实！谢谢参与") ;	// 设置短信内容
        it.setType("vnd.android-dir/mms-sms") ;	// 短信的MIME类型
        it.setData(uri);// 要设置的数据
        this.startActivity(it);
    }

    //发送彩信
    private void action_send(){
        Uri uri = Uri.parse("file:///sdcard/Download/disney.jpg");// 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_SEND);// 设置要操作的Action
        it.putExtra("address", "15639707315") ;	// 设置发送联系人
        it.putExtra("sms_body","郑州大学工学院") ;	// 设置彩信内容
        it.putExtra(Intent.EXTRA_STREAM, uri);
        it.setType("image/png");
        this.startActivity(it);
    }

    //发送邮件
    private void action_sendemail(){
        Intent it = new Intent(Intent.ACTION_SEND) ;
        it.setType("plain/text") ;	// 设置类型
        String address[] = new String[] {"zheng31326@126.com"} ;	// 邮件地址
        String subject = "郑州大学北校区（ZZU）" ;
        String content = "www.baidu.com" ;
        it.putExtra(Intent.EXTRA_EMAIL, address) ;
        it.putExtra(Intent.EXTRA_SUBJECT, subject) ;
        it.putExtra(Intent.EXTRA_TEXT, content) ;
        this.startActivity(it) ;	// 执行跳转
    }


    //通讯录
    private void action_pick(){

        Uri uri = Uri.parse("content://contacts/people") ;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        super.startActivityForResult(intent, 1) ;
    }

    //选择器
    private void action_get_content(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//选择类型，起到过滤的作用
      this.startActivity(Intent.createChooser(intent,
                "选择图片浏览工具"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                Uri ret = data.getData(); // 接收返回的数据
                String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        + "=?"; // 设置查询条件
                String[] phoneSelectionArgs = { String.valueOf(ContentUris
                        .parseId(ret)) }; // 返回的ID
                Cursor c = super.managedQuery(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        phoneSelection, phoneSelectionArgs, null);
                StringBuffer buf = new StringBuffer();
                buf.append("电话号码是：");
                for (c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()) {
                    buf.append(
                            c.getString(c
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                            .append("、");
                }
                Toast.makeText(this, buf, Toast.LENGTH_LONG).show() ;
        }
    }
}
