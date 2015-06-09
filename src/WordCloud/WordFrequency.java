package WordCloud;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import Sunburst.StopWords;

public class WordFrequency {

	public static void main(String[] args) {

		String url = "/Users/ColinMac/Documents/colin1990324.github.io.raw";
		
		// get all files
		List<File> fileList = getFiles(url);
		
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		for (File file : fileList) {
			// count word frequency
			wordCount(file, wordMap);
		}

		// sort by counter.
		wordMap = sortByComparator(wordMap);

		// calculate weight
		int counter = 1;
		int sum = 0;
		int max = 0;
		int min = 0;
		for (Entry<String, Integer> entry : wordMap.entrySet()) {
			if (counter == 1)
				max = entry.getValue();
			if (counter == 100)
				min = entry.getValue();
			System.out.println(entry.getKey() + ": " + entry.getValue());
			sum += entry.getValue();
			counter++;
			if (counter > 100)
				break;
		}

		// output 
		float arg = 90 / ((float) max - min);
		counter = 1;
		for (Entry<String, Integer> entry : wordMap.entrySet()) {
			System.out.println("myMap.set(\"" + entry.getKey() + "\", "
					+ (int) (10 + (entry.getValue() - min) * arg) + ");");
			counter++;
			if (counter > 100)
				break;
		}

		counter = 1;
		for (Entry<String, Integer> entry : wordMap.entrySet()) {
			System.out.print("\"" + entry.getKey() + "\",");
			if (counter % 10 == 0)
				System.out.println();
			counter++;
			if (counter > 100)
				break;
		}

	}

	public static List<File> getFiles(String url) {

		List<File> fileList = new ArrayList<File>();

		// add posts
		File dir = new File(url + "/_posts");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					File[] blogs = child.listFiles();
					for (File blog : blogs) {
						fileList.add(blog);
					}
				}
			}
		} else {
			System.out.println(url + "is not really a directory.");
		}

		// add pages
		String[] pages = { "about" };
		for (String f : pages) {
			fileList.add(new File(url + "/" + f + "/index.md"));
		}

		return fileList;
	}

	public static void wordCount(File file, Map<String, Integer> wordMap) {

		Map<String, Boolean> stopWords = StopWords.getStopWords();
		FileReader fileReader = null;
		BufferedReader br = null;
		try {
			fileReader = new FileReader(file);
			br = new BufferedReader(fileReader);

			System.out.println("Processing file: " + file.getName());

			String s;
			int counter = 0;
			while ((s = br.readLine()) != null) {
				// English split words
				// String[] ss = s.split("[^a-zA-Z]+");
				
				// now using Chinese ansj_seg analysis
				ArrayList<String> ss = segmentText(s);
				for (String word : ss) {
					if (stopWords.containsKey(word.toLowerCase()))
						continue;
					if (wordMap.containsKey(word))
						wordMap.put(word, wordMap.get(word) + 1);
					else
						wordMap.put(word, 1);
				}
				// limit blogs that are too long, which will take too much
				// weight.
				counter++;
				if (counter > 30)
					break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<String> segmentText(String str) {
		List<Term> termList = ToAnalysis.parse(str);
		ArrayList<String> terms = new ArrayList<String>(termList.size());

		for (Term term : termList) {
			if (term.getNatureStr().equals("n")
					|| (term.getNatureStr().equals("en") && term.getName()
							.length() > 3))
				terms.add(term.getName());
		}

		return terms;
	}

	public static Map<String, Integer> sortByComparator(
			Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Collections.reverse(list);

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
