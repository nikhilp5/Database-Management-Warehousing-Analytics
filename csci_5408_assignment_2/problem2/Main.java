import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
	public static void main(String args[])  {
		try {
			Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
			Transaction1 t1=new Transaction1(connection);
			Transaction2 t2=new Transaction2(connection);
			t1.start();
			t1.join();
			t2.start();
			t2.join();
			connection.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
