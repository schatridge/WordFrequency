import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Main {

	public static void main(String[] args) {		
		System.out.println("Beginning frequency analysis...\n");
		System.out.print("Opening input directory " + args[0] + " ...");
		String filepath = args[0];
		System.out.println(" Done!");
		System.out.print("Opening output directory " + args[1] + " ...");
		String outputPath = args[1];
		System.out.println(" Done!\n");
		
		FrequencyAnalyst analyzer = new FrequencyAnalyst();
		FrequencyAnalyst composite = new FrequencyAnalyst();
		
		System.out.print("Reading all documents and computing word difficulty based on frequnecy...");
		composite.readAllDocuments(filepath);
		
		Map<String, Double> wordDifficulties = composite.wordDifficulties();
		System.out.println(" Done!\n");
		
		System.out.print("Reading in individual files, analyzing, and writing to JSON files... ");
		File dir = new File(filepath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.getName().contains(".txt")) {
					analyzer.clearAndReadDocument(filepath + "/" + child.getName());
					
					String fname = child.getName().replaceAll(".txt", ".json");
				
					analyzer.writeToFile(outputPath, fname, wordDifficulties);
					
					//FrequencyAnalyst.merge(composite, analyzer);
		        	}
			}
			
			composite.setTitle("all_texts");
			composite.writeToFile(outputPath, "allTexts.json", wordDifficulties);
			
			System.out.println("Done!");
			System.out.println("Program terminated normally.\n");
		}
		else
			System.out.println("Failure");
	}

}
