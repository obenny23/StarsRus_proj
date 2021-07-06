package net.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static void recordTransaction(int cTid, String sym, double price, int shares, double profit){
        String sql = "INSERT INTO StockTransactions (date, ctid,sym ,price ,shares,profit)" 
                    + "VALUES( ?, ?, ?, ?, ?, ?)";
        String date = interfDB.getCurrentDate();

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, date);
            prepstmt.setInt(2, cTid);
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

    public static void accrueInterest(int tid, int mtid, Double interest, Double balance){
        String sql = "INSERT INTO AccruedInterest(date, ctid, mtid, interest, balance) "
                + "VALUES (?, ?, ?, ?, ?)";
        String date = interfDB.getCurrentDate();

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, date);
            prepstmt.setInt(2, tid);
            prepstmt.setInt(3, mtid);
            prepstmt.setDouble(4, interest);
            prepstmt.setDouble(5, balance);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }
    
    public static void deleteTransactions(){
        String sql = "DELETE FROM StockTransactions ";
        try {
            connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            sql = "DELETE FROM MarketTransactions ";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            sql = "DELETE FROM AccruedInterest";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void showTransactionHistory(int tid) {
        String sql = "";
        String trans = "";

        sql = "SELECT * FROM StockTransactions WHERE ctid=?";

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();
            if(!rs.isBeforeFirst()){
                System.out.println("No Stock Transactions this month.\n");
            }else{
                System.out.println("        Stock Transactions      \n");
                System.out.println("   Date      Stock  Shares   Price   Profit");
                System.out.println("---------------------------------------------");
                
                while(rs.next()){
                    trans = rs.getString("date") + " | "
                            + rs.getString("sym") + "    " 
                            + rs.getInt("shares") + "    " 
                            + String.format("$%.2f",rs.getDouble("price")) + "  "
                            + String.format("$%.2f",rs.getDouble("profit"));

                    System.out.println(trans);
                }
            }


            sql = "SELECT * FROM MarketTransactions WHERE ctid=?";
            String am = "";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){
                System.out.println("No Market Transactions this month.\n");
            }else{
                System.out.println("\n          Market Transactions    \n");
                System.out.println("    Date        Amount          Balance");
                System.out.println("-----------------------------------------");
                
                while(rs.next()){
                    am = String.format(" $%.2f",rs.getDouble("amount"));

                    if (am.charAt(2) == '-'){
                        am = "-$" + am.substring(3);
                    }

                    if (am.length() <= 14){
                        for (int i = am.length(); i <= 14;i++){
                            am += " ";
                        }
                    }

                    trans = rs.getString("date") + " | "
                            + am + "  "
                            + String.format("$%.2f",rs.getDouble("balance"));
                    System.out.println(trans);
                }
            }

            sql = "SELECT * FROM AccruedInterest WHERE ctid=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, tid);
            rs = pst.executeQuery();

            if(!rs.isBeforeFirst()){
                System.out.println("No interest accrued this month.\n");
            }
            if(rs.next()){
                Double interest = rs.getDouble("interest");
                Double balance = rs.getDouble("balance");
                System.out.println("Interest accrued for the month was "
                    + String.format("$%.2f", interest) + ", for an account balance of "
                    + String.format("$%.2f", balance));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }

    public static int getTotalShares(int tid){
        String sql =  "SELECT shares FROM StockTransactions WHERE ctid =?";
        int total = 0;
        
        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            while(rs.next()){
                int shares = rs.getInt("shares");
                total += Math.abs(shares);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return total;
    }

    public static Double getTotalProfit(int tid){
        String sql =  "SELECT profit FROM StockTransactions WHERE ctid =?";
        Double total = 0.0;
        
        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            while(rs.next()){
                Double profit = rs.getDouble("profit");
                total += profit;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return total;
    }

    public static Double getInterestCharged(int tid){
        String sql =  "SELECT interest FROM AccruedInterest WHERE ctid =?";
        Double total = 0.0;
        
        try{
            connect();

            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            if(rs.next()){
                Double interest = rs.getDouble("interest");
                total += interest;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return total;
    }

    public static Integer getCommissions(Integer t) {
        String sql = "SELECT COUNT(*) FROM StockTransactions WHERE ctid=?";
        int count = 0;

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, t);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }

        return count;
    }
}
