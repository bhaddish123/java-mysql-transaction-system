public class Grading {
   public static void main(String[] args) {
   System.out.println("--- Starting Grading Test ---");
   
   // 1. Initialize Walkthrough to establish connection
   Walkthrough dbOps = new Walkthrough();
   String testTable = "StudentsTest";
   boolean overallSuccess = true;
   
   try {
   // --- Step 1: Cleanup and Existence Check (like Trans.exist/drop) ---
   System.out.println("\n--- Step 1: Cleanup ---");
   if(dbOps.tableExists(testTable)) {
   System.out.println("Table exists. Dropping it for fresh test...");
   dbOps.executeDrop(testTable);
   System.out.println("✓ Drop successful.");
   } else {
   System.out.println("Table does not exist. Proceeding with test.");
   }
   
   // --- Step 2: Test Create (like Trans.create) ---
   System.out.println("\n--- Step 2: Test Create (StudentsTest, id int, name varchar(50)) ---");
   dbOps.executeCreate(testTable, "id INT, name VARCHAR(50)");
   if(dbOps.tableExists(testTable)) {
   System.out.println("✓ Test 2 Passed: Table created successfully.");
   } else {
   System.out.println("✗ Test 2 Failed: Table creation failed.");
   overallSuccess = false;
   }
   // --- Step 3: Test Write (like Trans.write) ---
   System.out.println("\n--- Step 3: Test Write (Insert data) ---");
   String columnNames = "id, name";
   String[] student1 = {"1", "John Doe"};
   String[] student2 = {"2", "Jane Smith"};
   dbOps.executeInsert(testTable, columnNames, student1);
   dbOps.executeInsert(testTable, columnNames, student2);
   System.out.println("✓ Test 3 Passed: Data inserted successfully (2 rows).");
   
   
   // --- Step 4: Test Read (like Trans.read) ---
   System.out.println("\n--- Step 4: Test Read (name where id = 1) ---");
   String[] results = dbOps.executeQuery(testTable, "name", "id = '1'");
   System.out.println("Results found:");
   boolean test4Passed = false;
   if (results.length == 1) {
   System.out.println(results[0]);
   if (results[0].equals("John Doe")) {
   test4Passed = true;
   }
   } else {
   System.out.println("No or too many results found.");
   }
   if(test4Passed) {
   System.out.println("✓ Test 4 Passed: Correct data 'John Doe' retrieved.");
   } else {
   System.out.println("✗ Test 4 Failed: Expected 'John Doe' not found or result count wrong.");
   overallSuccess = false;
   }
   
   
   } catch (java.sql.SQLException e) {
   System.out.println("\n✗ A SQL Error occurred during testing!");
   e.printStackTrace();
   overallSuccess = false;
   } catch (Exception e) {
   System.out.println("\n✗ An unexpected error occurred!");
   e.printStackTrace();
   overallSuccess = false;
   } finally {
   // Final cleanup
   System.out.println("\n--- Final Cleanup ---");
   try {
   if(dbOps.tableExists(testTable)) {
   dbOps.executeDrop(testTable);
   System.out.println("✓ Test table cleaned up successfully.");
   }
   } catch (java.sql.SQLException e) {
   System.out.println("✗ Cleanup failed: " + e.getMessage());
   }
   dbOps.closeConnection();
   }
   
   System.out.println("\n--- Grading Test Complete ---");
   System.out.println("Overall Test Status: " + (overallSuccess ? "SUCCESS" : "FAILURE"));
   }
   }
   