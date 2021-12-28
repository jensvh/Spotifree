package me.jensvh.spotifree.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqLite {
    
    private static Connection conn = null;
    
    public static void connectToCookieDatabase(String path) {
        try {
            String url = "jdbc:sqlite:" + path;
            conn = DriverManager.getConnection(url);
            
        } catch (SQLException e) {
            System.out.println();
            e.printStackTrace();
        }
    }
    
    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println();
            e.printStackTrace();
        }
    }
    
    public static ResultSet query(String query) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            return rs;
        } catch (SQLException e) {
            System.out.println();
            e.printStackTrace();
        }
        return null;
    }

}
