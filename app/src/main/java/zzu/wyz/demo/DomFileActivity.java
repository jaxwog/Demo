package zzu.wyz.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import zzu.wyz.demo.util.LinkMan;
import zzu.wyz.demo.util.MySAXRead;
import zzu.wyz.demo.util.MyXMLPullRead;
import zzu.wyz.demo.util.MyXMLPullSave;

public class DomFileActivity extends Activity {

    private EditText domname,domemail;
    private Button dombut;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dom_file);

        domname = (EditText)super.findViewById(R.id.domname);
        domemail = (EditText)super.findViewById(R.id.domemail);
        dombut = (Button)super.findViewById(R.id.dombut);

        dombut.setOnClickListener(new OnClickListenerImpl());

    }

    private class OnClickListenerImpl implements View.OnClickListener{
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //如果SD卡不存在返回被调用处
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                return;
            }
            //文件输出和读取的路径
           file = new File(Environment.getExternalStorageDirectory()
                    +File.separator+"zzu"+File.separator+"member.xml");
          //调用解析与保存xml文件的方法
             // DomFileActivity.this.saveDomToSDcard();
            // DomFileActivity.this.readDomFromSDcard();
            //DomFileActivity.this.readSAXFromSDcard();
           // DomFileActivity.this.readXMLPullFromSDcard();
            DomFileActivity.this.saveXMLPullToSDcard();
        }
    }


    //通过XMLPull保存xml文件，从显示在编辑框之中获取内容
    public void saveXMLPullToSDcard(){
        if (!file.getParentFile().exists()) { // 文件不存在
            file.getParentFile().mkdirs() ;	// 创建文件夹
        }
        List<LinkMan> all = new ArrayList<LinkMan>() ;
        //扫描到的数据写入到list集合里面，可以定义List到类体里面

        //定义写入xml文件的list集合，也可以采用list2.addAll(list1)
        for (int x = 0; x < 3; x++) {
            LinkMan man = new LinkMan() ;
            man.setName("王永政 - " + x ) ;
            man.setEmail("zheng31326@126.com") ;
            all.add(man) ;
        }
        OutputStream output = null ;
        try {
            //输出流，并把list集合数据写入到
            output = new FileOutputStream(file) ;

            new MyXMLPullSave(output,all).save() ;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //无论如何，只要输出流不为空，直接关闭
            if (output != null) {
                try {
                    output.close() ;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }


    //通过XMLPull解析xml文件，并显示在编辑框之中
    public void readXMLPullFromSDcard(){
        if (!file.exists()) { // 文件不存在
            return;
        }
        try {
            InputStream input = new FileInputStream(file);
            MyXMLPullRead util = new MyXMLPullRead(input) ;
            //从xml文件读取数据，保存到List集合里面
            List<LinkMan> all = util.getAllLinkMan() ;
            //遍历List集合，得到每个Linkman的相应的子元素的数据
            for (int i=0;i<all.size();i++) {
                DomFileActivity.this.domname.setText(all.get(i).getName());
                DomFileActivity.this.domemail.setText(all.get(i).getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


     //通过SAX解析xml文件，并在编辑框显示
    public void readSAXFromSDcard(){
        if (!file.exists()) { // 文件不存在
            return;
        }
        SAXParserFactory factory = SAXParserFactory.newInstance() ;
        SAXParser parser = null ;
        MySAXRead sax = new MySAXRead() ;
        try {
            parser = factory.newSAXParser() ;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            parser.parse(file, sax) ;
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //解析到的数据保存到了list集合里面，通过list集合循环读取数据
        List<LinkMan> all = sax.getAll() ;
        System.out.println(all.size());
        for (int i=0;i<all.size();i++) {
            DomFileActivity.this.domname.setText(all.get(i).getName());
            DomFileActivity.this.domemail.setText(all.get(i).getEmail());
        }


}



    //通过Dom从file读取文件内容到文本显示框中，Dom并不擅长读取内容，更适合保存内容到文件
    public void readDomFromSDcard(){
        // 文件不存在,直接退出
        if (!file.exists()) {
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.parse(file); // 通过文件转化文档
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //根据节点名称得到子节点，然后依次取出内容
        NodeList nl = doc.getElementsByTagName("linkman");
        System.out.println("nl.getLength()="+nl.getLength());
        for (int x = 0; x < nl.getLength(); x++) {
            // 取得相应位置（X）上linkman元素
            Element e = (Element) nl.item(x);
            //取出当前linkman里面的子节点元素，并显示到相应的view中
            DomFileActivity.this.domname.setText(e.getElementsByTagName("name")
                    .item(0).getFirstChild().getNodeValue());
            DomFileActivity.this.domemail.setText(e.getElementsByTagName("email")
                    .item(0).getFirstChild().getNodeValue());
        }

    }


    //DOM输出文件到SD卡方法
    public void saveDomToSDcard(){
        //父路径不存在创建相应的路径
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
        DocumentBuilder builder = null ;
        try {
            builder = factory.newDocumentBuilder() ;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null ;
        doc = builder.newDocument() ;	// 创建一个新的文档
        //定义根节点
        Element addresslist = doc.createElement("addresslist") ;

        for (int i=0;i<3;i++){
            //每循环一次就添加一条linkman
            Element linkman = doc.createElement("linkman") ;
            Element name = doc.createElement("name") ;
            Element email = doc.createElement("email") ;
            //填充内容
            name.appendChild(doc.createTextNode(DomFileActivity.this.domname.getText()
                    .toString()));
            email.appendChild(doc.createTextNode(DomFileActivity.this.domemail.getText()
                    .toString()));

            linkman.appendChild(name) ;
            linkman.appendChild(email) ;
            addresslist.appendChild(linkman) ;
        }

        //循环结束，补充根节点
        doc.appendChild(addresslist) ;
        TransformerFactory tf = TransformerFactory.newInstance() ;
        Transformer t = null ;
        try {
            t = tf.newTransformer() ;
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        //设置编码格式为UTF-8
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8") ;
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file) ;
        try {
            t.transform(source, result) ;
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

}
