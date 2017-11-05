import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class ListAllFileInDir {

	public  static Collection<File> getAllFiles()
	{
		String directoryName ="C:\\java\\workspace\\exceltoflatfile\\schema-config\\";
		File directory = new File(directoryName);
		return FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}
}
