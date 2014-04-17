import java.io.File;


public class Main {

	public static void main(String[] args) {
		String filepath = "/Users/schatridge/Downloads/books_for_clay";
		FrequencyAnalyst analyzer = new FrequencyAnalyst();
		
		File dir = new File(filepath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.getName().contains(".txt")) {
					analyzer.clearAndReadDocument(filepath + "/" + child.getName());
					System.out.println(analyzer.frequency("fun"));
				
					analyzer.writeToFile("/Users/schatridge/Downloads/books_for_clay/sorted", child.getName());
		        	}
				}
		}
		else
			System.out.println("Failure");
	}

}
