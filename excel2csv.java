/*
this helper class has been written to convert .xls and .xlsx files into csv format 
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excel2csv {

	static Properties prop = null;
	static final Logger logger = Logger.getLogger(excel2csv.class);

	/*static {

		// loading properties
		if (logger.isInfoEnabled())
			logger.info("Loading application properties..");

		prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("application.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (logger.isInfoEnabled())
			logger.info("Application properties loaded..");

	}*/

	static String sourceFilePath = null;
	static String targetFilePath = null;
	static String columnSeperator = null;
	static char rowSeperator = '\n';
	static String fileExtention = null;
	static String isSheetnameInclude=null;
	private static List<String> fileList = new ArrayList<String>();

	public static void main(String[] args) {
		
		 
		
        
		if(args.length == 1)
		{
			if(!args[0].isEmpty() && args[0] != null)
			{
				String location = args[0];
				// loading properties
				if (logger.isInfoEnabled())
					logger.info("Loading application properties..");

				prop = new Properties();
				InputStream input = null;
				try {
					input = new FileInputStream(location);
					prop.load(input);
				} catch (IOException ex) {
					logger.error("Issue loading apllication property file......");
					ex.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				if (logger.isInfoEnabled())
					logger.info("Application properties loaded..");
			}
			
		}else {
			logger.info("Please provide application properties file ... " + targetFilePath);
		}
		sourceFilePath = prop.getProperty("excel.file.inpth");
		//sourceFilePath = args[0];
		targetFilePath = prop.getProperty("converted.file.dir");
		columnSeperator = prop.getProperty("data.delimiter");
		fileExtention = prop.getProperty("file.extention");
		isSheetnameInclude=prop.getProperty("isSheetnameInclude");
		
		//rowSeperator = prop.getProperty("row.seperator").charAt(0);
		File[] listOfFiles=null;
		try{
			File incommingFolder = new File(sourceFilePath);
			if(incommingFolder.isDirectory() && incommingFolder.exists())
			{
				listOfFiles = incommingFolder.listFiles();
			}
		}catch (Exception ex) {
			logger.error("Issue loading incomming directory......");
			ex.printStackTrace();
		} 
		for(File file:listOfFiles)
		{
			String sourceFilePath1=file.getAbsolutePath();
			if (null != sourceFilePath) {
				if ("xlsx".equalsIgnoreCase(getFileType(sourceFilePath1))) {
					logger.info("File Type is : " + getFileType(sourceFilePath1));
					excel2csv.readXLSXFile(sourceFilePath1, targetFilePath,
							columnSeperator, rowSeperator,new Boolean(isSheetnameInclude));
					logger.info("flat files are written to : " + targetFilePath);
					System.out.println("success");
				}
				else if ("xls".equalsIgnoreCase(getFileType(sourceFilePath1))){
					logger.info("File Type is : " + getFileType(sourceFilePath1));
					excel2csv.readXLSFile(sourceFilePath1, targetFilePath,
							columnSeperator, rowSeperator,new Boolean(isSheetnameInclude));
					logger.info("flat files are written to : " + targetFilePath);
					System.out.println("success");
				}
				else
					logger.info("Sorry ! we can process only .xlsx and xls extentions, your extention is " 
							+ getFileType(sourceFilePath));
			} else
				logger.info("Source path for file is missing..");
			
		}
		

	}
	
	/*private List<String> listFiles(){
        String directoryName = sourceFilePath = prop.getProperty("excel.file.dir");
		File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList)
            if (file.isFile()) fileList.add(file.getName());
        
        return fileList;
       }*/

	private static String getFileType(String filePath) {
		logger.info("In side getFileType method..");
		String path = filePath;
		String extention = new StringBuilder(path).reverse().toString()
				.substring(0, 4);
		if ("xslx".equalsIgnoreCase(extention))
			return "xlsx";
		return "xls";
	}

	private static void readXLSXFile(String sourceFilePath,
			String targetFilePath, String columnSeperator, char rowSeperator,boolean isSheetNameIncludes) {
		logger.info("sourceFilePath : " + sourceFilePath);
		logger.info("targetFilePath : " + targetFilePath);
		logger.info("columnSeperator : " + columnSeperator);
		logger.info("rowSeperator : " + rowSeperator);

		
		if (null != sourceFilePath && null != targetFilePath
				&& null != columnSeperator) {
			try {
				File file=new File(sourceFilePath);
				FileInputStream fileIp = new FileInputStream(file);
				HSSFDataFormatter objHSSFDataFormatter=new HSSFDataFormatter();
				
				XSSFWorkbook workbook = new XSSFWorkbook(fileIp);
				Iterator<XSSFSheet> sheetIterator = workbook.iterator();
				while (sheetIterator.hasNext()) {
					XSSFSheet sheet = sheetIterator.next();
					String targetFileName = generateFilename(file.getName(),sheet
							.getSheetName(),isSheetNameIncludes);
					
					String filePath = targetFilePath+"//"+targetFileName;
					Iterator<Row> rowIterator = sheet.iterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						Row headerRow=sheet.getRow(0);
						int columnCount=headerRow.getLastCellNum();
						//System.out.println("head row column count " +columnCount);
						
						String tupple = "";
						String blankTupple="";
							
						//Iterator<Cell> cellIterator = row.cellIterator();
						//while (cellIterator.hasNext()) {
						for(int i=0;i<columnCount;i++)
						{
							//Cell cell = cellIterator.next();
							blankTupple+= "" + columnSeperator;
							Cell cell=row.getCell(i);
							if ( null == cell )
								tupple += "" + columnSeperator;
							else
							//if( null != cell)
							{
								int CELL_TYPE = cell.getCellType();
								
								if (CELL_TYPE == Cell.CELL_TYPE_NUMERIC) {
									if (HSSFDateUtil.isCellDateFormatted(cell))
										tupple += applyDateFormat(cell
												.getDateCellValue().toString())
												+ columnSeperator;
									else
										tupple += cell.getNumericCellValue() + columnSeperator;
										//tupple += objHSSFDataFormatter.formatCellValue(cell)
										//+ columnSeperator;
								}
								else if (CELL_TYPE == Cell.CELL_TYPE_STRING)
									tupple +=  cell.getStringCellValue()
											+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_BOOLEAN)
									tupple += cell.getBooleanCellValue()
									+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_FORMULA){
									switch(cell.getCachedFormulaResultType()) {
						            case Cell.CELL_TYPE_NUMERIC:
						            	tupple += cell.getNumericCellValue()
										+ columnSeperator; 
						                break;
						            case Cell.CELL_TYPE_STRING:
						            	tupple += cell.getStringCellValue()
										+ columnSeperator;
						                break;
						        }
									
								}
								else if (CELL_TYPE == Cell.CELL_TYPE_ERROR)
									tupple += cell.getErrorCellValue()
									+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_BLANK)
									tupple += "" + columnSeperator;
								
							}
						}
						
						if(!tupple.equals(blankTupple))
						excel2csv.writeCSVFile((tupple.substring(0,
										tupple.length() - 1) + rowSeperator),
										filePath);
					}
				}
				fileIp.close();
			} catch (Exception e) {
				logger.error("Facing problem while parsing excel file : " + e);
				
			}
		} else
			logger.info("sourceFilePath or targetFilePath or columnSeperator is missing ");

	}

	private static void readXLSFile(String sourceFilePath,
			String targetFilePath, String columnSeperator, char rowSeperator,boolean isSheetNameIncludes) {
		logger.info("sourceFilePath : " + sourceFilePath);
		logger.info("targetFilePath : " + targetFilePath);
		logger.info("columnSeperator : " + columnSeperator);
		logger.info("rowSeperator : " + rowSeperator);

		if (null != sourceFilePath && null != targetFilePath
				&& null != columnSeperator) {

			try {
				File file = new File(sourceFilePath);
				FileInputStream fileIp = new FileInputStream(new File(
						sourceFilePath));
				HSSFWorkbook workbook = new HSSFWorkbook(fileIp);
				int sheetCount = workbook.getNumberOfSheets();
				for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
					HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
					String targetFileName = generateFilename(file.getName(),sheet
							.getSheetName(),isSheetNameIncludes);
					String filePath = targetFilePath + targetFileName;
					Iterator<Row> rowIterator = sheet.iterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						
						Row headerRow=sheet.getRow(0);
						int columnCount=headerRow.getLastCellNum();
						//System.out.println("head row column count " +columnCount);
						
						String tupple = "";
						String blankTupple="";
						
						//Iterator<Cell> cellIterator = row.cellIterator();
						//while (cellIterator.hasNext()) {
						for(int i=0;i<columnCount;i++)
						{	
							//Cell cell = cellIterator.next();
							blankTupple+= "" + columnSeperator;
							Cell cell=row.getCell(i);
							if ( null == cell )
								tupple += "" + columnSeperator;
							else
							//if ( null != cell )
							{
								int CELL_TYPE = cell.getCellType();
								
								if (CELL_TYPE == Cell.CELL_TYPE_NUMERIC) {
									if (HSSFDateUtil.isCellDateFormatted(cell))
										tupple += applyDateFormat(cell
												.getDateCellValue().toString())
												+ columnSeperator;
									else
										tupple += cell.getNumericCellValue()
												+ columnSeperator;
								}
								else if (CELL_TYPE == Cell.CELL_TYPE_STRING)
									tupple += cell.getStringCellValue()
											+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_BOOLEAN)
									tupple += cell.getBooleanCellValue()
									+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_FORMULA){
									switch(cell.getCachedFormulaResultType()) {
						            case Cell.CELL_TYPE_NUMERIC:
						            	tupple += cell.getNumericCellValue()
										+ columnSeperator; 
						                break;
						            case Cell.CELL_TYPE_STRING:
						            	tupple += cell.getStringCellValue()
										+ columnSeperator;
						                break;
						        }
									
								}
								else if (CELL_TYPE == Cell.CELL_TYPE_ERROR)
									tupple += cell.getErrorCellValue()
									+ columnSeperator;
								else if (CELL_TYPE == Cell.CELL_TYPE_BLANK)
									tupple += "" + columnSeperator;
								
							}	
						}
						if(!tupple.equals(blankTupple))
						excel2csv
								.writeCSVFile((tupple.substring(0,
										tupple.length() - 1) + rowSeperator),
										filePath);
					}
				}
				fileIp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			logger.info("sourceFilePath or targetFilePath or columnSeperator is missing ");

	}

	private static String applyDateFormat(String dateString) {
		// String dateStr = "Mon Jun 06 00:00:00 SGT 2016";
		//logger.info("Date from excel sheet " + dateString);
		String dateStr = dateString;
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy");
		Date date;
		try {
			date = (Date) formatter.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			String formatedDate = cal.get(Calendar.YEAR)
					+ prop.getProperty("excel.date.seperator")
					+ (cal.get(Calendar.MONTH) + 1)
					+ prop.getProperty("excel.date.seperator")
					+ cal.get(Calendar.DATE);
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					prop.getProperty("excel.date.format"));
			Date d_date = dateFormat.parse(formatedDate);
			String strDate = dateFormat.format(d_date);
			//logger.info("Formated date " + strDate);
			return strDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String generateFilename(String fileName,String sheetName,boolean isSheetNameInclude) {
		if( null != sourceFilePath)
		{
			String tmp = fileName;
			String[] tmp1 = tmp.split("/");
			String tmp2 = tmp1[tmp1.length - 1];
			String tmp3 = new StringBuilder(tmp2).reverse().toString();
			int index = tmp3.indexOf('.');
			String tmp4 = tmp3.substring(index + 1);
			String targetFileName = new StringBuilder(tmp4).reverse().toString();
			if(isSheetNameInclude)
			{
				return targetFileName + "_" + sheetName + "." + fileExtention;
			}else{
				return targetFileName+"."+fileExtention;
			}
			
		}
		logger.info("in side generateFilename() .. Sheet Name :   " + sheetName);
		return null;
	}

	private static void writeCSVFile(String row, String filePath) {
		try {
			if (null != row && null != filePath )
			{
				File file = new File(filePath);
				if (!file.exists())
					file.createNewFile();
				FileWriter fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.append(row);
				bufferWritter.flush();
				fileWritter.close();
			}
			else
				logger.info("in side writeCSVFile() .. row :   " + row + " filePath : " + filePath);
		} catch (IOException e) {
			logger.error("Ther is problem while writing to file.. " + e);
			
		}
	}

}
