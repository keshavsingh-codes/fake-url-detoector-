package com.fakeurldetector.dao;

/**
 * URLDao.java
 *
 * This is the DAO (Data Access Object) class.
 * DAO Pattern separates database operations from business logic.
 *
 * Why DAO Pattern?
 * - Centralizes all database operations in one place
 * - Makes code easier to maintain and test
 * - If database changes, only DAO needs modification
 * - Follows Single Responsibility Principle
 *
 * This DAO handles:
 * - Inserting new URL scan results into database
 * - Retrieving all scan history from database
 */

import com.fakeurldetector.util.DBConnection;  // Our database connection utility
import com.fakeurldetector.model.URLLog;        // Standalone Model class for database records

import java.sql.Connection;           // Database connection interface
import java.sql.PreparedStatement;    // Pre-compiled SQL statement (safer than Statement)
import java.sql.ResultSet;            // Holds results from SELECT queries
import java.sql.SQLException;         // Handles SQL exceptions
import java.util.ArrayList;           // Dynamic array for storing logs
import java.util.List;                // Interface for lists

public class URLDao {

    /**
     * Inserts a scanned URL result into the database.
     *
     * @param url    The URL that was scanned
     * @param score  The calculated risk score
     * @param result The classification result (SAFE/SUSPICIOUS/DANGEROUS)
     *
     * Why use PreparedStatement instead of Statement?
     * ------------------------------------------------
     * 1. SQL Injection Protection: User input is treated as data, not SQL code
     * 2. Performance: SQL is pre-compiled, faster for repeated executions
     * 3. Type Safety: Methods like setString(), setInt() ensure correct data types
     * 4. Readability: Clear separation of SQL logic and data values
     *
     * Try-with-resources (Java 7+):
     * - Automatically closes Connection and PreparedStatement
     * - No need for manual close() in finally block
     * - Prevents resource leaks
     */
    public static void insertURL(String url, int score, String result) {
        // SQL INSERT query with placeholders (?)
        // ? = parameter placeholder that will be replaced safely
        String sql = "INSERT INTO url_logs (url, score, result) VALUES (?, ?, ?)";

        // Try-with-resources: automatically closes resources after use
        try (Connection conn = DBConnection.getConnection();          // Step 1: Get DB connection
             PreparedStatement ps = conn.prepareStatement(sql)) {      // Step 2: Prepare SQL

            // Step 3: Set parameter values (index starts at 1, not 0)
            ps.setString(1, url);      // Replace 1st ? with URL string
            ps.setInt(2, score);       // Replace 2nd ? with score integer
            ps.setString(3, result);   // Replace 3rd ? with result string

            // Step 4: Execute the INSERT statement
            // executeUpdate() is used for INSERT, UPDATE, DELETE
            // It returns the number of rows affected
            ps.executeUpdate();

        } catch (SQLException e) {
            // Print error details if database operation fails
            // In production, consider using a logging framework
            e.printStackTrace();
        }
        // Resources (conn, ps) are automatically closed here by try-with-resources
    }

    /**
     * Retrieves all scanned URL records from the database.
     * Results are ordered by timestamp in descending order (newest first).
     *
     * @return List<URLLog> List of all scan history records
     *
     * Steps:
     * 1. Create empty list to store results
     * 2. Execute SELECT query
     * 3. Iterate through ResultSet
     * 4. Create URLLog object for each row
     * 5. Add to list and return
     */
    public static List<URLLog> getAllLogs() {
        // Create empty list to hold URL scan records
        List<URLLog> logs = new ArrayList<>();

        // SQL SELECT query - ORDER BY checked_at DESC shows newest first
        String sql = "SELECT * FROM url_logs ORDER BY checked_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {  // executeQuery() for SELECT

            // Iterate through all rows in the result set
            // rs.next() moves to next row, returns false when no more rows
            while (rs.next()) {
                // Create a new URLLog object from current row data
                // Using the standalone URLLog model class (not inner class anymore)
                URLLog log = new URLLog(
                    rs.getInt("id"),              // Get integer from 'id' column
                    rs.getString("url"),          // Get string from 'url' column
                    rs.getInt("score"),           // Get integer from 'score' column
                    rs.getString("result"),       // Get string from 'result' column
                    rs.getTimestamp("checked_at") // Get timestamp from 'checked_at' column
                );

                // Add this record to our list
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the complete list of scan history
        return logs;
    }
}

