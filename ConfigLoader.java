/*
* this class helps to read from a yamal configuration file and generate table config dynamically
*/

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConfigLoader {

	public static Map<String,TableConfig> configLoaderFactory() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		TableConfig tableConfig = null;
		Map<String,TableConfig> configMap=new HashMap<>();
		Map<String, String> configFiles=ConfigLoader.readTableConfigFiles();
		Iterator<Map.Entry<String, String>> entries = configFiles.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			try {
				
				tableConfig =new TableConfig();
				tableConfig = mapper.readValue(
						new File(entry.getValue()),
						TableConfig.class);
				configMap.put(entry.getKey(), tableConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return configMap;
	}

	private static Map<String, String> readTableConfigFiles() {
		
		
		File directory = new File(AppPropLoader.getValue("table.config.dir"));
		FileFilter fileFilter = new FileFilter() {

			@Override
			public boolean accept(File fileExtension) {
				if (fileExtension.getName().toLowerCase().endsWith("yaml")) {
					return true;
				}
				return false;
			}
		};

		File[] listOfFiles = directory.listFiles(fileFilter);
		Map<String, String> listOfConfigFiles = new HashMap<>();
		for (File files : listOfFiles) {
			if (files.isFile()) {
				String fileName = files.getName();
				String filePath = files.getAbsolutePath();
				listOfConfigFiles.put(fileName, filePath);
			}
		}
		return listOfConfigFiles;
	}
}
