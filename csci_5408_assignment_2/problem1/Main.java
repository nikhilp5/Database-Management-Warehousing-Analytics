import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.DriverManager;

public class Main {
	public static void main(String args[])  {
		Connection remoteConnection = null;
		Connection localConnection = null;
		try {
			File gdc=new File("GDC.txt");
			Scanner sc=new Scanner(gdc);
			while(sc.hasNextLine()) {
				System.out.println(sc.nextLine());
			}
			System.out.println();
			sc.close();
			localConnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/a2_distnk561034", "root", "password");
			remoteConnection= DriverManager.getConnection("jdbc:mysql://35.203.41.177:3306/A2_distnk561034", "root", "passsword");
			Statement localStatement=localConnection.createStatement();
			Statement remoteStatement=remoteConnection.createStatement();
			localStatement.executeUpdate("SET profiling=1");
			remoteStatement.executeUpdate("SET profiling=1");
			localConnection.setAutoCommit(false);
			remoteConnection.setAutoCommit(false);
			ResultSet localResult=localStatement.executeQuery("select * from events;");
			while(localResult.next()) {
				System.out.println(localResult.getString(1)+" "+localResult.getString(2)+" "+localResult.getString(3)+" "+localResult.getString(4));
			}
			ResultSet remoteResult=remoteStatement.executeQuery("select * from park;");
			while(remoteResult.next()) {
				System.out.println(remoteResult.getString(1)+" "+remoteResult.getString(2)+" "+remoteResult.getString(3)+" "+remoteResult.getString(4)+" "+remoteResult.getString(5));
			}
			System.out.println();
			localStatement.executeUpdate("update events set name='Diwali Event' where event_id=1;");
			remoteStatement.executeUpdate("update park set name='Park Name' where park_id=1;");
			localConnection.commit();
			remoteConnection.commit();
			ResultSet localProfileResult=localStatement.executeQuery("SHOW PROFILES");
			ResultSet remoteProfileResult=remoteStatement.executeQuery("SHOW PROFILES");
			System.out.println("Local database:-");
			while(localProfileResult.next()) {
				System.out.println("Query duration is "+localProfileResult.getString(2)+" ms for query "+localProfileResult.getString(3));
			}
			System.out.println("Remote database:-");
			while(remoteProfileResult.next()) {
				System.out.println("Query duration is "+remoteProfileResult.getString(2)+" ms for query "+remoteProfileResult.getString(3));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				localConnection.close();
				remoteConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
