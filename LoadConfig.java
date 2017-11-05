import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.scb.edm.spark.datagrid.common.AppPropLoader;

public class LoadConfig {

	public static void main(String[] args) {
		
		LoadConfig.writeToFile();
		System.out.println("DDL file created");
	}

	private static Map<String, StringBuilder> readTableConfig(String configFileDirectoy) {
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		StringBuilder configData = new StringBuilder();
		File directory = new File(configFileDirectoy);
		File[] listOfFiles = directory.listFiles();
		Map<String, StringBuilder> listOfConfigFiles = new HashMap<>();

		try {

			for (File files : listOfFiles) {
				if (files.isFile()) {
					String filePath = configFileDirectoy + "\\" + files.getName();
					File file = new File(filePath);
					if (!file.exists())
						System.out.println("File does not exist at specified location .. " + filePath);
					if (0L == file.getTotalSpace())
						System.out.println("File is empty. Please check content .. " + filePath);
					fileReader = new FileReader(filePath);
					bufferReader = new BufferedReader(fileReader);
					String line = "";

					while ((line = bufferReader.readLine()) != null)
						// fileData +=line+'\n';
						configData.append(line).append('\n');

					listOfConfigFiles.put(files.getName(), configData);
				}
			}

		} catch (IOException e) {
			System.out.println("There is some problem while reading from file.. " + e);

		} finally {

			try {

				if (bufferReader != null)
					bufferReader.close();

				if (fileReader != null)
					fileReader.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		return listOfConfigFiles;
	}

	private static void writeToFile() {
		String ddlFilePath=AppPropLoader.getValue("hive.ddl.dir");
		String ddlFileName=AppPropLoader.getValue("hive.ddl.file");
		String filePath=ddlFilePath+ddlFileName;
		List<StringBuilder> queryList = new ArrayList<>();
		queryList=LoadConfig.createTable();
		
		try {
			if (null != queryList && null != filePath) {
				File file = new File(filePath);
				if (file.exists())
					file.delete();
				file.createNewFile();
				FileWriter fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				for(StringBuilder query:queryList)
				{
					bufferWritter.append(query);
					bufferWritter.flush();
				}
				
				fileWritter.close();
			} else
				System.out.println(" Hive DDl file written to .. " + filePath);
		} catch (IOException e) {
			System.out.println("Ther is problem while writing to file.. " + e);

		}
	}

	private static List<StringBuilder> createTable() {

		Map<String,TableConfig> configMap=ConfigLoader.configLoaderFactory();
		Iterator<Map.Entry<String,TableConfig>> eachConfigFile=configMap.entrySet().iterator();
		StringBuilder query = new StringBuilder();
		char newLine='\n';
		List<StringBuilder> queryList=new ArrayList<>();
		
		while(eachConfigFile.hasNext())
		{
			Map.Entry<String, TableConfig> entry = eachConfigFile.next();
			TableConfig tableConfig=entry.getValue();
			query.append("CREATE " + tableConfig.getTableType() + " TABLE IF NOT EXISTS " + tableConfig.getDatabase() + "." + tableConfig.getTableName());
			query.append(newLine);
			query.append("(");
			query.append(newLine);
			query.append(tableConfig.getColumnList());
			query.append(newLine);
			query.append(")");
			
			if( null != tableConfig.getPartitionedBy())
			{
				query.append(newLine);
				query.append("PARTITIONED BY ( "+ tableConfig.getPartitionedBy() + " )");
			}
				
			if( null != tableConfig.getFieldsTerminatedBy())
			{
				query.append(newLine);
				query.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY "+ tableConfig.getFieldsTerminatedBy());
			}
				
			
			if( null != tableConfig.getStoredAs())
			{
				query.append(newLine);
				query.append("STORED AS "+ tableConfig.getStoredAs());
			}
				
			query.append(newLine);
			query.append(";");
			query.append(newLine);
			query.append(newLine);
			queryList.add(query);
		}
		
		return 	queryList;
	}
}
