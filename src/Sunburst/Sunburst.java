package Sunburst;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Sunburst {

	public static String url = "/Users/ColinMac/Documents/colin1990324.github.io.raw";

	public static void main(String[] args) throws IOException {
		getJson();
	}

	@SuppressWarnings("unchecked")
	public static void getJson() throws IOException {
		JSONObject root = new JSONObject();
		root.put("name", "wurui.cc");
		JSONArray level1 = new JSONArray();
		root.put("children", level1);

		JSONObject page = new JSONObject();
		page.put("name", "page");
		JSONArray level21 = new JSONArray();
		page.put("children", level21);

		JSONObject blog = new JSONObject();
		blog.put("name", "blog");
		JSONArray level22 = new JSONArray();
		blog.put("children", level22);

		JSONObject life = new JSONObject();
		life.put("name", "life");
		JSONArray level23 = new JSONArray();
		life.put("children", level23);

		JSONObject tech = new JSONObject();
		tech.put("name", "tech");
		JSONArray level24 = new JSONArray();
		tech.put("children", level24);

		level1.add(page);
		level1.add(blog);
		level1.add(life);
		level1.add(tech);

		String[] pageUrl = { "about", "search", "word-cloud", "sunburst" };
		for (String s : pageUrl) {
			level21.add(getTitle(url + "/" + s + "/index.md"));
		}

		// add posts
		File dir = new File(url + "/_posts/blog");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				level22.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		dir = new File(url + "/_posts/life");
		directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				level23.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		dir = new File(url + "/_posts/tech");
		directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				level24.add(getTitle(child));
			}
		} else {
			System.out.println(dir.getPath() + "is not really a directory.");
		}

		try {

			FileWriter file = new FileWriter(url + "/sunburst/blog.json");
			file.write(root.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static JSONObject getTitle(String url) throws IOException {
		File file = new File(url);
		return getTitle(file);
	}

	public static JSONObject getTitle(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		br.readLine();
		br.readLine();
		String title = br.readLine();
		title = title.substring(8, title.length() - 1);
		System.out.print("title: " + title);
		System.out.println("    size: " + file.length());
		JSONObject obj = new JSONObject();
		obj.put("name", title);
		obj.put("value", new Long(file.length()));
		return obj;
	}

}
