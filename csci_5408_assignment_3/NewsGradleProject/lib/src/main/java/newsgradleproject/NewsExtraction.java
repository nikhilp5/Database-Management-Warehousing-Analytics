package newsgradleproject;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class NewsExtraction {
	public static void main(String args[]) {
		try {
			ArrayList<String> keywords=new ArrayList<String>(Arrays.asList("Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"));
			
			for(String keyword:keywords) {
				URL url = new URL("https://newsapi.org/v2/everything?q="+keyword+"&apiKey=611b7349488a47fa84ed91bdfc26444c");
				String inline = "";
			    Scanner scanner = new Scanner(url.openStream());
			  
			    while (scanner.hasNext()) {
			       inline += scanner.nextLine();
			    }	 
			    scanner.close();

			    FileWriter fileWriter = new FileWriter(keyword+".txt");
			    fileWriter.write(inline);
			    fileWriter.close();
			    System.out.println(keyword+".txt created");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
