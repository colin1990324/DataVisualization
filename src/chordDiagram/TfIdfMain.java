/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chordDiagram;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author Mubin Shrestha
 */
public class TfIdfMain {

	public static String url = "/Users/ColinMac/Documents/colin1990324.github.io.raw";

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String args[]) throws FileNotFoundException,
			IOException {
		Article.main(args);
		DocumentParser dp = new DocumentParser();
		dp.parseFiles(url);
		dp.tfIdfCalculator(); // calculates tfidf
		dp.getCosineSimilarity(url); // calculated cosine similarity
	}
}
