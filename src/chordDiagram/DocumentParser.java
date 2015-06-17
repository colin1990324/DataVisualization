/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chordDiagram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Sunburst.StopWords;

/**
 * Class to read documents
 * 
 * @author Mubin Shrestha
 */
public class DocumentParser {

	// This variable will hold all terms of each document in an array.
	private List<String[]> termsDocsArray = new ArrayList<String[]>();
	private List<String> allTerms = new ArrayList<String>(); // to hold all
																// terms
	private List<double[]> tfidfDocsVector = new ArrayList<double[]>();

	/**
	 * Method to read files and store in array.
	 * 
	 * @param filePath
	 *            : source file path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parseFiles(String filePath) throws FileNotFoundException,
			IOException {
		File[] blog = new File(filePath + "/_posts/blog").listFiles();
		File[] life = new File(filePath + "/_posts/life").listFiles();
		File[] tech = new File(filePath + "/_posts/tech").listFiles();
		File[] allfiles = concat(blog, life, tech);
		System.out.println("article numbers :" + allfiles.length);
		Map<String, Boolean> stopWords = StopWords.getStopWords();
		BufferedReader in = null;
		for (File f : allfiles) {
			if (f.getName().endsWith(".markdown")
					|| f.getName().endsWith(".md")) {
				in = new BufferedReader(new FileReader(f));
				StringBuilder sb = new StringBuilder();
				String s = null;
				while ((s = in.readLine()) != null) {
					sb.append(s);
				}
				String[] tokenizedTerms = sb.toString()
						.replaceAll("[\\W&&[^\\s]]", "").split("\\W+"); // to
																		// get
																		// individual
																		// terms
				for (String term : tokenizedTerms) {
					if (stopWords.containsKey(term.toLowerCase()))
						continue;
					if (!allTerms.contains(term)) { // avoid duplicate entry
						allTerms.add(term);
					}
				}
				termsDocsArray.add(tokenizedTerms);
			}
		}

		System.out.println("all files scanned");
	}

	/**
	 * Method to create termVector according to its tfidf score.
	 */
	public void tfIdfCalculator() {
		double tf; // term frequency
		double idf; // inverse document frequency
		double tfidf; // term requency inverse document frequency
		for (String[] docTermsArray : termsDocsArray) {
			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			for (String terms : allTerms) {
				tf = new TfIdf().tfCalculator(docTermsArray, terms);
				idf = new TfIdf().idfCalculator(termsDocsArray, terms);
				tfidf = tf * idf;
				tfidfvectors[count] = tfidf;
				count++;
			}
			tfidfDocsVector.add(tfidfvectors); // storing document vectors;
		}
	}

	/**
	 * Method to calculate cosine similarity between all the documents.
	 */
	public void getCosineSimilarity(String url) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(url + "/chord/max.json");
			fw.write("[");
			for (int i = 0; i < tfidfDocsVector.size(); i++) {
				fw.write("[");
				for (int j = 0; j < tfidfDocsVector.size(); j++) {
					if (i == j) {
						fw.write("0");
						if (j < tfidfDocsVector.size() - 1)
							fw.write(",");
						continue;
					} else {
						fw.write(String.valueOf(CosineSimilarity
								.cosineSimilarity(tfidfDocsVector.get(i),
										tfidfDocsVector.get(j))));
						if (j < tfidfDocsVector.size() - 1)
							fw.write(",");
					}
				}
				if (i < tfidfDocsVector.size() - 1)
					fw.write("],");
				else
					fw.write("]]");
			}
			System.out.println(url + "/chord/max.json is generated.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public File[] concat(File[]... arrays) {
		int lengh = 0;
		for (File[] array : arrays) {
			lengh += array.length;
		}
		File[] result = new File[lengh];
		int pos = 0;
		for (File[] array : arrays) {
			for (File element : array) {
				result[pos] = element;
				pos++;
			}
		}
		return result;
	}
}
