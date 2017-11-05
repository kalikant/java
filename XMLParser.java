import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XMLParser {

	private static String sourceFilePath = null;
	private static String targetFilePath = null;
	private static String columnSeperator = null;
	private static String rowSeperator = null;
	
	static List<String> tagList=new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		
		sourceFilePath=args[0];
		targetFilePath=args[1];
		columnSeperator=args[2];
		rowSeperator = args[3];
		
		SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
		
		try {
			
			SAXParser parser= SAXFactory.newSAXParser();
			RowHandler rowHandler =new RowHandler();
			rowHandler.setColumnSeperator(columnSeperator);
			rowHandler.setRowSeperator(rowSeperator);
			parser.parse(new File(sourceFilePath),rowHandler);
			tagList = rowHandler.getRowList();
			String filePath = targetFilePath + generateFilename(sourceFilePath) ;
			for(String str:tagList)
				writeCSVFile(str.replaceAll("\\s","") + '\n',filePath);
			
			System.out.println("CSV File written to : "+ targetFilePath);
			
			 
//			ArMLMessageHeaderHandler objArMLMessageHeaderHandler = new ArMLMessageHeaderHandler();
//			parser.parse(new File(sourceFilePath),objArMLMessageHeaderHandler);
//			System.out.println(objArMLMessageHeaderHandler.getArMLMessage());
//			
//
//			ArMLMessageRequestHandler objArMLMessageRequestHandler = new ArMLMessageRequestHandler();
//			parser.parse(new File(sourceFilePath),objArMLMessageRequestHandler);
//			System.out.println(objArMLMessageRequestHandler.getArMLMessage());
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static String generateFilename(String sourceFilePath)
	{
		String tmp = sourceFilePath;
		String [] tmp1 = tmp.split("/");
		String tmp2 = tmp1[tmp1.length-1];
		String tmp3 = new StringBuilder(tmp2).reverse().toString();
		int index = tmp3.indexOf('.');
		String tmp4 = tmp3.substring(index+1);
		//System.out.println(new StringBuilder(tmp4).reverse().toString() );
		String targetFileName = new StringBuilder(tmp4).reverse().toString() ; 	
		
		return targetFileName  +  ".csv";
	}
	
	private static void writeCSVFile(String row, String filePath)
	{
		try{
			File file =new File(filePath);
    		//if file doesn't exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		//true = append file
    		FileWriter fileWritter = new FileWriter(filePath,true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.append(row);
    	        bufferWritter.flush();
    	        fileWritter.close();
    	          
    	}catch(IOException e){
    		e.printStackTrace();
    	}
	}

}
