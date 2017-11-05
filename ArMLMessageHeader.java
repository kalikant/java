public class ArMLMessageHeader {
	
	private String messageID =null;
	private String systemID = null;
	private String location = null;
	private String timeZone = null;
	private String dateTimeForm = null;
	private String generatedTime = null;
	
	
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public String getSystemID() {
		return systemID;
	}
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getDateTimeForm() {
		return dateTimeForm;
	}
	public void setDateTimeForm(String dateTimeForm) {
		this.dateTimeForm = dateTimeForm;
	}
	public String getGeneratedTime() {
		return generatedTime;
	}
	public void setGeneratedTime(String generatedTime) {
		this.generatedTime = generatedTime;
	}
	
	@Override
	public String toString() {
		return "ArMLMessageHeader [messageID=" + messageID + ", systemID="
				+ systemID + ", location=" + location + ", timeZone="
				+ timeZone + ", dateTimeForm=" + dateTimeForm
				+ ", generatedTime=" + generatedTime + "]";
	}
	
	

}
