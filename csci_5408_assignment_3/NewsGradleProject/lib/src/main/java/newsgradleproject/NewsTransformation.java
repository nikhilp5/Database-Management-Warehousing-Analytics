package newsgradleproject;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class NewsTransformation {
	public static void main(String[] args){
		try {
			ArrayList<String> keywords=new ArrayList<String>(Arrays.asList("Canada", "Halifax", "hockey", "hurricane", "electricity", "house", "inflation"));
			MongoClient mongoClient=new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoDatabase database=mongoClient.getDatabase("news");
			for(String keyword:keywords) {
				Path filePath= Paths.get(keyword+".txt");
				String content = Files.readString(filePath,Charset.forName("ISO-8859-1"));
				content=content.replaceAll("<.*?>", "");//https://stackoverflow.com/questions/240546/remove-html-tags-from-a-string
				JSONObject obj = new JSONObject(content);
				JSONArray jsonArray=new JSONArray(obj.get("articles").toString());
				JSONArray ModifiedJsonArray=new JSONArray();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject tempObj=jsonArray.getJSONObject(i);
					String tempTitle=tempObj.getString("title");
					String tempDesc=tempObj.getString("description");
					String tempContent=tempObj.getString("content");
					tempContent=tempContent.replaceAll("[^a-zA-Z0-9]", " ");
					tempTitle=tempTitle.replaceAll("[^a-zA-Z0-9]", " ");
					tempDesc=tempDesc.replaceAll("[^a-zA-Z0-9]", " ");
					tempObj.put("content",tempContent);
					tempObj.put("title",tempTitle);
					tempObj.put("description",tempDesc);
					ModifiedJsonArray.put(tempObj);
				}
				obj.put("articles", ModifiedJsonArray);
				content=obj.toString();
				content=content.replaceAll("\\s*\\\"url\\\" *: *(\\\"(.*?)\\\"(,|\\s|)|\\s*\\{(.*?)\\}(,|\\s|))", "");//https://stackoverflow.com/questions/46160681/regex-remove-json-property
				content=content.replaceAll("\\s*\\\"urlToImage\\\" *: *(\\\"(.*?)\\\"(,|\\s|)|\\s*\\{(.*?)\\}(,|\\s|))", "");//https://stackoverflow.com/questions/46160681/regex-remove-json-property
				content=content.replaceAll("\\\\r|\\\\n|\\\\t", "");
				content=content.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", " ");
				content=content.replaceAll("(\\\"author\\\":(\\\"http.*?\\\"|null|\\\"null\\\"|\\\"\\\"),)", " ");
				content=content.replaceAll("(,\\\"id\\\":(null|\\\"null\\\"|\\\"\\\"))", " ");
				BasicDBObject o = BasicDBObject.parse(content);
				database.createCollection(keyword);
				MongoCollection<Document> coll = database.getCollection(keyword);
				coll.insertOne(new Document(o.toMap()));
				System.out.println(keyword+" collection created");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}