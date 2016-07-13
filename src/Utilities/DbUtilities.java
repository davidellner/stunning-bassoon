/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;


/**
 * Provides methods for: 
 * 1. Retrieving data sets from MySQL databases. 
 * 2. For executing UPDATE, INSERT, DELETE queries 
 * 3. For building tables to populate data grids (JTable)
 * @author Dmitriy Babichenko
 * @version 1.1
 */
public class DbUtilities {

    private static Connection conn = null; // connection object
    private static String hostName; // server address + port number
    private static String dbName; // default database name
    private static String dbUserName; // login name for the database server
    private static String dbPassword; // password for the database server

    /**
     * Default constructor creates a connection to database at the time of instantiation.
     */
    static {
       Properties dbconfig = new Properties();
       String configFilePath = DbUtilities.class.getResource("databaseconfig.properties").getFile();
        try {
            dbconfig.load(new FileInputStream(configFilePath));
            hostName = dbconfig.getProperty("host")+":"+dbconfig.getProperty("port");
            dbName = dbconfig.getProperty("defaultdatabase");
            dbUserName = dbconfig.getProperty("username");
            dbPassword = dbconfig.getProperty("password");
        } catch (IOException ex) {
            
        }
    }

    /**
     * Alternate constructor - use it to connect to any MySQL database
     * @param hostName - server address/name of MySQL database
     * @param dbName - name of the database to connect to
     * @param dbUserName - user name for MySQL database
     * @param dbPassword - password that matches dbUserName for MySQL database
     */
    public DbUtilities(String hostName, String dbName, String dbUserName, String dbPassword) {
        // Set class-level (instance) variables
        this.hostName = hostName;
        this.dbName = dbName;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
        // Create new database connection
        createDbConnection();
    }

    
    /**
     * Creates database connection using credentials stored in class variables.  
     * Connection to database is the most resource-consuming part of the database transaction. 
     * That's why we create a connection once when the object is instantiated and keep it alive through the life of this object.
     * Note that this is a private method and cannot be accessed from outside of this class.
     */
    private static void createDbConnection() {
        try {
            // Build connection string
            String mySqlConn = "jdbc:mysql://" + hostName + "/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword; 
//String mySqlConn = "jdbc:mysql://localhost/nhl?user=dellner&password=mangina";       
// Instantiate the MySQL database connector driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Connect to the database
           
            conn = DriverManager.getConnection(mySqlConn);
            
        } catch (SQLException ex) {
    // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }       catch (ClassNotFoundException ex) {
            
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    
    /**
     * Get SQL result set (data set) based on an SQL query
     * @param sql - SQL SELECT query
     * @return - ResultSet - java.sql.ResultSet object, contains results from SQL query argument
     * @throws SQLException
     */
    public static ResultSet getResultSet(String sql) throws SQLException {
        try {
            if (conn == null) { // Check if connection object already exists
                createDbConnection(); // If does not exist, create new connection
            }
            else if(conn.isClosed()){
                createDbConnection();
            }
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql); // Return ResultSet
        } catch (Exception e) {
            e.printStackTrace(); // debug
            
        }
        return null;
    }

    
    /**
     * Executes INSERT, UPDATE, DELETE queries
     * @param sql - SQL statement - a well-formed INSERT, UPDATE, or DELETE query
     * @return true if execution succeeded, false if failed 
     * @throws edu.pitt.utilities.QueryFailureException 
     */
    public static boolean executeQuery(String sql) {
        try {
            if (conn == null) {
                createDbConnection();
            }
            else if(conn.isClosed()){
                createDbConnection();
            }
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql); // execute query
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // debug
            return false;
        }
        
    }
    
    /**
     * This method converts a ResultSet into a JSON object
     * @param sqlQuery - an SQL query - we need to get a data set from a database 
     *      and convert it to JSON
     * @return JSON object
     * @throws SQLException
     * @throws JSONException 
     */
    
    
    
    /**
     * 
     * @param sqlQuery
     * @param tableID
     * @return
     * @throws SQLException 
     */
    public static String getHtmlDataTable(String sqlQuery, String tableID) throws SQLException {
        ResultSet rs = getResultSet(sqlQuery);
        ResultSetMetaData metaData = rs.getMetaData();
        String tbl = "<table id='" + tableID + "'><tr>";
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            tbl += "<td>" + metaData.getColumnName(column) + "</td>";
        }
        tbl += "</tr>";

        while (rs.next()) {
            tbl += "<tr>";
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tbl += "<td>" + rs.getString(columnIndex) + "</td>";
            }
            tbl += "</tr>";
        }
        return tbl;
    }
    public static void close(){
        closeDbConnection();
    }
    private static void closeDbConnection(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbUtilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
