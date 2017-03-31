package zzu.wyz.demo;

import android.app.Activity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TestMenuActivity extends Activity {

    private String []data = new String[]{
            "郑州金水区","郑州惠济区","郑州高新技术区","郑州大学北校区","软件测试王永政"
    };
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         // setContentView(R.layout.test_menu);

        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(
                this,android.R.layout.simple_dropdown_item_1line,data));
        setContentView(listView);
        super.registerForContextMenu(listView);



    }


    //子菜单选项以及监听操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu fileMenu = menu.addSubMenu("文件");
        SubMenu editMenu = menu.addSubMenu("编辑") ;
        fileMenu.add(Menu.NONE,Menu.FIRST + 1 , 1, "新建") ;
        fileMenu.add(Menu.NONE,Menu.FIRST + 2 , 2, "打开") ;
        fileMenu.add(Menu.NONE,Menu.FIRST + 3 , 3, "保存") ;
        editMenu.add(Menu.NONE,Menu.FIRST + 4 , 4, "撤消") ;
        editMenu.add(Menu.NONE,Menu.FIRST + 5 , 5, "恢复") ;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {	// 判断操作的菜单ID
            case Menu.FIRST + 1:
                Toast.makeText(this, "您选择的是“新建”项", Toast.LENGTH_LONG).show() ;
                break ;
            case Menu.FIRST + 2:
                Toast.makeText(this, "您选择的是“打开”项", Toast.LENGTH_LONG).show() ;
                break ;
            case Menu.FIRST + 3:
                Toast.makeText(this, "您选择的是“保存”项", Toast.LENGTH_LONG).show() ;
                break ;
            case Menu.FIRST + 4:
                Toast.makeText(this, "您选择的是“撤消”项", Toast.LENGTH_LONG).show() ;
                break ;
            case Menu.FIRST + 5:
                Toast.makeText(this, "您选择的是“恢复”项", Toast.LENGTH_LONG).show() ;
                break ;
        }
        return false;
    }


    /*
    //上下文菜单以及监听操作
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("信息操作");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "添加联系人");
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "查看详情");
        menu.add(Menu.NONE, Menu.FIRST + 3, 3, "删除信息");
        menu.add(Menu.NONE, Menu.FIRST + 4, 4, "另存为");
        menu.add(Menu.NONE, Menu.FIRST + 5, 5, "编辑");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                Toast.makeText(this, "您选择的是“添加联系人”项", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 2:
                Toast.makeText(this, "您选择的是“查看详情”项", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 3:
                Toast.makeText(this, "您选择的是“删除信息”项", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 4:
                Toast.makeText(this, "您选择的是“另存为”项", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 5:
                Toast.makeText(this, "您选择的是“编辑”项", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
*/
    /*
  //选项菜单监听等
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.getMenuInflater().inflate(R.menu.mymenu,menu);

       *//* menu.add(Menu.NONE,Menu.FIRST+1,1,"添加");
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "删除");
        menu.add(Menu.NONE,Menu.FIRST+3,3,"打开");
        menu.add(Menu.NONE,Menu.FIRST+4,4,"关闭");*//*

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST+1:
                Toast.makeText(this,"你选择的是添加",Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST+2:
                Toast.makeText(this,"你选择的是删除",Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST+3:
                Toast.makeText(this,"你选择的是打开",Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST+4:
                Toast.makeText(this,"你选择的是关闭",Toast.LENGTH_SHORT).show();
                break;
        }


        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Toast.makeText(this,
                "在菜单显示前预处理功能。）",
                Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }*/



}
