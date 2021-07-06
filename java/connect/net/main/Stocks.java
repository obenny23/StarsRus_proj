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

    /*          Make Changes to Stocks Database     */

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

    public static void buyStock(String buySym, int tid, int shares) {
        double balance = interfDB.getBalance(tid);
        double price = getStockPrice(buySym);
        double total = price * shares + 20.00;
        if (shares <= 0){
            System.out.println("Must buy 1 or more shares.");
            System.out.println("Transaction failed.");
            return;
        }

        if(balance < total){
            System.out.println("Cannot buy " + shares + " shares of " + buySym 
            + ".\nPurchase total exceeds your account balance.\nTransaction failed.\n");
        }else{
            Market.subToMarketBalance(total, tid);
            Transactions.recordTransaction(tid, buySym, price, shares, 0.00);
            if(holdsStock(tid,buySym)){
                updateStockAcc(tid, buySym, shares, price);
            }else {
                addNewStockAccount(tid, buySym, shares, price);
            }
        System.out.println("Successfully bought " + shares + " shares of " + buySym + "!");
        System.out.println("Transaction total: " + String.format("%.2f", total));
        }
    }

    public static void sellStock(String sellSym, int tid, int shares){
        double buyPrice = getStockPurchasePrice(sellSym, tid);
        double currPrice = getStockPrice(sellSym);
        double profit = (currPrice - buyPrice) * shares;
        double total = currPrice*shares - 20.00;
        int sharesOwned = getNumShares(tid, sellSym, buyPrice);

        if (shares <= 0){
            System.out.println("Must sell at least 1 share.");
            System.out.println("Transaction failed.");
            return;
        }

        if (sharesOwned < shares){
            System.out.println("Cannot sell " + shares + " shares of " + sellSym 
            + ".\nRequested number of shares exceeds shares owned.\nTransaction failed.\n");
        }else{
            Transactions.recordTransaction(tid, sellSym, currPrice, -shares, profit);    
            Market.addToMarketBalance(total, tid);
            updateStocksOwned(tid, sellSym, shares, buyPrice);
            System.out.println("Successfully sold " + shares + " shares of " + sellSym + "!");
            System.out.println(String.format("$%.2f", total) + " added to your account balance.\n" 
                            + "Your profit from this transaction was " + String.format("$%.2f", profit));
        }
    }

    private static void updateStockAcc(int tid, String buySym, int shares, double price) {
        String sql = "UPDATE Stocks SET price=?, shares= shares +? WHERE tid=? AND sym=?";
        Double pricePurch = getStockPurchasePrice(buySym, tid);
        Integer ownedShares = getNumShares(tid, buySym, pricePurch);
        Double priceAve = ((shares * price) + (ownedShares*pricePurch)) / (ownedShares+shares);

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setDouble(1, priceAve);
            prepstmt.setInt(2, shares);
            prepstmt.setInt(3, tid);
            prepstmt.setString(4, buySym);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }

    private static void updateStocksOwned(int tid, String sellSym, int shares, Double price) {
        String sql = "";
        Integer ownedShares = getNumShares(tid, sellSym, price);

        if (ownedShares == shares){
            sql = "DELETE FROM Stocks WHERE tid=? AND sym=? AND price=?";
        } else{
            sql = "UPDATE Stocks SET shares= shares - ? WHERE tid=? AND sym=? AND price=?";
        }

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            if (ownedShares == shares){
                prepstmt.setInt(1, tid);
                prepstmt.setString(2, sellSym);
                prepstmt.setDouble(3, price);
            }else {
                prepstmt.setInt(1, shares);
                prepstmt.setInt(2, tid);
                prepstmt.setString(3, sellSym);
                prepstmt.setDouble(4, price);
            }
            prepstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }
    }

    /*          Display Functions       */

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
        }finally{
            close();
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
                System.out.println("Stock: " + ssym + ", Current price: " + String.format("$%.3f", price));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }
    }

    public static boolean showStocksOwned(int tid){
        String sql = "SELECT sym, shares, price FROM Stocks WHERE tid=?";
        boolean empty = false;

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();

            
            if (!rs.isBeforeFirst()){
                System.out.println("No stocks owned at the moment.");
                return true;
            }            
            while(rs.next()){
                String s = rs.getString("sym");
                Integer shares = rs.getInt("shares");
                Double price = rs.getDouble("price");
                System.out.println(" " + s + "  " + shares +" shares at $" + String.format("%.2f", price) + " a share");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
        return empty;
    }


    /*          Stock DB Queries        */

    public static double getStockPrice(String sym){
        String sql =  "SELECT curr_price FROM Actors WHERE sym = ?";
        double price = -1.0;

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, sym);
            ResultSet rs = prepstmt.executeQuery();

            if(rs.next()){
                price = rs.getDouble("curr_price");
            }else{
                price = -1.0;
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Stock " + sym + " does not exist.");

        }finally{
            close();
        }

        return price;
    }
    
    private static Integer getNumShares(int tid, String sym, Double price){
        String sql = "Select shares FROM Stocks WHERE tid=? AND sym=? AND price=?";
        Integer shares = 0;

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            prepstmt.setString(2, sym);
            prepstmt.setDouble(3, price);
            ResultSet rs = prepstmt.executeQuery();

            if(rs.next()){
                shares = rs.getInt("shares");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }
        return shares;
    }

    public static double getStockPurchasePrice(String sym, int tid) {
        String sql = "SELECT price FROM Stocks WHERE sym=? AND tid=?";
        double purchPrice = 0.00;

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, sym);
            prepstmt.setInt(2, tid);
            ResultSet rs = prepstmt.executeQuery();

            if (!rs.isBeforeFirst()){
                purchPrice = -1.00;
            }
            while(rs.next()){
                double price = rs.getDouble("price");
                purchPrice = price;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
        return purchPrice;
    }

    private static boolean holdsStock(int tid, String buySym) {
        boolean exists = false;
        String sql = "SELECT * FROM Stocks WHERE tid=? AND sym=?";

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ps.setString(2, buySym);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close();
        }
        return exists;
    }


    /*          Manager Functions          */

    public static int changeStockPrice(String sym, Double newprice){
        String sql = "UPDATE Actors SET curr_price=? WHERE sym=?;";
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

}