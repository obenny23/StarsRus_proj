package net.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import 

import javax.swing.text.Style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Connect {
     /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

        connect();
        populate();

        System.out.println("            Welcome to Stars R Us             ");
        System.out.println("----------------------------------------------");
        System.out.println(" ");
        System.out.println("Are you signed up? (y/n)");

        String resp = reader.readLine();

        System.out.println(resp);


        if (resp == "y" || resp == "Y") {
            System.out.println("Would you like to Login as:");
            System.out.println("1. Customer 2.Manager 3.Admin");

            String name = reader.readLine();

            switch (name) {
                case "1":
                    
                    break;

                case "2":
                
                    break;

                case "3":
                
                    break;
        
                default:
                    break;
            }
        }

        else if (resp == "n" || resp == "N") {
            System.out.println("Would you like to sign up as a new customer?");
            
            String r = reader.readLine();

            if (r == "y" || r == "Y") {
                createNew();
            }
        }
    }


    private static void createNew() {
        System.out.println("no :p ");
    }
}