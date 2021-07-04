package net.main;

import java.sql.*;

public class Market {
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

    static String addMarketAcc(int tid) {
        String newAid = "---";
        Integer num = getNumMarketAccounts()+1;
        
        if (num >= 100) {
            newAid = num.toString(); 
        }
        else if (num >= 10){
            newAid = "0" + num.toString();
        }else{
            newAid = "00" + num.toString();
        }

        String sql = "INSERT INTO Markets(tid, aid, balance) "
            + "VALUES (?, ?, ?)";

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            prepstmt.setString(2, newAid);
            prepstmt.setDouble(3, 1000.00);
            prepstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("wtf");
        }finally{
            close();
        }

        return newAid;
    }

    private static Integer getNumMarketAccounts() {
        Integer count = 0;
        String sql = "SELECT COUNT(aid) FROM Markets";

        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()){
                count = rs.getInt(1);
            }
        } catch (Exception se) {
                se.printStackTrace();
        }
        finally{
            close();
        }
        return count;
    }

    public static void addToMarketBalance(Double amount, int tid) {
        String sql = "UPDATE Markets SET balance = balance + ? WHERE aid=?";
        String aid = getMarketID(tid);

        try
        {
          connect();
          PreparedStatement prepstmt = conn.prepareStatement(sql);
          prepstmt.setDouble(1, amount);
          prepstmt.setString(2, aid);
          prepstmt.executeUpdate();
        }catch(SQLException se){
          se.printStackTrace();
        }finally {
            close();
        }
        recordMarketTransaction(tid, amount);
    }

    public static void subToMarketBalance(Double amount, int tid) {
        String sql = "UPDATE Markets SET balance = balance - ? WHERE aid=?";
        String aid = getMarketID(tid);

        try
        {
          connect();
          PreparedStatement prepstmt = conn.prepareStatement(sql);
          prepstmt.setDouble(1, amount);
          prepstmt.setString(2, aid);
          prepstmt.executeUpdate();
          
        }catch(SQLException se){
          se.printStackTrace();
        }finally{
            close();
        }
        recordMarketTransaction(tid, amount);
    }

    public static String getMarketID(int tid) {
        String sql = "SELECT aid FROM Markets WHERE tid=?";
        String aid = "";

        try
        {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();
            if(rs.next()){
                aid = rs.getString("aid");
            }
        }catch(SQLException se)
        {
            se.printStackTrace();
        }finally {
            close();
        }
        return aid;
    }

    public static void recordMarketTransaction(int tid, double amount){
        String date = interfDB.getCurrentDate();
        Double balance = interfDB.getBalance(tid);
        String sql = "INSERT INTO MarketTransactions (date, ctid, amount, balance)"
                    + "VALUES( ?, ?, ?, ?)";

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, date);
            prepstmt.setInt(2, tid);
            prepstmt.setDouble(3, amount);
            prepstmt.setDouble(4, balance);
            prepstmt.executeUpdate();

        }catch(SQLException se){
            se.printStackTrace();
        }finally{
            close();
        }
    }

}
