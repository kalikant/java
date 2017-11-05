/*
helper class to read file

*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaders {

	private static BufferedReader br ;
		
	public static void main(String[] args) {
		
	}
	
	public static String readFile(String fileName)
	{
		StringBuilder fileContent=new StringBuilder();
		
		try {
				br = new BufferedReader(new FileReader(fileName));
				String line = null;
				while ((line = br.readLine()) != null) 
					fileContent.append(line+'\n');
				
				return fileContent.toString();
			
		} catch (Exception e) {
			System.out.println("Problem while reading file.. " + fileName);
		}
		finally
		{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
