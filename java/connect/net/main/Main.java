package net.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.IOException;

public class Main{

    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";

    
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(DB_URL);
                
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scn = new Scanner(System.in);
        Integer input = null;

        connect();
        Populate.createDB();
        System.out.println("            Welcome to Stars R Us             ");
        System.out.println("----------------------------------------------");
        System.out.println("Current date: " + interfDB.getCurrentDate());
        System.out.println(" ");

        System.out.println("Would you like to Login as:");
        System.out.print("1. Customer 2.Manager ? \n");

        do {
            System.out.print("Please enter your choice: ");

            String s = scn.nextLine();

            try {
                input = Integer.parseInt(s);

            } catch (NumberFormatException e) {
                System.out.println("ERROR: " + s + " is not a number input");
            }

        } while (input == null);

        switch (input) {
            case 1:      
                System.out.println("\nProceeding to Customer log in... ");
                Trader.CustomerLogin();                    
                break;

            case 2:
                System.out.println("\nProceeding to Manager log in... ");
                Manager.ManagerLogin();
                break;

            default:
                System.out.println("Not a valid entry.");
                System.out.println("Closing the program.");
                break;
        }
        scn.close();
    }
    
}