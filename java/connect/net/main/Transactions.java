package net.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Transactions {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";
    private static Connection conn = null;
    private static Statement stmt = null;
    private static PreparedStatement prepstmt = null;
    
    public static void connect() {
        try {
            conn = DriverManager.getConnection(DB_URL);
                        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void close() {
        try
        {
            //close statement
            if(stmt != null)
                stmt.close();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        try
        {
            if(prepstmt != null)
                prepstmt.close();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        try
        {
            //close connection
            if(conn!=null)
            {
                conn.close();
                conn = null;
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
    }

    public static void record_transaction(String date, int customerTAXID, String sym, double price, int shares, double profit){
        String sql = "INSERT INTO StockTransactions (date, ctid,sym ,price ,shares,profit)" +
                        "VALUES( ?, ?, ?, ?, ?, ?)";
        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, date);
            prepstmt.setInt(2, customerTAXID);
            prepstmt.setString(3, sym);
            prepstmt.setDouble(4, price);
            prepstmt.setInt(5, shares);
            prepstmt.setDouble(6, profit);
            prepstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }

    }

    public static void delete_transaction(){
        String sql = "DELETE " +
                        "FROM StockTransactions ";
        try {
            connect();

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    // public static int get_total_shares(String taxID){
    //     String QUERY =  "SELECT * " +
    //                     "FROM StockTransactions " +
    //                     "WHERE CustomerTAXID = " + "'" + taxID + "'";

    //     ResultSet resultSet = Utility.sql_query(QUERY);

    //     int sum = 0;
    //     try{
    //         while(resultSet.next()){
    //             int shares = resultSet.getInt("shares");

    //             sum += Math.abs(shares);
    //         }
    //     } catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return sum;
    // }

    // public static double get_total_profit(String taxID){
    //     String QUERY =  "SELECT * " +
    //                     "FROM StockTransactions " +
    //                     "WHERE CustomerTAXID = " + "'" + taxID + "'";

    //     ResultSet resultSet = Utility.sql_query(QUERY);

    //     double sum = 0;
    //     try{
    //         while(resultSet.next()){
    //             double profit = resultSet.getDouble("profit");

    //             sum += profit;
    //         }
    //     } catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return sum;
    // }

    // public static String get_transactions(String taxID){
    //     String QUERY =  "SELECT * " +
    //                     "FROM StockTransactions " +
    //                     "WHERE CustomerTAXID = " + "'" + taxID + "'";

    //     ResultSet resultSet = Utility.sql_query(QUERY);

    //     String res = "";

    //     try{
    //         while(resultSet.next()){
    //             String date = resultSet.getString("date");
    //             String customerTAXID = resultSet.getString("customerTAXID");
    //             String actorID = resultSet.getString("actorID");
    //             double price = resultSet.getDouble("price");
    //             int shares = resultSet.getInt("shares");
    //             double profit = resultSet.getDouble("profit");


    //             res += "Date: " + date;
    //             res += ", Transaction type: " + ((shares>0) ? "Buy" : "Sell");
    //             res += ", Customer TaxID: "  + customerTAXID;
    //             res += ", Stock symbol: " + actorID;
    //             res += ", Price: " + (new Double(price)).toString();
    //             res += ", Shares: " + (new Integer(shares)).toString();
    //             res += ", Profit: " + (new Double(profit)).toString();
    //             res += "\n";
    //         }
    //     } catch(Exception e){
    //         e.printStackTrace();
    //     }

    //     return res;
    // }
}
