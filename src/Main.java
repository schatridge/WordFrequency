import java.io.File;
import java.util.Map;
import java.util.TreeMap;


public class Main {

	public static void main(String[] args) {
		String filepath = args[0];
		FrequencyAnalyst analyzer = new FrequencyAnalyst();
		FrequencyAnalyst composite = new FrequencyAnalyst();
		
		File dir = new File(filepath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.getName().contains(".txt")) {
					analyzer.clearAndReadDocument(filepath + "/" + child.getName());
					System.out.println(analyzer.frequency("fun"));
					
					String fname = child.getName().replaceAll(".txt", ".json");
				
					analyzer.writeToFile(args[1], fname);
					
					FrequencyAnalyst.merge(composite, analyzer);
		        	}
			}
			
			composite.setTitle("all_texts");
			composite.writeToFile(args[1], "allTexts.json");
		}
		else
			System.out.println("Failure");
	}

}
