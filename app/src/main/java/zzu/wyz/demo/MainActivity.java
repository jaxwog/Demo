package zzu.wyz.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private String data[][] = {{"001", "北京魔乐科技"},
            {"002", "www.mldnjava.cn"}, {"003", "讲师：李兴华"},
            {"004", "中国高校讲师联盟"}, {"005", "www.jiangker.com"},
            {"006", "咨询邮箱：mldnqa@163.com"}, {"007", "客户服务：mldnkf@163.com"},
            {"008", "客户电话：(010) 51283346"}, {"009", "魔乐社区：bbs.mldn.cn"},
            {"010", "程序员招聘网：http://www.javajob.cn/"}};
    private int[] imgRes = new int[]{R.drawable.ispic_a, R.drawable.ispic_b,
            R.drawable.ispic_b, R.drawable.ispic_b, R.drawable.ispic_e};

    private int[] picRes = new int[] { R.drawable.png_01, R.drawable.png_02,
            R.drawable.png_03, R.drawable.png_04, R.drawable.png_05,
            R.drawable.png_06, R.drawable.png_07, R.drawable.png_08,
            R.drawable.png_09, R.drawable.png_10, R.drawable.png_11,
            R.drawable.png_12, R.drawable.png_13, R.drawable.png_14,
            R.drawable.png_15, R.drawable.png_16, R.drawable.png_17,
            R.drawable.png_18, R.drawable.png_19, R.drawable.png_20,
            R.drawable.png_21, R.drawable.png_22, R.drawable.png_23,
            R.drawable.png_24 };

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
    private Button testBut;
    private Button testToast;

    //imageseitcher测试
    private Button testLast;
    private Button testNext;
    private ImageSwitcher imageSwitcher;
    private TextSwitcher textSwitcher;
    private Button dateBut;
    private int foot = 0;

    //Gallery和GridView
    private Gallery myGallery;
    private List<Map<String, Integer>> gList = new ArrayList<Map<String, Integer>>();
    private GridView myGridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_grid);
       // initAdapter();
        myGridView = (GridView)super.findViewById(R.id.myGridView);
       // this.myGridView.setAdapter(this.simpleAdapter);
       this.myGridView.setAdapter(new GridAdapter(this,picRes));
        this.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView showImg = new ImageView(MainActivity.this);
                showImg.setScaleType(ImageView.ScaleType.CENTER);
                showImg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
               // Map<String, Integer> map =(Map<String, Integer>) MainActivity.this.simpleAdapter.getItem(position);
               // showImg.setImageResource(map.get("img"));
                showImg.setImageResource(MainActivity.this.picRes[position]);


                Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.pic_m)
                        .setTitle("查看图片")
                        .setView(showImg)
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });



       
        /*this.imageSwitcher = (ImageSwitcher)super.findViewById(R.id.gallerySwitcher);
        this.myGallery = (Gallery) super.findViewById(R.id.myGallery);
        this.myGallery.setAdapter(simpleAdapter);
        this.imageSwitcher.setFactory(new viewFactoryImpl());
        this.myGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
          Map<String, Integer> map =(Map<String, Integer>) MainActivity.this.simpleAdapter.getItem(position);
                MainActivity.this.imageSwitcher.setImageResource(map.get("img"));

            }
        });*/
        
        

       /* this.myGallery = (Gallery)super.findViewById(R.id.myGallery);
        this.myGallery.setAdapter(new GalleryAdapter(this));
        this.myGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });*/



      /* testToast = (Button)super.findViewById(R.id.toast_but);
        testToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast myToast =   Toast.makeText(MainActivity.this, "郑州大学王永政", Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.CENTER, 60, 30);
                LinearLayout myToastView = (LinearLayout)myToast.getView();
                ImageView img = new ImageView(MainActivity.this);
                img.setImageResource(R.drawable.mldn_3g);
                myToastView.addView(img,0);


                myToast.show();


            }
        });*/



      /*  testBut = (Button)super.findViewById(R.id.test_but);

        testBut.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final ProgressDialog dialog =
                                                   ProgressDialog.show(MainActivity.this, "搜索网络", "耐性等待...");
                                           new Thread() {
                                               @Override
                                               public void run() {
                                                   try {
                                                       Thread.sleep(3000);
                                                   } catch (InterruptedException e) {
                                                       e.printStackTrace();
                                                   } finally {
                                                       dialog.dismiss();
                                                   }
                                               }
                                           }.start();
                                           dialog.show();
                                       }
                                   }

        );*/


   /*     testBut = (Button)super.findViewById(R.id.test_but);
        testBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("搜索网络");
                dialog.setMessage("请耐心等待...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(100);
                dialog.setProgress(40);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "后台处理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.onStart();
                new Thread(){


                    @Override
                    public void run() {
                        for (int i=0;i<100;i++) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dialog.incrementProgressBy(2);

                        }
                    dialog.dismiss();
                    }
                }.start();

                dialog.show();
            }
        });*/



          /*  listView=(ListView)super.

            findViewById(R.id.list_item);

            for(
            int i = 0;
            i<this.data.length;i++)

            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("_id", this.data[i][0]);
                map.put("name", this.data[i][1]);
                this.arrayList.add(map);
            }


            this.simpleAdapter=new

            SimpleAdapter(this,
                          arrayList, R.layout.data_table, new String[] {
                "_id", "name"
            }

            ,
                    new int[]

            {
                R.id._id, R.id.name
            }

            );
            this.listView.setAdapter(this.simpleAdapter);*/



       /* imageSwitcher = (ImageSwitcher)super.findViewById(R.id.imageSwitcher);
        textSwitcher = (TextSwitcher)super.findViewById(R.id.dateShow);
        dateBut = (Button)super.findViewById(R.id.dateBut);
        testLast = (Button)super.findViewById(R.id.last_but);
        testNext = (Button)super.findViewById(R.id.next_but);
        imageSwitcher.setFactory(new viewFactoryImpl());
        textSwitcher.setFactory(new TextFactoryImpl());

        textSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        imageSwitcher.setImageResource(MainActivity.this.imgRes[foot++]);

        dateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSwitcher.setText("当前时间为："+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            }
        });


        testLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageResource(MainActivity.this.imgRes[foot--]);
                MainActivity.this.checkButEnble();
            }
        });

        testNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSwitcher.setImageResource(MainActivity.this.imgRes[foot++]);
                MainActivity.this.checkButEnble();
            }
        });

        }


     private  class  viewFactoryImpl implements ViewSwitcher.ViewFactory {

         @Override
         public View makeView() {
             ImageView img  = new ImageView(MainActivity.this);
             img.setBackgroundColor(0xFFFFFFFF);
             img.setScaleType(ImageView.ScaleType.CENTER);
             img.setLayoutParams(new ImageSwitcher.LayoutParams(
                     ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

             return img;
         }
     }


    private  class  TextFactoryImpl implements ViewSwitcher.ViewFactory {

        @Override
        public View makeView() {
            TextView txt  = new TextView(MainActivity.this);
            txt.setBackgroundColor(0xFFFFFFFF);
         //   txt.setScaleType(ImageView.ScaleType.CENTER);
            txt.setTextColor(0xFF000000);
            txt.setTextSize(30);
            txt.setLayoutParams(new TextSwitcher.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            return txt;
        }
    }

    private void checkButEnble(){
    if (foot<imgRes.length-1){
        testNext.setEnabled(true);
    }else {
        testNext.setEnabled(false);
    }
        if (foot==0){
            testLast.setEnabled(false);
        }else{
            testLast.setEnabled(true);
        }

*/


    }

    private  class  viewFactoryImpl implements ViewSwitcher.ViewFactory {

        @Override
        public View makeView() {
            ImageView img  = new ImageView(MainActivity.this);
            img.setBackgroundColor(0xFFFFFFFF);
            img.setScaleType(ImageView.ScaleType.CENTER);
            img.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            return img;
        }
    }

    public void initAdapter() {
        Field[] fidlds = R.drawable.class.getDeclaredFields();
        for (int x = 0; x < fidlds.length; x++) {
            // System.out.println(fidlds[x].toString());
            if (fidlds[x].getName().startsWith("png_")) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                try {
                    map.put("img", fidlds[x].getInt(R.drawable.class));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.gList.add(map);
            }

        }

        this.simpleAdapter = new SimpleAdapter(MainActivity.this,
                this.gList, R.layout.grid_table,
                new String[]{"img"}, new int[]{R.id.gridImg});
    }
}
