import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction1 extends Thread{
	Connection connection;
	Statement statement;
	public Transaction1(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void run() {
		acquireLock(connection);
		operation();
		releaseLock();
	}
	
	public void acquireLock(Connection connection) {
		try {
			statement=connection.createStatement();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			statement.executeUpdate("lock table department write;");
			System.out.println("Transaction 1 acquired lock");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void operation() {
		try {
			connection.setAutoCommit(false);
			statement.executeUpdate("update department set department_name='T1 updated department' where department_id=1");
			System.out.println("Transaction 1-Executed update department set department_name='T1 updated department' where department_id=1");
			statement.executeUpdate("insert into department values(3,'T1 new department');");
			System.out.println("Transaction 1-Executed insert into department values(3,'T1 new department')");
			ResultSet result=statement.executeQuery("select * from department;"); 
			System.out.println("Transaction 1-Executed select * from department");
			while(result.next()) {
				System.out.println(result.getString(1)+" "+result.getString(2));
			}
			System.out.println("Transaction 1 done");
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void releaseLock() {
		try {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			statement.executeUpdate("unlock tables;");
			System.out.println("Transaction 1 released lock");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
