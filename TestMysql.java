import java.sql.*;

public class TestMysql {
public static void main(String[] args) {
// Database login and info
String url = "jdbc:mysql://localhost:3306/final_project";
String username = "root"; // Change to your MySQL username
String password = "password"; // Change to your MySQL password
Connection conn = null;
try {
// Load the MySQL JDBC driver
Class.forName("com.mysql.cj.jdbc.Driver");
// Build connection
conn = DriverManager.getConnection(url, username, password);
if (conn != null) {
System.out.println("Connected to MySQL database successfully!");
System.out.println("Database: " + conn.getCatalog());
}
} catch (ClassNotFoundException e) {
System.out.println("MySQL JDBC Driver not found!");
e.printStackTrace();
} catch (SQLException e) {
System.out.println("Connection failed!");
e.printStackTrace();
} finally {
try {
if (conn != null && !conn.isClosed()) {
conn.close();
System.out.println("Connection closed.");
}
} catch (SQLException e) {
e.printStackTrace();
}
}
}
}