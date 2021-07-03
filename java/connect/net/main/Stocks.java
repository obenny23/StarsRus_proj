package net.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class Stocks {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";
    private static Connection conn = null;
    private static Statement stmt;
    private static PreparedStatement prepstmt;
    
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

    public static void addNewStockAccount(int tid, String sym, int shares, Double price){
        String sql = "INSERT INTO Stocks(tid, shares, sym, price) "
            + "VALUES (?, ?, ?, ?);";
        try {
            connect();

            // Create new entry in ownsStock, creating a new stock account
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            prepstmt.setInt(2, shares);
            prepstmt.setString(3, sym);
            prepstmt.setDouble(4, price);
            prepstmt.executeUpdate();

        } catch(SQLException se) {
            se.printStackTrace();
        } finally {
            close();
        }
    }

    public static void showStocks(){
        String sql = "SELECT DISTINCT sym FROM Actors";
        String ssym = "";
        System.out.println("    Stocks    \n---------------- ");

        try{
            connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                ssym = rs.getString("sym");
                System.out.println(ssym);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void showStocksWPrices(){
        String sql = "SELECT DISTINCT sym, curr_price FROM Actors";
        String ssym = "";
        Double price = 0.00;

        try{
            connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                ssym = rs.getString("sym");
                price = rs.getDouble("curr_price");
                System.out.println("Stock: " + ssym + ", Current price: " + price);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void showStocksOwned(int tid){
        String sql = "SELECT sym, shares, price FROM Stocks WHERE tid=?";

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            while(rs.next()){
                String s = rs.getString("sym");
                Integer shares = rs.getInt("shares");
                Double price = rs.getDouble("price");
                System.out.println(s + "    " + shares +" shares at $" + String.format("%.2f", price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }

    public static double getStockPrice(String sym){
        String sql =  "SELECT A.curr_price " +
                        "FROM Actors A " +
                        "WHERE A.sym = " + "'" + sym + "'";

        double res = -1.0;

        try{
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                res = rs.getDouble("curr_price");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return res;
    }
    
    public static int changeStockPrice(String sym, Double newprice){
        String sql = "UPDATE Actor SET curr_price=? WHERE sym=?;";
        int succ = 1;

        try {
            connect();

            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setDouble(1, newprice);
            prepstmt.setString(2, sym);
            prepstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not change price. Stock " + sym + " does not exist.");
            succ = -1;
        }finally {
            close();
        }

        return succ;
    }

    public static void buyStock(String buySym, int tid, int shares) {
        double price = getStockPrice(buySym);
        String date = interfDB.getCurrentDate();
        Transactions.record_transaction(date, tid, buySym, price, shares, 0.00);
        double total = price * shares + 20.00;
    }

    public static void sellStock(String sellSym, int tid, int shares){
        double price = getStockPrice(sellSym);
        String date = interfDB.getCurrentDate();
        Transactions.record_transaction(date, tid, sellSym, price, shares, 0.00);
        double total = price * shares + 20.00;
    }

    public static Double getCurrStockPrice(String sym){
        String sql = "SELECT curr_price FROM Actors WHERE sym=?";
        Double price = 0.00;

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, sym);
            ResultSet rs = prepstmt.executeQuery();
            if(rs.next()){
                price = rs.getDouble("curr_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            close();
        }
        return price;
    }
}