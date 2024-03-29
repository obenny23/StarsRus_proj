package net.main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /*          Balance Changes      */

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
        recordMarketTransaction(tid, -amount);
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

    public static void addInterest(int mtid) {
        float rate = 0.00166666666666666667f;
        double interest = -1.0;
        int tid = -1;
        double balance, ave;
    
        HashMap<Integer , Double> aveBalance = getMarketAverageBalances();        
        Iterator<Map.Entry<Integer,Double>> balances = aveBalance.entrySet().iterator();
        
        while(balances.hasNext()){
            Map.Entry<Integer, Double> account_balance = balances.next();
            tid = account_balance.getKey();
            ave = account_balance.getValue();
            interest = rate * ave;

            // // Delete all transactions if interest accrued after end of month
            // // if(Date.getDay(interfDB.getCurrentDate()) == 1){
            // //     Transactions.deleteTransactions();
            // // }
    
            Market.addToMarketBalance(interest, tid);
            balance = interfDB.getBalance(tid);
            System.out.println("Interest accrued for account with TaxID :" + tid + " is " + String.format("$%.2f", interest)
                + "'\nAccount Balance now " + String.format("$%.2f", balance));
            Transactions.accrueInterest(tid, mtid, interest, balance);
        }
    
    }
    
    private static HashMap<Integer, Double> getMarketAverageBalances() {
        HashMap<Integer, Double> averages = new HashMap<>();
        List<Integer> tids = getTids();
        String date;
        Double ave ;
        int month;
        int amountDays;
        int day;
        int daysInMonth = 0;
    
        double bal = 0.00;
        double pastBalance = 0.00;
        Double total = 0.0;
    
        for (Integer tid : tids) {
            date = "";
            day = amountDays = 0;
            ave = bal = total = pastBalance = 0.00;
    
            HashMap <String, Double> EODBalances = getEndOfDayMarketBalances(tid);
    
            if(EODBalances.isEmpty()){
                ave = interfDB.getBalance(tid);
            }else {
    
                for (Map.Entry<String, Double> set : EODBalances.entrySet()) {
                    date = set.getKey();
                    bal = set.getValue();
    
                    // first date get total days in month, and start sum 
                    if (amountDays == 0){
                        month = Date.getMonth(date);
                        daysInMonth = Date.daysInMonth(month);
    
                        pastBalance = getInitialBalance(tid);
                        day = Date.getDay(date);
                        total += day * pastBalance;
                        pastBalance = bal;
                        amountDays = -1;
    
                    }else {
                        amountDays = Date.getDay(date) - day;
                        day = Date.getDay(date);
                        total += amountDays*pastBalance;
                        pastBalance = bal;
                    } 
                }
    
                if (day != daysInMonth){
                    total += (daysInMonth-day) * pastBalance;
                }
    
                ave = total/daysInMonth;
            }
            averages.put(tid, ave);
        }
    
        return averages;
    }
    
    private static HashMap<String, Double> getEndOfDayMarketBalances(Integer tid) {
        String sql = "SELECT date, balance FROM MarketTransactions WHERE ctid=?";
        HashMap<String, Double> balances = new HashMap<>();

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            while(rs.next()){
                balances.put(rs.getString("date"), rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
        return balances;
    }

    public static List<Integer> getTids(){
        String sql = "SELECT DISTINCT tid FROM Markets";
        List<Integer> tids = new ArrayList<>();

        try{
            connect();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                tids.add(rs.getInt("tid"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

        return tids;
    }

    public static Double getInitialBalance(int tid) {
        String sql = "SELECT * FROM MarketTransactions WHERE ctid=?";
        Double balance = 0.00;
        int day = 0;

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                day = Date.getDay(rs.getString("date"));
                if (day == 1){
                    balance = rs.getDouble("balance");
                } else{
                    balance = rs.getDouble("balance") - rs.getDouble("amount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }

        return balance;
    }
}
