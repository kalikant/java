import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class TagHandler extends org.xml.sax.helpers.DefaultHandler {
	
	List<String> tagList=new ArrayList<String>();
	
	public List<String> getTagList()
	{
		return tagList;
	}
	
	@Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes)
            throws SAXException {
		this.tagList.add(tagName);
	}
 
}
