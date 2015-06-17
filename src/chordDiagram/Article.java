package chordDiagram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Article {

	public static String url = "/Users/ColinMac/Documents/colin1990324.github.io.raw";

	public static void main(String[] args) throws IOException {

		List<Art> list = new ArrayList<Art>();

		// add posts
		File dir = new File(url + "/_posts/blog");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				list.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		dir = new File(url + "/_posts/life");
		directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				list.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		dir = new File(url + "/_posts/tech");
		directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				list.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		generateCSV(list, url + "/chord/articles.csv");
	}

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final Object[] FILE_HEADER = { "name", "value", "color" };

	public static void generateCSV(List<Art> list, String url)
			throws IOException {

		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withRecordSeparator(NEW_LINE_SEPARATOR);

		FileWriter fileWriter = new FileWriter(url);
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		csvFilePrinter.printRecord(FILE_HEADER);

		for (Art a : list) {
			List<String> record = new ArrayList<String>();
			record.add(a.name);
			record.add(String.valueOf(a.value));
			if(a.category.equals("blog"))
				record.add("#F781BF");
			if(a.category.equals("life"))
				record.add("#377EB8");
			if(a.category.equals("tech"))
				record.add("#FF7F00");

			csvFilePrinter.printRecord(record);
		}

		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
			System.out.println("articles.csv generated successfully.");
		} catch (IOException e) {
			System.out
					.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			e.printStackTrace();
		}

	}

	public static Art getTitle(String url) throws IOException {
		File file = new File(url);
		return getTitle(file);
	}

	public static Art getTitle(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		br.readLine();
		br.readLine();
		String title = br.readLine();
		title = title.substring(8, title.length() - 1);
		System.out.print("title: " + title);
		System.out.println("    size: " + file.length());
		Art obj = new Art();
		obj.name = title;
		obj.value = new Long(file.length());
		obj.category = file.getParentFile().getName();
		fileReader.close();
		br.close();
		return obj;
	}
}

class Art {
	String name;
	long value;
	String category;
}