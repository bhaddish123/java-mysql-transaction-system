#Java MySQL Transaction System

A Java CLI application demonstrating JDBC-based MYSQL transactions,dynamic table creation,
prepared statements, and safe database interaction through a clean abstraction layer.

#Features
	•	JDBC connection handling with MySQL
	•	Dynamic table creation and deletion
	•	Insert and query operations using prepared statements
	•	Transaction-style workflow abstraction
	•	Command-line interactive walkthrough
	•	Automated grading/test runner for database operations

#Project Structure
	•	Trans.java – Core database abstraction layer (connect, create, insert, query, drop)
	•	Walkthrough.java – CLI interface for interactive database operations
	•	TestMysql.java – Standalone MySQL connection validation
	•	Grading.java – Automated test harness for database functionality

#Technologies Used
	•	Java
	•	MySQL
	•	JDBC
	•	SQL
	•	Prepared Statements
	•	Command Line Interface (CLI)

#How to Run

Prerequisites
	•	Java 8+
	•	MySQL installed and running
	•	MySQL JDBC Connector

#Compile
javac *.java

#Run Interactive CLI
java Walkthrough

#Run Automated Tests
java Grading

#Notes
	•	Database credentials are configurable inside TestMysql.java and Trans.java
	•	Designed to emphasize clean separation between application logic and database access
	•	Focused on correctness, safety, and clarity rather than frameworks

#Purpose

This project demonstrates practical backend development skills, including:
	•	Database connectivity
	•	Transaction-style logic
	•	Defensive SQL programming
	•	Java application design
