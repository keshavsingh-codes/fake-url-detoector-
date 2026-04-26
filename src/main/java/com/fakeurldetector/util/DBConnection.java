package com.fakeurldetector.util;

/**
 * DBConnection.java
 * 
 * This is a Utility class responsible for establishing and providing
 * database connections using JDBC (Java Database Connectivity).
 * 
 * Why Utility Class?
 * - We use a single utility class to handle all DB connections
 * - This follows the DRY (Don't Repeat Yourself) principle
 * - Any class that needs DB access can simply call getConnection()
 * 
 * JDBC Steps:
 * 1. Load the JDBC Driver (com.mysql.cj.jdbc.Driver)
 * 2. Establish connection using DriverManager.getConnection()
 * 3. Return the Connection object
 */

import java.sql.Connection;      // Interface for database connection
import java.sql.DriverManager;   // Manages JDBC drivers and connections
import java.sql.SQLException;    // Handles SQL-related exceptions

public class DBConnection {
    
    // ============================================================
    // DATABASE CONFIGURATION
    // Change these values according to your MySQL setup
    // ============================================================
    
    /** URL format: jdbc:mysql://host:port/database_name */
    private static final String URL = "jdbc:mysql://localhost:3306/fakeurldb";
    
    /** MySQL username (default is usually 'root') */
    private static final String USER = "root";
    
    /** MySQL password - UPDATE THIS WITH YOUR ACTUAL PASSWORD */
    private static final String PASSWORD = "Keshav@123";
    
    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * @return Connection object if successful
     * @throws SQLException if connection fails or driver not found
     * 
     * How it works:
     * 1. Class.forName() - Loads the MySQL JDBC driver class into memory
     *    This registers the driver with DriverManager automatically
     * 
     * 2. DriverManager.getConnection() - Uses the loaded driver to 
     *    establish an actual network connection to the database
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Step 1: Load the MySQL JDBC Driver
            // For MySQL 8.x, use: com.mysql.cj.jdbc.Driver
            // For older MySQL 5.x, use: com.mysql.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        } catch (ClassNotFoundException e) {
            // This exception occurs if the MySQL JDBC JAR is not in classpath
            // Solution: Add mysql-connector-java-x.x.x.jar to WEB-INF/lib
            throw new SQLException("MySQL JDBC Driver not found. " +
                "Please add mysql-connector-java JAR to WEB-INF/lib", e);
        }
        
        // Step 2: Create and return the database connection
        // This actually connects to MySQL using the provided credentials
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

