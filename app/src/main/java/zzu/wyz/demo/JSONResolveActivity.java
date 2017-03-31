package zzu.wyz.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONResolveActivity extends Activity {
    File file;
    JSONObject allData;
    TextView jsonmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_json);

        jsonmsg = (TextView)super.findViewById(R.id.jsonmsg);

        //生成数组格式的JSON字符串
        //this.buildJSONData();
        //生成JSONObject包含数组的JSON字符串
       // this.buildJSONMoreData();
        //解析JSON数组字符串
       // this.showJSONArray();
        this.showJSONObject();



    }


    //得到JSONObject字符串，调用解析方法，并在msg中显示出来
    public void showJSONObject(){
        String string = "{\"memberdata\":[{\"id\":1,\"name\":\"王永政\",\"age\":24},"
                + "{\"id\":2,\"name\":\"ZZU\",\"age\":10}],\"company\":\"郑州大学北校区\"}";
        //设置字符缓冲流StringBuffer
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Map<String,Object> allMap = JSONResolveActivity.this.resolveJSONObject(string);
            //将解析的allMap根据key得到数据
            stringBuffer.append("公司名称：" + allMap.get("company") + "\n");
            //得到的数据为Object类型，强制转换为List集合
            List<Map<String,Object>> list =(List<Map<String,Object>>) allMap.get("memberdata");
            //循环读取出没一行数据，并保存到StringBuff中
            Iterator<Map<String,Object>> iterator = list.iterator();
            while (iterator.hasNext()){
                Map<String,Object> map = iterator.next();

                stringBuffer.append("ID：" + map.get("id") + "，姓名：" + map.get("name")
                        + "，年龄：" + map.get("age") + "\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jsonmsg.setText(stringBuffer.toString());

    }

    //解析JSONObject，并保存到map里面
    public Map<String,Object> resolveJSONObject (String data) throws Exception{
        Map<String,Object> allMap = new HashMap<String,Object>();
        //data类型为jsonObject类型，先将字符串转化为JSONObject类型
       JSONObject jsonObject1 = new JSONObject(data);
        //解析出来JSONObject一个选项，保存到allMap中
        allMap.put("company",jsonObject1.getString("company"));
        //解析出来JsonArray
        JSONArray jsonArray = jsonObject1.getJSONArray("memberdata");

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        for (int i=0;i<jsonArray.length();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            //取得每一个jsonArray里面的JSONObject
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put("id",jsonObject.getInt("id"));
            map.put("name",jsonObject.getString("name"));
            map.put("age",jsonObject.getInt("age"));
            //得到数据，保存到list集合之中
            list.add(map);

        }
        //将jsonA解析的数据，解析到list集合里面，并保存到allMap中
        allMap.put("memberdata",list);

        return allMap;
    }


    //得到JSONArray字符串，调用解析方法，并在msg中显示出来
    public void showJSONArray(){
        String string = "[{\"id\":1,\"name\":\"王永政\",\"age\":25},{\"id\":2,\"name\":\"郑州大学\",\"age\":10}]";
        //设置字符缓冲流StringBuffer
        StringBuffer stringBuffer = new StringBuffer();
        try {
            List<Map<String,Object>> list = JSONResolveActivity.this.resolveJSONArray(string);
            //循环读取出没一行数据，并保存到StringBuff中
            Iterator<Map<String,Object>> iterator = list.iterator();
            while (iterator.hasNext()){
                Map<String,Object> map = iterator.next();

                stringBuffer.append("ID：" + map.get("id") + "，姓名：" + map.get("name")
                        + "，年龄：" + map.get("age") + "\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jsonmsg.setText(stringBuffer.toString());

    }

    //解析JSONArray数组，并保存到list集合里面，java中List集合适合保存，读取数据
    public List<Map<String,Object>> resolveJSONArray (String data) throws Exception{
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //data类型为jsonArray类型，先将字符串转化为JSON数组类型
        JSONArray jsonArray = new JSONArray(data);

        for (int i=0;i<jsonArray.length();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            //取得每一个jsonArray里面的JSONObject
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put("id",jsonObject.getInt("id"));
            map.put("name",jsonObject.getString("name"));
            map.put("age",jsonObject.getInt("age"));
            //得到数据，保存到list集合之中
            list.add(map);

        }

        return list;
    }





    //生成复杂的JSON数据
    public void buildJSONMoreData(){

       /* {"telephone":"15639707315","address":"文化路97号","persondata":
            [{"birthday":"Sat Jan 09 16:56:58 GMT+08:00 2016","salary":3000,"married":false,"age":25,
                "name":"王永政"},{"birthday":"Sat Jan 09 16:56:58 GMT+08:00 2016",
                "salary":5000,"married":true,"age":10,"name":"郑州大学"},
            {"birthday":"Sat Jan 09 16:56:58 GMT+08:00 2016","salary":9000,"married":false,"age":13,
                    "name":"ZZU"}],"company":"郑州大学北校区"}*/

       String nameData[] = new String[] { "王永政", "郑州大学", "ZZU" };
        int ageData[] = new int[] { 25, 10, 13 };
       boolean isMarraiedData[] = new boolean[] { false, true, false };
         double salaryData[] = new double[] { 3000.0, 5000.0, 9000.0 };
         Date birthdayData[] = new Date[] { new Date(), new Date(),
                new Date() };
         String companyName = "郑州大学北校区" ;
         String companyAddr = "文化路97号" ;
         String companyTel = "15639707315" ;


        allData=null;
       allData = new JSONObject(); // 建立最外面的节点对象
        JSONArray sing = new JSONArray(); // 定义数组
        for (int x = 0; x < nameData.length; x++) { // 将数组内容配置到相应的节点
            JSONObject temp = new JSONObject(); // 每一个包装的数据都是JSONObject
            try {
                temp.put("name", nameData[x]);
                temp.put("age",ageData[x]);
                temp.put("married", isMarraiedData[x]);
                temp.put("salary", salaryData[x]);
                temp.put("birthday",birthdayData[x]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sing.put(temp); // 保存多个JSONObject
        }
        try {
            allData.put("persondata", sing);
            allData.put("company", companyName) ;
            allData.put("address", companyAddr) ;
            allData.put("telephone",companyTel) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONResolveActivity.this.fileIsHave();

        JSONResolveActivity.this.outPutFileToSDcard();



    }

     //生成JSON格式的数据
    public void buildJSONData(){
       /* //输出的字符串类型
       {"urldata":
            [{"myurl":"www.zzu.edu.cn"},
            {"myurl":"WangYongZheng"},
            {"myurl":"zheng31326@126.com"}]
        }*/


        String data[] = new String[] { "www.zzu.edu.cn","WangYongZheng",
                "zheng31326@126.com" }; // 要输出的数据
        //建立最外面的节点对象
        allData=null;
        allData = new JSONObject();
        //定义JSON数组
        JSONArray sing = new JSONArray();
        //将数组内容配置到相应的节点
        for (int i=0;i<data.length;i++ ){
            JSONObject temp = new JSONObject();
            //每个数据生成一个JSONObject，key=myurl
            try {
                temp.put("myurl",data[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //每一个JSONObject添加到JSONArray中
            sing.put(temp);

        }
        //保存JSONArray到最外面的节点对象JSONObject中，key==urldata

        try {
            allData.put("urldata",sing);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONResolveActivity.this.fileIsHave();

        JSONResolveActivity.this.outPutFileToSDcard();





    }
      //判断sd卡和父文件路径
    public void fileIsHave(){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 不存在不操作
            return; // 返回到程序的被调用处
        }
         file= new File(Environment.getExternalStorageDirectory()
                + File.separator + "wyz" + File.separator + "json.txt"); // 要输出文件的路径
        if (!file.getParentFile().exists()) { // 文件不存在
            file.getParentFile().mkdirs(); // 创建文件夹
        }
    }

    public void outPutFileToSDcard(){
        PrintStream out = null;
        try {
            //不能输出汉字
            out = new PrintStream(new FileOutputStream(file));

            out.print(allData.toString()); // 将数据变为字符串后保存
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close(); // 关闭输出
            }
        }
    }

}
