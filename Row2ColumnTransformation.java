/*
this is helper class to transform data fron row to column
it takes fixed no of columns as input at first row and transform other rows into that many no of cloumns
e.g fixed no of columns = c and no of rows in input file is r then out put file will have c X r no of rows 
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Row2ColumnTransformation {

	public static void main(String[] args) {
		String filePath = "";
		String fileOutputPath = "";
		String finalOutputData = "";
		StringBuilder SBfinalOutputData = new StringBuilder();
		String fixedColumns = "";

		if (null == args[0])
			System.out.println("Please provide input file path ..");
		else if (null == args[1])
			System.out.println("Please provide output file path ..");
		else if (null == args[2])
			System.out.println("Please provide fixed set of columns ..");
		else {
			filePath = args[0];
			fileOutputPath = args[1];
			fixedColumns = args[2];
			SBfinalOutputData = App.transpose(filePath, fixedColumns);
			App.writeCSVFile(SBfinalOutputData, fileOutputPath);
			System.out.println("File written successfully to .. "
					+ fileOutputPath);
		}

	}

	private static StringBuilder transpose(String filePath, String fixedColumns) {
		StringBuilder fileData = App.readCSVFile(filePath);
		int fixedColumnsCount = fixedColumns.split("\\|").length;
		String[] rows = fileData.toString().split("\n");
		String headerRow = rows[0];
		String outData = "";
		StringBuilder SBoutData = new StringBuilder();

		int headerRowColCount = headerRow.split("\\|").length;
		for (int i = 1; i < rows.length; i++) {
			String row = rows[i];
			String headRow = headerRow;
			String[] rowSplit = new String[headerRowColCount];

			rowSplit = row.split("\\|");
			String[] headRowSplit = headRow.split("\\|");
			String key = "";
			String value = "";
			String[] enhancedRowSplit = new String[headerRowColCount];

			int rowSplitIndex = 0;
			int rowSplitLastIndex = rowSplit.length;
			int enhancedRowSplitIndex = 0;

			while (enhancedRowSplitIndex < headerRowColCount) {
				if (rowSplitIndex < rowSplitLastIndex) {
					enhancedRowSplit[enhancedRowSplitIndex] = rowSplit[enhancedRowSplitIndex];
					rowSplitIndex++;
					enhancedRowSplitIndex++;
				} else {
					enhancedRowSplit[enhancedRowSplitIndex] = "";
					enhancedRowSplitIndex++;
				}
			}
			// setting null to rowSplit to clear memory
			rowSplit = null;
			for (int j = 0; j < fixedColumnsCount; j++)
				key += enhancedRowSplit[j].trim() + "|";
			for (int k = fixedColumnsCount; k < headerRowColCount; k++) {
				value = "";
				value = key + headRowSplit[k].trim() + "|"
						+ enhancedRowSplit[k].trim();
				// outData+=value+'\n';
				SBoutData.append(value).append('\n');
				// System.out.println(value);
			}

		}
		return SBoutData;
	}

	private static StringBuilder readCSVFile(String filePath) {
		FileReader fileReader = null;
		BufferedReader bufferReader = null;
		String fileData = "";
		StringBuilder SBfileData = new StringBuilder();

		try {
			File file = new File(filePath);
			if (!file.exists())
				System.out
						.println("File does not exist at specified location .. "
								+ filePath);
			fileReader = new FileReader(filePath);
			bufferReader = new BufferedReader(fileReader);
			String line = "";

			while ((line = bufferReader.readLine()) != null)
				// fileData +=line+'\n';
				SBfileData.append(line).append('\n');

		} catch (IOException e) {
			System.out
					.println("Ther is problem while reading from file.. " + e);

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

		return SBfileData;
	}

	private static void writeCSVFile(StringBuilder row, String filePath) {
		try {
			if (null != row && null != filePath) {
				File file = new File(filePath);
				if (!file.exists())
					file.createNewFile();
				FileWriter fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.append(row);
				bufferWritter.flush();
				fileWritter.close();
			} else
				System.out.println("in side writeCSVFile() .. row :   " + row
						+ " filePath : " + filePath);
		} catch (IOException e) {
			System.out.println("Ther is problem while writing to file.. " + e);

		}
	}
}
