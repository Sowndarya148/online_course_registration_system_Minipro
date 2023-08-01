package course_registration;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coursereg";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    static Connection conn;
    public static Connection getConnection() throws SQLException {
    	
    	try {
    		Class.forName(JDBC_DRIVER);
    		conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		System.out.println(e.getMessage());
    	}
    	return conn;
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
