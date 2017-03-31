package zzu.wyz.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class FileReadSaveActivity extends Activity {
    // 设置文件名称
  //  private static final String FILENAME="zzu";
   //    private static final String FILENAME="wyz.txt";
    //private static final String FILENAME = "/mnt/sdcard/mldndata/mymldn.txt" ;
    //private static final String FILENAME = "/storage/emulated/0/mldndata/mymldn.txt" ;

    private static final String FILENAME = "wyz.txt";
    private static final String DIR = "zzu";


    private TextView saveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_read_save);

        saveInfo = (TextView)super.findViewById(R.id.saveInfo);


        //从程序读取资源文件，调用ContextThemeWrapper类的getResources()读取内容，
        //Scanner包裹InputStream，使用缓冲流显示
        Resources resources = super.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.mybook);
        Scanner scanner = new Scanner(inputStream);
        StringBuffer stringBuffer = new StringBuffer();
        while (scanner.hasNext()){
            stringBuffer.append(scanner.next()).append("\n");
        }
        scanner.close();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveInfo.setText(stringBuffer.toString());





/*

        //*********标准********判断文件是否存在，如果存在创建文件夹，并在文件后面追加数据
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory().toString()
                    +File.separator+DIR+File.separator+FILENAME);
            //如果父文件夹不存在，创建文件夹
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            //在文件后面追加数据，采用的是传递参数为true
            PrintStream out = null;
            try {
                out = new PrintStream(new FileOutputStream(file,true));
                out.println("郑州大学（www.zzu.edu.cn），学生：王永政");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                //无论如何都关闭输出流
                if (out!=null){
                    out.close();
                }
            }

            //从sdcard读取数据并显示出来
            Scanner scan  = null ;
            try {
                scan = new Scanner(new FileInputStream(file));
                while (scan.hasNext()){
                    saveInfo.append(scan.next()+"\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                //只要输入流输入数据，无论如何都要关闭输入流
                if (scan!=null){
                    scan.close();
                }
            }

        }else {
            //sdcard不存在，提示用户
            Toast.makeText(this,"保存失败，sd卡不存在",Toast.LENGTH_SHORT).show();
        }
*/



      /*  File file = new File(FILENAME);
        if (!file.getParentFile().exists()){
            // 创建父文件夹路径
            file.getParentFile().mkdirs();
        }

        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(FILENAME));
            out.println("郑州大学（www.zzu.edu.cn），学生：王永政");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
        //无论如何都关闭输出流
           if (out!=null){
               out.close();
           }
        }
*/




/*

         //根据Activity得到对象并读取内容
        FileInputStream inputStream = null;
        try {
            inputStream = super.openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()){
            saveInfo.append(scanner.next()+"\n");
        }
        scanner.close();
*/


/*

        FileOutputStream fops = null;
        try {
            //打开activity程序继承的方法得到对象
            fops = super.openFileOutput(FILENAME,Activity.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PrintStream printStream = new PrintStream(fops);
        printStream.println("姓名：王永政；");
        printStream.println("年龄：24；");
        printStream.println("地址：郑州大学；");
        printStream.close();

*/




       /*
        //保存文件到本程序配置文件中
        SharedPreferences share = super.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();

        edit.putBoolean("zzu", true);
        edit.putInt("age", 60);
        edit.commit();

        //从本地文件获取信息
        //share.getAll();
        share.getBoolean("zzu", false);
        share.getInt("age", 0);
        */
    }

}
