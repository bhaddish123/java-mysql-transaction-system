import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Walkthrough {

    private Trans t;          
    private Scanner scanner;   

    public Walkthrough() {
        scanner = new Scanner(System.in);
        t = new Trans();      // connecting internally
        t.connect();
    }

    private void printTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[Timestamp: " + sdf.format(new Date()) + "]");
    }

    public void run() {
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println(" DATABASE OPERATIONS MENU ");
            System.out.println("========================================");
            System.out.println("1. Create a new table");
            System.out.println("2. Insert data into a table");
            System.out.println("3. Query data from a table");
            System.out.println("4. Display all tables");
            System.out.println("5. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1: createTable(); break;
                case 2: insertData(); break;
                case 3: queryData(); break;
                case 4: displayAllTables(); break;
                case 5:
                    running = false;
                    System.out.println("Exiting…");
                    t.disconnect();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Function to create table ---------------
    public void createTable() {
        try {
            System.out.println("\n--- CREATE NEW TABLE ---");
            printTimestamp();

            System.out.print("Enter table name: ");
            String tableName = scanner.nextLine();

            System.out.print("Enter number of columns: ");
            int numColumns = scanner.nextInt();
            scanner.nextLine();

            StringBuilder def = new StringBuilder();
            for (int i = 0; i < numColumns; i++) {

                System.out.println("\nColumn " + (i + 1));
                System.out.print("Name: ");
                String columnName = scanner.nextLine();

                System.out.print("Data type (INT, VARCHAR, DATE, etc.): ");
                String type = scanner.nextLine().toUpperCase();

                if (type.equals("VARCHAR")) {
                    System.out.print("Size (e.g., 50): ");
                    int size = scanner.nextInt();
                    scanner.nextLine();
                    type = "VARCHAR(" + size + ")";
                }

                def.append(columnName).append(" ").append(type);
                if (i < numColumns - 1) def.append(", ");
            }

            t.executeUpdate("CREATE TABLE " + tableName + " (" + def.toString() + ")");

            System.out.println("\n✓ Table created successfully.");
            printTimestamp();
        } catch (SQLException e) {
            System.out.println("Error creating table.");
            e.printStackTrace();
        }
    }

    // Data inserting function -------------------------------------------------------
    public void insertData() {
        try {
            System.out.println("\n--- INSERT DATA ---");
            printTimestamp();

            System.out.print("Enter table name: ");
            String tableName = scanner.nextLine();

            // Get through trans
            DatabaseMetaData meta = t.getConnection().getMetaData();

            ResultSet cols = meta.getColumns(null, null, tableName, null);

            List<String> names = new ArrayList<>();
            List<String> types = new ArrayList<>();

            System.out.println("\nColumns:");
            while (cols.next()) {
                names.add(cols.getString("COLUMN_NAME"));
                types.add(cols.getString("TYPE_NAME"));
                System.out.println(" - " + cols.getString("COLUMN_NAME") + " (" + cols.getString("TYPE_NAME") + ")");
            }
            cols.close();

            if (names.isEmpty()) {
                System.out.println("Table not found.");
                return;
            }

            System.out.print("\nHow many rows to insert? ");
            int rows = scanner.nextInt();
            scanner.nextLine();

            StringBuilder colList = new StringBuilder(String.join(", ", names));
            StringBuilder ph = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                ph.append("?");
                if (i < names.size() - 1) ph.append(", ");
            }

            String sql = "INSERT INTO " + tableName + " (" + colList + ") VALUES (" + ph + ")";

            for (int r = 0; r < rows; r++) {
                System.out.println("\nRow " + (r + 1));

                String[] vals = new String[names.size()];
                for (int i = 0; i < names.size(); i++) {
                    System.out.print("Enter value for " + names.get(i) + ": ");
                    vals[i] = scanner.nextLine();
                }

                t.executePreparedInsert(sql, vals);
                System.out.println("✓ Row inserted.");
            }
            System.out.println("\nAll rows inserted.");
        } catch (Exception e) {
            System.out.println("Insert error.");
            e.printStackTrace();
        }
    }

    // QUERY getting function--------------------------------------------------------------
    public void queryData() {
        try {
            System.out.println("\n--- QUERY DATA ---");
            printTimestamp();

            System.out.print("Enter table name: ");
            String tableName = scanner.nextLine();

            System.out.print("Enter column (* for all): ");
            String column = scanner.nextLine();

            System.out.print("WHERE clause? (yes/no): ");
            String yn = scanner.nextLine();

            String where = "";
            if (yn.equalsIgnoreCase("yes")) {
                System.out.print("Enter WHERE clause: ");
                where = " WHERE " + scanner.nextLine();
            }

            String sql = "SELECT " + column + " FROM " + tableName + where;
            ResultSet rs = t.executeQuery(sql);

            ResultSetMetaData m = rs.getMetaData();
            int count = m.getColumnCount();

            System.out.println("\nRESULTS:");
            for (int i = 1; i <= count; i++)
                System.out.printf("%-20s", m.getColumnName(i));
            System.out.println();
            System.out.println("=".repeat(count * 20));

            int rc = 0;
            while (rs.next()) {
                for (int i = 1; i <= count; i++)
                    System.out.printf("%-20s", rs.getString(i));
                System.out.println();
                rc++;
            }
            System.out.println("\nTotal rows: " + rc);

            rs.close();
        } catch (Exception e) {
            System.out.println("Query error.");
            e.printStackTrace();
        }
    }

    // DISPLAY TABLES function -----------------------------------------------------
    public void displayAllTables() {
        try {
            System.out.println("\n--- ALL TABLES ---");
            printTimestamp();

            DatabaseMetaData meta = t.getConnection().getMetaData();
            ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});

            int i = 0;
            while (rs.next()) {
                System.out.println(" " + (++i) + ". " + rs.getString("TABLE_NAME"));
            }

            if (i == 0) System.out.println("No tables found.");

            rs.close();
        } catch (Exception e) {
            System.out.println("Display error.");
            e.printStackTrace();
        }
    }

    // (GRADING.JAVA)
    // -------------------------------------------------------------
    public boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = t.getConnection().getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    public void executeDrop(String tableName) throws SQLException {
        t.executeUpdate("DROP TABLE IF EXISTS " + tableName);
    }

    public void executeCreate(String tableName, String columnsDefinition) throws SQLException {
        t.executeUpdate("CREATE TABLE " + tableName + " (" + columnsDefinition + ")");
    }

    public void executeInsert(String tableName, String columnNames, String[] values) throws SQLException {
        String[] columns = columnNames.split("\\s*,\\s*");
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Column count and values length do not match.");
        }

        String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));
        String joinedColumns = String.join(", ", columns);
        String sql = "INSERT INTO " + tableName + " (" + joinedColumns + ") VALUES (" + placeholders + ")";

        t.executePreparedInsert(sql, values);
    }

    public String[] executeQuery(String tableName, String column, String whereClause) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT ").append(column).append(" FROM ").append(tableName);
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            sql.append(" WHERE ").append(whereClause);
        }

        List<String> results = new ArrayList<>();
        try (ResultSet rs = t.executeQuery(sql.toString())) {
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } finally {
            t.disconnect();
        }

        return results.toArray(new String[0]);
    }

    public void closeConnection() {
        t.disconnect();
    }
//Main function
    public static void main(String[] args) {
        Walkthrough app = new Walkthrough();
        app.run();
    }
}
