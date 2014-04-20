
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FrequencyAnalyst {
	private Map<String, Integer> wordCatalog = new TreeMap<String, Integer>();
	private boolean activeData = false;
	private String title = "";
	
	public FrequencyAnalyst(String filepath) {
		readDocument(filepath);
	}
	
	public FrequencyAnalyst() {}
	
	public void clear() {
		wordCatalog.clear();
		activeData = false;
		title = "";
	}
	
	private String stem(String word) throws Throwable {
		/*Class stemClass = Class.forName("org.tartarus.snowball.ext. Frequency Analysis Stemmer");
		SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
		
		stemmer.setCurrent(word);
		stemmer.stem();
		
		return stemmer.getCurrent();*/ return word;
	}
	
	public void clearAndReadDocument(String filepath) {
		clear();
		readDocument(filepath);
	}
	
	public void readDocument(String filepath) {
		try {
			Scanner scan = new Scanner(new File(filepath));
			
			boolean titleAscertained = false;
			String word = "";
			
			while (scan.hasNext()) {
				
				if (!titleAscertained) {
					while (!word.contains("***")) {
						word = scan.nextLine();
					}
					
					word = word.toLowerCase();
					word = word.replace("start of this project gutenberg ebook", "");
					word = word.replace("***", "");
					word = word.trim();
					
					title = word;
					titleAscertained = true;
				}
				
				word = scan.next();
								
				word = word.toLowerCase();
				word = word.replaceAll("[^a-zA-Z]", "");
				word = word.trim();
				
				try {
					word = stem(word);
				}
				catch (Throwable e) {
					System.out.println("Error in stemming algorithm.");
					return;
				}
				
				if (wordCatalog.containsKey(word)) {
					int value = wordCatalog.get(word) + 1;
					wordCatalog.put(word, value);
				}
				else {
					wordCatalog.put(word, 1);
				}
			}
			activeData = true;
		}
		catch (FileNotFoundException e) {
			System.out.println("Failed to open file: " + filepath);
		}
	}
	
	public void setTitle(String title_) {
		title = title_;
	}
	
	public float mean() {
		if (activeData) {
			int numWords = 0;
			float total = 0;
			
			Set<String> words = wordCatalog.keySet();
			
			for (String word : words) {
				total += wordCatalog.get(word);
				numWords++;
			}
			
			return total / numWords;
		}
		else {
			throw new RuntimeException("No valid data to analyze.");
		}
	}
	
	public int frequency(String word) {
		if (activeData) {
			if (contains(word))
				return wordCatalog.get(word);
			else
				return 0;
		}
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public boolean contains(String word) {
		if (activeData) {
			return (wordCatalog.get(word) != null);
		}
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public int numWords() {
		if (activeData) return wordCatalog.size();
		
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public int min() {
		if (activeData) {
			int min = Integer.MAX_VALUE;
			
			Set<String> words = wordCatalog.keySet();
			for (String word : words) {
				if (wordCatalog.get(word) < min) {
					min = wordCatalog.get(word);
				}
			}
			
			return min;
		}
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public int max() {
		if (activeData) {
			int max = Integer.MIN_VALUE;
			
			Set<String> words = wordCatalog.keySet();
			for (String word : words) {
				if (wordCatalog.get(word) > max) {
					max = wordCatalog.get(word);
				}
			}
			
			return max;
		}
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public float sd() {
		if (activeData) {
			int[] frequencies = new int[wordCatalog.size()];
			Set<String> words = wordCatalog.keySet();
			int counter = 0;
			for (String word : words) {
				frequencies[counter] = wordCatalog.get(word);
				counter++;
			}
			
			float mean = mean();
			
			float sum = 0;
			for (int i = 0; i < counter; ++i) {
				sum += Math.pow(frequencies[i] - mean, 2);
			}
			double omega = Math.sqrt(sum / counter);
			
			return (float) omega;
		}
		else throw new RuntimeException("No valid data to analyze.");
	}
	
	public boolean writeToFile(String directory, String fname) {
		if (activeData) {
			try {				
				PrintWriter writer = new PrintWriter(directory + "/" + fname);
				Set<String> words = wordCatalog.keySet();
				
				JSONObject freqAn = new JSONObject();
				
				JSONObject word = new JSONObject();
				JSONArray wordLib = new JSONArray();
				ArrayList<JSONObject> wordVector = new ArrayList<JSONObject>();
				
				for (String w : words) {
					if (w.trim().length() > 0) {
						wordVector.add(new JSONObject());
						
						wordVector.get(wordVector.size() -1).put("word", w);
						wordVector.get(wordVector.size() - 1).put("frequency",  wordCatalog.get(w));
						wordVector.get(wordVector.size() - 1).put("difficulty_level", "TBD");
						
						wordLib.add(wordVector.get(wordVector.size() - 1));
					}
				}
				freqAn.put("words", wordLib);
				
				freqAn.put("total_num_words", numWords());
				freqAn.put("standard_deviation", sd());
				freqAn.put("mean_word_frequency", mean());
				freqAn.put("title", title);
				freqAn.put("text_id", fname.replaceAll(".json", ""));
				
				writer.print(freqAn.toJSONString());
				writer.close();
				return true;
			}
			catch (FileNotFoundException fnfe) {
				return false;
			}
			catch (SecurityException se) {
				return false;
			}
		}
	else throw new RuntimeException("No valid data to analyze.");
	}
	
	public static void merge(FrequencyAnalyst recipient, FrequencyAnalyst merger) {
		Set<String> mergerSet = merger.wordCatalog.keySet();
		
		for (String word : mergerSet) {
			if (recipient.wordCatalog.containsKey(word)) {
				int value = recipient.frequency(word) + merger.frequency(word);
				recipient.wordCatalog.put(word, value);
			}
			else {
				recipient.wordCatalog.put(word, merger.frequency(word));
			}
		}
		
		if (!recipient.activeData && merger.activeData) {
			recipient.activeData = true;
		}
	}
}

	
