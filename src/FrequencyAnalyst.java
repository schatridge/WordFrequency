
import java.util.*;
import java.io.*;

public class FrequencyAnalyst {
	private Map<String, Integer> wordCatalog = new TreeMap<String, Integer>();
	
	public FrequencyAnalyst(String filepath) {
		readDocument(filepath);
	}
	
	public FrequencyAnalyst() {}
	
	public void clear() {
		wordCatalog.clear();
	}
	
	public void clearAndReadDocument(String filepath) {
		clear();
		readDocument(filepath);
	}
	
	public void readDocument(String filepath) {
		try {
			Scanner scan = new Scanner(new File(filepath));
			
			while (scan.hasNext()) {
				String word = scan.next();
				
				word = word.toLowerCase();
				word = word.replaceAll("[^a-zA-Z]", "");
				word = word.replaceAll("\\s","");
				word = word.replaceAll(" ", "");
				
				//System.out.println(word);
				//if (word.equals("academy")) System.out.println("Test");
				
				if (wordCatalog.containsKey(word)) {
					int value = wordCatalog.get(word) + 1;
					wordCatalog.put(word, value);
				}
				else {
					wordCatalog.put(word, 1);
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Failed to open file.");
		}
	}
	
	public float mean() {
		int numWords = 0;
		float total = 0;
		
		Set<String> words = wordCatalog.keySet();
		
		for (String word : words) {
			total += wordCatalog.get(word);
			numWords++;
		}
		
		return total / numWords;
	}
	
	public int frequency(String word) {
		if (contains(word))
			return wordCatalog.get(word);
		else
			return 0;
	}
	
	public boolean contains(String word) {
		return (wordCatalog.get(word) != null);
	}
	
	public int numWords() {
		return wordCatalog.size();
	}
	
	public int min() {
		int min = Integer.MAX_VALUE;
		
		Set<String> words = wordCatalog.keySet();
		for (String word : words) {
			if (wordCatalog.get(word) < min) {
				min = wordCatalog.get(word);
			}
		}
		
		return min;
	}
	
	public int max() {
		int max = Integer.MIN_VALUE;
		
		Set<String> words = wordCatalog.keySet();
		for (String word : words) {
			if (wordCatalog.get(word) > max) {
				max = wordCatalog.get(word);
			}
		}
		
		return max;
	}
	
	public float sd() {
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
	
	public boolean writeToFile(String directory, String fname) {
		try {
			PrintWriter writer = new PrintWriter(directory + "/" + fname);
			Set<String> words = wordCatalog.keySet();
			
			for (String word : words) {
				writer.println(word + "\t" + wordCatalog.get(word));
			}
			
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
}
