import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
	public static void main(String args[]) {
		try {
			ArrayList<String> keywords=new ArrayList<String>(Arrays.asList("Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"));
			for(String word:keywords) {
				int count=0;
				for(String keyword:keywords) {
					Path filePath= Paths.get(keyword+".txt");
					String content = Files.readAllLines(filePath,Charset.forName("ISO-8859-1")).get(0);
					count+=countOccurrences(content,word);
				}
				System.out.println(word+" "+count);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	static int countOccurrences(String str, String word)
	{
		int count = 0;
		Pattern p = Pattern.compile(word,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		while (m.find()) {
			count++;
		}
		return count;
	}
}
