
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArMLMessageHeaderHandler extends DefaultHandler {
	
	private boolean messageID = false;
	private boolean systemID = false;
	private boolean location = false;
	private boolean timeZone = false;
	private boolean dateTimeForm = false;
	private boolean generatedTime = false;
	
	private ArMLMessageHeader objArMLMessageHeader = null;
	private List<ArMLMessageHeader> listArMLMessageHeader = new ArrayList<ArMLMessageHeader>();
	
	@Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes)
            throws SAXException {
	
		if(tagName.equalsIgnoreCase("MsgHeader")) objArMLMessageHeader =new ArMLMessageHeader();
		
		if(tagName.equalsIgnoreCase("MessageID")) messageID = true;
		if(tagName.equalsIgnoreCase("SystemID")) systemID = true;
		if(tagName.equalsIgnoreCase("Location")) location = true;
		if(tagName.equalsIgnoreCase("TimeZone")) timeZone = true;
		if(tagName.equalsIgnoreCase("DateTimeForm")) dateTimeForm = true;
		if(tagName.equalsIgnoreCase("GeneratedTime")) generatedTime = true;		
		
	}
	
	 @Override
	 public void characters(char ch[], int start, int length) throws SAXException {
	 
		 if(messageID) { objArMLMessageHeader.setMessageID(new String(ch, start, length)); messageID = false; }
		 if(systemID) { objArMLMessageHeader.setSystemID(new String(ch, start, length)); systemID =false; }
		 if(location) { objArMLMessageHeader.setLocation(new String(ch, start, length)); location = false; }
		 if(timeZone) { objArMLMessageHeader.setTimeZone(new String(ch, start, length)); timeZone = false; }
		 if(dateTimeForm) { objArMLMessageHeader.setDateTimeForm(new String(ch, start, length)); dateTimeForm = false; }
		 if(generatedTime) { objArMLMessageHeader.setGeneratedTime(new String(ch, start, length)); generatedTime = false; }
		    
	 }
	 
	 @Override
	 public void endElement(String uri, String localName, String tagName) throws SAXException {
	    
		 if (tagName.equalsIgnoreCase("MsgHeader")) listArMLMessageHeader.add(objArMLMessageHeader);
	    }
	 
	 public List<ArMLMessageHeader> getArMLMessage(){
		 return listArMLMessageHeader;
	 }
}
