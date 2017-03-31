package zzu.wyz.demo;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;


//SDK13版本此ActivityGroup已经过时；Use the new Fragment and FragmentManager
public class ActivityGroupActivity extends ActivityGroup {

    private GridView gridViewBar;
    private MenuImageAdapter menuImageAdapter;
    private LinearLayout grouplayout;
    //定义图片资源
    private int[]menu_img = new int[]{R.drawable.menu_main,R.drawable.menu_news,
            R.drawable.menu_sms, R.drawable.menu_more, R.drawable.menu_exit };
    //定义每个导航按钮的宽和高
    private int width = 0;
    private  int height = 0;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        this.init();
        this.gridBarToSet();

    }

    //定义注册组件
    private void init(){
        this.gridViewBar = (GridView)super.findViewById(R.id.gridViewBar);
        this.grouplayout = (LinearLayout)super.findViewById(R.id.grouplayout);

        this.gridViewBar.setOnItemClickListener(new OnItemCLickListenerImpl());

    }

    //设置gridViewBar的参数
    private void gridBarToSet(){
        this.gridViewBar.setNumColumns(this.menu_img.length);//设置导航钮的数量
        this.gridViewBar.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置透明的颜色
        this.gridViewBar.setGravity(Gravity.CENTER);
        this.gridViewBar.setVerticalSpacing(0);//设置间距,px为单位

        //根据屏幕的宽度和高度，动态计算出每个导航按钮的宽度和高度
        this.width = super.getWindowManager().getDefaultDisplay()
                .getWidth()/this.menu_img.length;

        this.height = super.getWindowManager().getDefaultDisplay()
                .getHeight()/8;

        this.menuImageAdapter = new MenuImageAdapter(this,
                menu_img,width, height, R.drawable.menu_selected);
        this.gridViewBar.setAdapter(this.menuImageAdapter);

        //第一次运行调用第一个页面，初始化界面
        this.switchActivity(0);




    }

    private class OnItemCLickListenerImpl implements AdapterView.OnItemClickListener{

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent   The AdapterView where the click happened.
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //根据position来选择需要跳转的Activity
            ActivityGroupActivity.this.switchActivity(position);
        }
    }

    private void switchActivity(int id){
        this.menuImageAdapter.setFocus(id); // 设置选中图片的背景
        this.grouplayout.removeAllViews();// 删除所有的内容
        switch (id) {
            case 0:
                this.intent = new Intent(this, SQLiteActivity.class);
                break;
            case 1:
                this.intent = new Intent(this, PopupActivity.class);
                break;
            case 2:
                this.intent = new Intent(this, MainActivity.class);
                break;
            case 3:
                this.intent = new Intent(this, IntentCaseActivity.class);
                break;
            case 4:
               this.exitDialog();
                return;
        }

        this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //设置并得到当前Activity的信息，id：要启动活动的惟一标识符
        //intent:要启动描述活动的意图
        Window subActivity = super.getLocalActivityManager().startActivity(
                "subActivity", this.intent);
//??????????????????????????????????????????????
        this.grouplayout.addView(subActivity.getDecorView(),
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }



    private void exitDialog() {
        Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.pic_m)
                .setTitle("程序退出？ ").setMessage("您确定要退出本程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityGroupActivity.this.finish() ;
                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityGroupActivity.this.switchActivity(0);
                    }
                }).create();

        dialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            this.exitDialog() ;
        }
        return false ;
    }

}
