import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ArMLMessageRequestHandler extends DefaultHandler {

	private boolean ref = false;
	private boolean name = false;
	private boolean value = false;
	
	private List<ArMLMessageRequest> listArMLMessageRequest = new ArrayList<ArMLMessageRequest>();
	private ArMLMessageRequest objArMLMessageRequest = null;
	private List<String> listAttributrName = null;
	private List<String> listAttributrValue = null;
	
	@Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes)
            throws SAXException {
	
		if(tagName.equalsIgnoreCase("Request")) 
		{
			objArMLMessageRequest =new ArMLMessageRequest();
			listAttributrName = new ArrayList<String>();
			listAttributrValue = new ArrayList<String>();
		}
		
		if(tagName.equalsIgnoreCase("Ref")) ref = true;
		if(tagName.equalsIgnoreCase("Name")) name = true;
		if(tagName.equalsIgnoreCase("Value")) value = true;
		
	}
	
		
	 @Override
	 public void characters(char ch[], int start, int length) throws SAXException {
	 
		 if(ref) { objArMLMessageRequest.setRef(new String(ch, start, length)); ref = false; }
		 if(name) { listAttributrName.add (new String(ch, start, length)); name =false; }
		 if(value) { listAttributrValue.add(new String(ch, start, length)); value = false; }
		    
	 }
	 
	 @Override
	 public void endElement(String uri, String localName, String tagName) throws SAXException {
	
		if(tagName.equalsIgnoreCase("Request")) 
		{
			objArMLMessageRequest.setAttributeName(listAttributrName);
			objArMLMessageRequest.setAttributeValue(listAttributrValue);
			listArMLMessageRequest.add(objArMLMessageRequest);
			objArMLMessageRequest =null;
			listAttributrName = null;
			listAttributrValue = null;
		}
		
	}
	 
	 public List<ArMLMessageRequest> getArMLMessage(){
		 return listArMLMessageRequest;
	 }

}
