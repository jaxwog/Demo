package zzu.wyz.demo.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MyXMLPullRead {
	private InputStream input = null ;
	public MyXMLPullRead(InputStream input) {
		this.input = input ;
	}
	public List<LinkMan> getAllLinkMan() throws Exception{
		List<LinkMan> all = null ;
		LinkMan man = null ;
		String elementName = null ;	// 保存节点的名称
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance() ;
		XmlPullParser xpp = factory.newPullParser() ;
		xpp.setInput(this.input, "UTF-8") ;
		int eventType = xpp.getEventType() ;	// 取得事件码
		// 不是文档底部，执行读取操作
		while(eventType != XmlPullParser.END_DOCUMENT) {
			if(eventType == XmlPullParser.START_DOCUMENT) {	// 文档开始
				all = new ArrayList<LinkMan>() ;//初始化List集合
			} else if (eventType == XmlPullParser.START_TAG) {	// 元素标记开始
				elementName = xpp.getName() ;	// 取得元素的名称
				if("linkman".equals(elementName)) {
					//元素linkman开始，初始化LinkMan
					man = new LinkMan() ;
				}
			} else if (eventType == XmlPullParser.END_TAG) {	// 结束元素
				elementName = xpp.getName() ;	// 取得节点名称
				//元素linkman结束，把linkman元素添加进入list集合，并清空Linkman循环下一次出现
				if("linkman".equals(elementName)) {
					all.add(man) ;
					man = null ;
				}
			} else if (eventType == XmlPullParser.TEXT) {	// 数据
				//如果是数据，取得数据并添加到LinkMan中，
				if("name".equals(elementName)) {
					man.setName(xpp.getText()) ;
				} else if ("email".equals(elementName)) {
					man.setEmail(xpp.getText()) ;
				}
			}
			// 取得下一个事件码，依次读取的关键，==读取文件的hashnext（）方法
			eventType = xpp.next() ;
		}
		return all ;
	}
}
