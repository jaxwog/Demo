package zzu.wyz.demo;

import android.app.ActionBar;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import java.util.List;
import java.util.Map;

import zzu.wyz.demo.util.MySQLCursor;
import zzu.wyz.demo.util.MySQLOperate;
import zzu.wyz.demo.util.MySQLiteOpenHelper;

public class SQLiteActivity extends AppCompatActivity {
    private Button sqlInsertBut,sqlUpdateBut,sqlDeleteBut,sqlFindBut;
    private SQLiteOpenHelper helper;
    private MySQLOperate database;
    private MySQLCursor dbCursor;
    private ListView listView;
    private LinearLayout mylayout;
    private static int count = 1;

    //分页显示声明对象
    private LinearLayout loadLayout;//读取的脚标布局
    private TextView loadView;//脚标的信息提示
    //脚标布局的布局参数
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> allList;
    private int currentPage = 1;//当前显示的页数
    private int lineSize = 15;//每页显示的数据量
    //得到数据库查询的数据量
    private int allRecorders=0;
    //默认只是一页，根据allRecorders与lineSize计算得出
    private int pageSize = 1;
    //保存数据显示的最后一项，用来判断当前页面是否到底
    private int lastItem = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        //this.buildNewSQLite();
        this.init();

    }

    //注册按钮，监听按钮事件
    private void init(){
        this.sqlInsertBut = (Button)super.findViewById(R.id.sqlInsertBut);
        this.sqlUpdateBut = (Button)super.findViewById(R.id.sqlUpdateBut);
        this.sqlDeleteBut = (Button)super.findViewById(R.id.sqlDeleteBut);
        this.sqlFindBut = (Button)super.findViewById(R.id.sqlFindBut);
        this.mylayout = (LinearLayout)super.findViewById(R.id.mylayout);

        this.sqlInsertBut.setOnClickListener(new OnClickListenerInsert());
        this.sqlUpdateBut.setOnClickListener(new OnClickListenerUpdate());
        this.sqlDeleteBut.setOnClickListener(new OnClickListenerDelete());
        this.sqlFindBut.setOnClickListener(new OnClickListenerQuery());
    }
    //定义脚标的显示风格，及显示内容
    private void initLoad(){
        this.loadLayout = new LinearLayout(this);
        this.loadView = new TextView(this);
        this.loadView.setText("数据加载中ing...");
        this.loadView.setGravity(Gravity.CENTER);
        this.loadView.setTextSize(30.0f);
        this.loadLayout.addView(this.loadView, this.layoutParams);
        this.loadLayout.setGravity(Gravity.CENTER);

    }


    //增、删、改时打开数据库
    private void openSQLite(){
        //取得数据库对象，如果数据库不存在则创建数据库
        this.helper = new MySQLiteOpenHelper(this);
        //打开数据库，SQLiteDatabase操作数据库
        this.database = new MySQLOperate(helper.getWritableDatabase());
    }

    //查询时候，打开数据库
    private void openCursorSQLite(){
        this.helper = new MySQLiteOpenHelper(this);
        this.dbCursor = new MySQLCursor(helper.getReadableDatabase());
    }



    //监听查询按钮事件
    private class OnClickListenerQuery implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SQLiteActivity.this.openCursorSQLite();

            //查询操作ArrayAdapter显示
           // SQLiteActivity.this.showCursorList();

            //定义显示脚标，需要在查询数据前加载
            SQLiteActivity.this.initLoad();


            //查询操作，SimpleAdapter显示分页
            SQLiteActivity.this.showCursorPageList();

            SQLiteActivity.this.getPageSize();

        }
    }

    //得到分页显示总页数
    private void getPageSize(){
        this.pageSize = (this.allRecorders+this.lineSize-1)/this.lineSize;
        System.out.println("pageSize = " + this.pageSize) ;
        System.out.println("allRecorders = " + this.allRecorders);

    }

    //List<Map<String,Object>>列表simpleAdapter转化查询到的数据并进行展示
    private void showCursorPageList(){

        //检索出数据总量，根据数据总量进行自动分页
        this.allRecorders = this.dbCursor.getCount();
        listView = new ListView(this) ;
        this.allList = this.dbCursor.currentFind(currentPage,lineSize);
        this.listView.addFooterView(loadLayout);//添加脚标视图到列表展示
        //实例化SimpleAdapter
        this.simpleAdapter = new SimpleAdapter(this,allList,R.layout.sqlite_table,
                new String[]{"queryId","queryName","queryBirthday"},
                new int[]{R.id.queryId,R.id.queryName,R.id.queryBirthday});
        this.listView.setAdapter(simpleAdapter);
        //监听ListView视图的滚动
        this.listView.setOnScrollListener(new OnScrollListenerImpl());
        this.mylayout.addView(listView);


    }

    private class OnScrollListenerImpl implements AbsListView.OnScrollListener{


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /**
             * 1.判断数据的最后一项是否与SimpleAdapter的count相等（到底）
             *2.当前页是否小于总页数，判断数据是否读取完成
             *3.当前页面不能再滑动，已经到了加载footView的时候
             */
            if (SQLiteActivity.this.lastItem==SQLiteActivity.this.simpleAdapter.getCount()
                    && SQLiteActivity.this.currentPage < SQLiteActivity.this.pageSize
                    && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){

                SQLiteActivity.this.currentPage++;//当前要显示的页数自动增加
                //设置更新后的数据显示在lastItem下面
                SQLiteActivity.this.listView.setSelection(SQLiteActivity.this.lastItem);

                SQLiteActivity.this.appendListDate();

            }


        }

        /**
         *
         * @param view       The view whose scroll state is being reported
         * @param firstVisibleItem  第一个可见项的Item标号
         * @param visibleItemCount   ListView现在在屏幕上一共显示多少列表项
         * @param totalItemCount    the number of items in the list adaptor可以显示的最后一项的id
         *    totalItemCount =< firstVisibleItem+ visibleItemCount   此时已经到列表项的最后一条
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            System.out.println("firstVisibleItem = " + firstVisibleItem) ;
            System.out.println("visibleItemCount = " + visibleItemCount) ;
            System.out.println("totalItemCount = " + totalItemCount) ;

            //用lastItem判断当前页面的数据是否到底
            SQLiteActivity.this.lastItem = firstVisibleItem + visibleItemCount - 1;

            System.out.println("***************************************************") ;

        }
    }

    //list集合中增加下一页的数据
    private void appendListDate(){
        this.openCursorSQLite();
      //定义新的List数据，此时currentPage已经更新，相当于查询下一页的数据加载到了newData中
        List<Map<String, Object>> newData = this.dbCursor.currentFind(currentPage,lineSize);

        this.allList.addAll(newData);//新数据全部复制到allList集合里面
        //通知内容改变
        this.simpleAdapter.notifyDataSetChanged();


    }


    //List<String>列表展示查询到的数据
    private void showCursorList(){

         listView = new ListView(this) ;
        listView.setAdapter(	// 要设置数据
                new ArrayAdapter<String>(	// 所有的数据是字符串
                        this,	// 上下文对象
                        android.R.layout.simple_list_item_1,
                        this.dbCursor.find() ));
       this.mylayout.addView(listView) ;
    }


    //监听添加按钮事件
    private class OnClickListenerInsert implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SQLiteActivity.this.openSQLite();
            SQLiteActivity.this.database.insert("王永政" + count++, "1991-04-15");
System.out.println("增加成功==="+"王永政" + count+",1991-04-15");
        }
    }
    //监听修改（更新）按钮事件
    private class OnClickListenerUpdate implements View.OnClickListener{
         //ID自动增长，删除id为3的数据，3这个位置不能被重新占用
        @Override
        public void onClick(View v) {
            SQLiteActivity.this.openSQLite();
            SQLiteActivity.this.database.update(3,"郑州大学","2000-01-01");

        }
    }
    //监听删除按钮事件
    private class OnClickListenerDelete implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            SQLiteActivity.this.openSQLite();
            SQLiteActivity.this.database.delete(3);

        }
    }


    //创建新数据库，以写的方式打开
    private void buildNewSQLite(){
         helper  = new MySQLiteOpenHelper(this);
        helper.getWritableDatabase();
    }

}
