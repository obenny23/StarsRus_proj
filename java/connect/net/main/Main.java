package net.main;

import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.text.Style;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import net.main.Populate;


public class Main{
     /**
     * Connect to a sample database
     */

    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";

    private Connection conn;
    private Statement stmt;
    private PreparedStatement prepstmt;
    private ResultSet rs;
    private static Console console;


    
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(DB_URL);
            
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
        Scanner scn = new Scanner(System.in);
        Populate tb = new Populate();

        boolean loginFail = true;

        connect();
        tb.populate();

        System.out.println("            Welcome to Stars R Us             ");
        System.out.println("----------------------------------------------");
        // System.out.println("Current date: " + getCurrentDate());
        System.out.println(" ");
        System.out.print("Are you signed up? (yes/no)  ");

        while(loginFail){
            String resp = scn.nextLine();

            if (resp.toLowerCase().equals("yes")) {
                System.out.println("Would you like to Login as:");
                System.out.print("1. Customer 2.Manager 3.Admin ? ");

                int choice = scn.nextInt();

                switch (choice) {
                    case 1: 
                        System.out.println("Proceeding to Customer log in... ");
                        CustomerLogin();                    
                        break;

                    case 2:
                        System.out.println("Proceeding to Manager log in... ");
                        ManagerLogin();
                        break;

                    case 3:
                        if (AdminCheck()){
                            System.out.println("Hello creator... ");
                        } else {
                            System.out.println("Incorrect Admin credentials.");
                        }
                        break;
            
                    default:
                        System.out.println("Error: incorrect entry");
                        System.out.println("Redirecting back to opening prompt.");
                        break;
                }
            }

            else if (resp.toLowerCase().equals("no")) {
                System.out.print("\nWould you like to sign up as a new customer? (yes/no)  ");
                
                String r = scn.next();

                if (r.toLowerCase().equals("yes")) {
                    createNewAccount();
                }
                else {
                    System.out.println("Have a nice day! (: ");
                }
            }
            else {
                System.out.println("Please enter either yes or no,\n");
                System.out.println("Are you Signed up?");
            }
        }
        scn.close();
    }

    
    private static boolean AdminCheck() {
        Scanner scn = new Scanner(System.in);
        return false;

    }

    private static void ManagerLogin() {
        Scanner scn = new Scanner(System.in);
        int attempts = 3;

		System.out.println("\n         Manager Login           ");
        System.out.println("---------------------------------");


        System.out.print("Enter username: ");
		String managerUsername = scn.nextLine();
		System.out.print("password: ");
		String managerPassword = scn.nextLine();

        boolean valid = isValid(true, managerUsername, managerPassword);

        while (!valid) {
            System.out.println("Incorrect Manager credentials.");
            System.out.println("Try again. Attempts left: " + (attempts) + "\n");
            System.out.print("Enter username: ");
            managerUsername = scn.nextLine();
            System.out.print("password: ");
            managerPassword = scn.nextLine();
            valid = isValid(true, managerUsername, managerPassword);

            if (!valid) {   attempts--;    } 
            else { valid = true;    }

            if (attempts == 0){
                System.out.println("Too many attempts. Closing program.");
                System.exit(0);
            }
        }

        openManagerInt();

        scn.close();
    }

    private static void openManagerInt() {
    }
    private static boolean isValid(boolean isManager, String managerUsername, String managerPassword) {
        return false;
    }

    private static void CustomerLogin() {
        Scanner scn = new Scanner(System.in);
        int attempts = 4;

		System.out.println("\n         Customer Login           ");
        System.out.println("---------------------------------");



        System.out.print("Enter username: ");
		String customerUsername = scn.nextLine();
		System.out.print("password: ");
		String customerPassword = scn.nextLine();

        boolean valid = isValid(false, customerUsername, customerPassword);

        while (!valid){
            System.out.println("Incorrect username or password.");
            System.out.println("Try again. Attempts left: " + (attempts) + "\n");
            System.out.print("username: ");
            customerUsername = scn.nextLine();
            System.out.print("password: ");
            customerPassword = scn.nextLine();

            if (!valid){    attempts--;     }
            else {  valid = true;   }

            if (attempts == 0){
                System.out.println("Too many attempts. Closing program.");
                System.exit(0);
            }
        }

        openTradersInt();
        scn.close();
    }

    private static void openTradersInt() {
    }


    private static void createNewAccount() throws InterruptedException {

        System.out.println("no :p ");
        Thread.sleep(4000);
        System.out.println("jk (:");

        System.out.println("Would you like to create a Customer or Manager Account");
    }
}