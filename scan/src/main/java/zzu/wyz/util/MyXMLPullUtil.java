package zzu.wyz.util;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class MyXMLPullUtil {
	private List<LinkMan> all = null;
	private OutputStream output = null;

	public MyXMLPullUtil(OutputStream output, List<LinkMan> all) {
		this.output = output;
		this.all = all;
	}

	public void save() throws Exception {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlSerializer xs = factory.newSerializer();
		xs.setOutput(this.output, "UTF-8");
		xs.startDocument("UTF-8", true);
		xs.startTag(null, "OrderHopperNumber");
		Iterator<LinkMan> iter = this.all.iterator();
		while (iter.hasNext()) {

			LinkMan man = iter.next();
			xs.startTag(null, "linkman");
			xs.startTag(null, "order");
			xs.text(man.getOrder());
			xs.endTag(null, "order");
			xs.startTag(null, "hopper");
			xs.text(man.getHopper());
			xs.endTag(null, "hopper");
			xs.startTag(null, "number");
			xs.text(man.getNumber());
			xs.endTag(null, "number");
			xs.endTag(null, "linkman");
		}
		xs.endTag(null, "OrderHopperNumber");
		xs.endDocument();
		xs.flush();
	}
}
