import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ValueHandler extends DefaultHandler  {
	
	List<String> tagList=new ArrayList<String>();
	List<String> tagValueList=new ArrayList<String>();
	Map<String,Boolean> tagMap=new HashMap<String,Boolean>();
	int tagCount = tagList.size();
	int counter = 0;
	
	public void setTagList(List<String> tagList)
	{
		this.tagList=tagList;
	}
	
	public void getTagValues()
	{
		System.out.println(tagValueList);
	}
	
	public void startElement(String uri, String localName,String tagName, 
            Attributes attributes) throws SAXException 
    {
		System.out.println(tagList.get(counter));
		if (tagName.equalsIgnoreCase(tagList.get(counter)))
			tagMap.put(tagName, true);
		
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
			counter++;
		}
	
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	
    	if(tagMap.get(tagList.get(counter)))
    	{
    		String tagValue = new String(ch, start, length).trim();
	    	if(tagValue.length() == 0) 
	    		return;
	    	tagValueList.add(tagValue);
	    	tagMap.put(tagList.get(counter), false);
    	}
    
    }


}
