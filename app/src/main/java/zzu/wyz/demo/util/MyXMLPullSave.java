package zzu.wyz.demo.util;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class MyXMLPullSave {
	private List<LinkMan> all = null;
	private OutputStream output = null;

	public MyXMLPullSave(OutputStream output, List<LinkMan> all) {
		this.output = output;
		this.all = all;
	}

	public void save() throws Exception {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlSerializer xs = factory.newSerializer();
		xs.setOutput(this.output, "UTF-8");
		//开始文档的写出，设置编码格式
		xs.startDocument("UTF-8", true);
		// 根元素，包含多个linkman元素，设置命名空间为null
		xs.startTag(null, "addresslist");
		//调用List集合的的Iterator接口，循环读取数据
		Iterator<LinkMan> iter = this.all.iterator();
		while (iter.hasNext()) {
			//依次取出LinkMan的数据，并写出到文件中
			LinkMan man = iter.next();
			//linkman开始标记
			xs.startTag(null, "linkman");

			xs.startTag(null, "name");
			xs.text(man.getName());
			xs.endTag(null, "name");
			//linkman中的两个元素分别保存
			xs.startTag(null, "email");
			xs.text(man.getEmail());
			xs.endTag(null, "email");
			//linkman结束标记
			xs.endTag(null, "linkman");
		}
		//文档结束标记
		xs.endTag(null, "addresslist");
		xs.endDocument();
		xs.flush();
	}
}
