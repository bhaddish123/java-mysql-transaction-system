import java.sql.*;


 // Class handles all DB connection logic so that Walkthrough.java
 //* does NOT talk to MySQL directly.
public class Trans {

    private Connection conn;

    // --------------------------------------------
    // Function to connect TO DATABASE
    public boolean connect() {
        try {
            if (conn != null && !conn.isClosed()) {
                return true;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Schema used in mysql
            String url = "jdbc:mysql://localhost:3306/final_project";

            conn = DriverManager.getConnection(url, "root", "password");

            System.out.println("✓ [Trans] Connected to MySQL.");
            return true;

        } catch (Exception e) {
            System.out.println("✗ [Trans] Connection failed.");
            e.printStackTrace();
            return false;
        }
    }
    // Disconnnect function
    // --------------------------------------------
    public boolean disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            System.out.println("✓ [Trans] Disconnected.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Statements to:(CREATE TABLE, INSERT, DELETE, UPDATE, DROP)
    // --------------------------------------------
    public boolean executeUpdate(String sql) throws SQLException {
        connect();
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
        disconnect();
        return true;
    }

  
    // SELECT STATEMENTS function
    // --------------------------------------------
    public ResultSet executeQuery(String sql) throws SQLException {
        connect();
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        // close statement automatically when caller closes the ResultSet
        stmt.closeOnCompletion();
        return stmt.executeQuery(sql);
    }

    // PREPARED INSERT WITH STRING ARRAY
    // --------------------------------------------
    public boolean executePreparedInsert(String sql, String[] values) throws SQLException {
        connect();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            ps.executeUpdate();
        }
        disconnect();
        return true;
    }

    // EXPOSE CONNECTION FOR METADATA ONLY function
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) connect();
        } catch (SQLException e) {
            connect();
        }
        return conn;
    }

   
    // Main function 
    public static void main(String[] args) {
        Walkthrough w = new Walkthrough();
        w.run();
    }
}
