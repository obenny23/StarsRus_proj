package net.main;

import java.sql.*;


public class Populate {

    public static void populate ()
    {

        Connection connection   = null;

        try {
            String url = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";

            connection = DriverManager.getConnection (url);  

            // try {
            //     Statement dropTable = connection.createStatement ();
            //     dropTable.executeUpdate ("DROP TABLE " + tableName);  
            // }
            // catch (SQLException e) {
            // }

            
            Statement createTable = connection.createStatement ();
            createTable.executeUpdate ("CREATE TABLE Customers "
                + " (tid int(9) UNIQUE, username varchar(255) PRIMARY KEY, "
                + " password varchar(255), cname varchar(100), state char(2), "
                + " phonenumber varchar(11), email varchar(64), ssn varchar(11)) "); 

            createTable.executeUpdate("CREATE TABLE Account("
                + " aid int(100) AUTO_INCREMENT, "
                + " uid varchar(64) NOT NULL, "
                + " PRIMARY KEY(aid, uid), "
                + " FOREIGN KEY(uid) REFERENCES Customers(username)"
                + " ON UPDATE CASCADE ON DELETE CASCADE )");

            createTable.executeUpdate("CREATE TABLE Market_Account( "
                + "aid int(100) NOT NULL PRIMARY KEY, "
                + "balance float(100, 3) NOT NULL, "
                + "avg_daily float(100, 3), "
                + "FOREIGN KEY (aid) REFERENCES Account(aid) "
                + "ON UPDATE CASCADE ON DELETE CASCADE)");

            createTable.executeUpdate("CREATE TABLE Stock_Account( "
                + "aid int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                + "uid int(100) NOT NULL, "
                + "sid int(100) NOT NULL, "
                + "num_shares int(100), "
                + "FOREIGN KEY (uid) REFERENCES Account(aid) "
                + "ON UPDATE CASCADE ON DELETE CASCADE, "
                + "FOREIGN KEY (sid) REFERENCES Stock(sid)  "
                + "ON DELETE CASCADE )");

            createTable.executeUpdate("CREATE TABLE Transactions( "
                + "transaction_id int(100) AUTO_INCREMENT PRIMARY KEY, "
                + "aid int(100) NOT NULL, "
                + "sys_date varchar(100), "
                + "date bigint(255) NOT NULL, "
                + "FOREIGN KEY (aid) REFERENCES Account(aid) "
                + "ON DELETE CASCADE )");

            createTable.executeUpdate("CREATE TABLE deposit( "
                + "tr_id int(100) NOT NULL PRIMARY KEY, "
                + "amount float(13, 3) NOT NULL, "
                + "FOREIGN KEY(tr_id) REFERENCES Transactions(transaction_id) "
                + "ON UPDATE CASCADE ON DELETE CASCADE )");

            createTable.executeUpdate("CREATE TABLE Withdrawal( "
                + "tr_id int(100) NOT NULL PRIMARY KEY, "
                + "amount float(13,3) NOT NULL, "
                + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
                + "ON UPDATE CASCADE ON DELETE CASCADE "
                );
            
            createTable.executeUpdate("CREATE TABLE buy( "
                + "tr_id int(100) NOT NULL PRIMARY KEY, "
                + "sid int(100) NOT NULL, "
                + "amount float(13, 3) NOT NULL, "
                + "price float(13, 3) NOT NULL, "
                + "FOREIGN KEY (tr_id)	REFERENCES Transactions(transaction_id) "
                + "ON UPDATE CASCADE ON DELETE CASCADE, "
                + "FOREIGN KEY (sid) REFERENCES Stock(sid)  "
                + "ON DELETE CASCADE )");
            
            createTable.executeQuery("CREATE TABLE sell ( "
                + "tr_id int(100) NOT NULL PRIMARY KEY, "
                + "sid int(100) NOT NULL, "
                + "amount float(13, 3) NOT NULL, "
                + "price float(13, 3) NOT NULL, "
                + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
                + "ON UPDATE CASCADE ON DELETE CASCADE, "
                + "FOREIGN KEY (sid) REFERENCES Stock(sid) " 
                + "ON DELETE CASCADE )");
            
            createTable.executeQuery("CREATE TABLE accrue_interest ( "
                + "tr_id int(100) NOT NULL PRIMARY KEY, "
                + "amount float(7, 3), "
                + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
                + "ON UPDATE CASCADE ON DELETE CASCADE )");
              
            createTable.executeQuery("CREATE TABLE Stock ( "
                + "sid int(100) AUTO_INCREMENT PRIMARY KEY, "
                + "aid int(100) NOT NULL, "
                + "ssym char(3) NOT NULL, "
                + "closing_price float(13,3), "
                + "current_price float(13,3), "
                + "active enum('0', '1') "
                + "FOREIGN KEY (aid) REFERENCES Actor(aid) " 
                + "ON UPDATE CASCADE )");
              
            createTable.executeQuery("CREATE TABLE Actor ( "
                + "aid int(100) AUTO_INCREMENT PRIMARY KEY, "
                + "aname varchar(100), "
                + "sid int(100), "
                + "dob varchar(100), "
                + "FOREIGN KEY (sid) REFERENCES Stock(sid) "
                + "ON UPDATE CASCADE )");
              
            createTable.executeQuery("CREATE TABLE Contracts ( "
                + "mid int(100) NOT NULL,  "
                + "aid int(100) NOT NULL, "
                + "title varchar(255) NOT NULL, "
                + "role enum('Actor', 'Director', 'Both'), "
                + "year_released int(100), "
                + "value int(100) NOT NULL, "
                + "PRIMARY KEY(mid, aid), "
                + "FOREIGN KEY (aid) "
                + "REFERENCES Actor(aid) )");

            
            // PreparedStatement insert = connection.prepareStatement ("INSERT INTO "
            //     + tableName + " (I, WORD, SQUARE, SQUAREROOT) "
            //     + " VALUES (?, ?, ?, ?)");  

            
        }
        catch (Exception e) {
            System.out.println ();
            System.out.println ("ERROR: " + e.getMessage());
        }
        finally {
            try {
                if (connection != null)
                    connection.close (); 
            }
            catch (SQLException e) {
                // Ignore.
            }
        }

        System.exit (0);
    }

}
