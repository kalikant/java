import java.util.List;

public class ArMLMessageRequest {

	private ArMLMessageHeader armlMessageHeader = null;
	private String Ref = null;
	private List<String> attributeName = null;
	private List<String> attributeValue = null;
	
	public ArMLMessageHeader getArmlMessageHeader() {
		return armlMessageHeader;
	}
	public void setArmlMessageHeader(ArMLMessageHeader armlMessageHeader) {
		this.armlMessageHeader = armlMessageHeader;
	}
	public String getRef() {
		return Ref;
	}
	public void setRef(String ref) {
		Ref = ref;
	}
	public List<String> getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(List<String> attributeName) {
		this.attributeName = attributeName;
	}
	public List<String> getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(List<String> attributeValue) {
		this.attributeValue = attributeValue;
	}
	
}
