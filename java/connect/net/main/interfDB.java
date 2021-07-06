package net.main;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class interfDB {

    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";
    private static Connection conn = null;
    private static Statement stmt;
    private static PreparedStatement prepstmt;

    private static void connect() {
        try {
            // create a connection to the database
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
    
    public static Account getAccount(Boolean isManager, String username, String password)
    { 
        //create query
        String sql = "";
        Account account =  null;
        try
        {
            connect();

            //customer/manager login query
            if(!isManager)
                sql = "SELECT * FROM Customers WHERE username=? AND password=?";
            else
                sql = "SELECT * FROM Managers WHERE username=? AND password=?";

            //use prepared statement
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, username);
            prepstmt.setString(2, password);

            //execute query
            ResultSet rs = prepstmt.executeQuery();

            //process query
            if(rs.next())
            {
                account = new Account(rs.getInt("tid"),username, password, rs.getString("cname"),
                rs.getString("state"), rs.getString("phonenumber"),
                rs.getString("email"),rs.getInt("ssn"));
            } else {
                account = new Account(-1, username, password, "", "", "", "", 0);
            }
        }

        catch(SQLException se)
        {
            se.printStackTrace();
            return account;
        }
        finally
        {
            close();
        }
        return account;
    }

    static double getBalance(int tid) {
        double balance = -1;
        String sql = "SELECT balance FROM Markets WHERE tid=?";
        
        try {
            connect();

            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, tid);
            ResultSet rs = prepstmt.executeQuery();
            
            if (rs.next()){
                balance = rs.getDouble("balance");
            }
            } catch (Exception se) {
                se.printStackTrace();
            }
            finally{
                close();
            }
            return balance;
    }

    public static boolean isMarketOpen(){
        String sql = "SELECT Open FROM Date";
        boolean isMarketOpen = false;

        try
        {
            connect();

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.getInt("Open") == 1){
                    isMarketOpen = true;
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
            return false;
        }
        finally
        {
            close();
        }
        return isMarketOpen;
    }

    /*      For Date         */
    public static String getCurrentDate() {
        String date = "";
        String sql = "SELECT date FROM Date";

        try{
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()){
                date = rs.getString("date");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

        return date;
    }

    public static void changeDate(String date){
        String sql = "UPDATE Date SET date =?";
        
        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, date);
            prepstmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
           close();
        }
    }

    public static void openMarket(){
        String sql = "UPDATE Date SET open=1";
        try {
            connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }finally{
            close();
        }
    }

    public static void closeMarket() {
        String sql = "UPDATE Date SET open=0";
        try {
            connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }finally{
            close();
        }
	}

    public static Integer createAccount(int taxId, String usrname, String pswd, String cName,
                            String cstate, String phoneNum, String cEmail, String SSN){
        Integer success = -1;
        String sql = "";

        if (getAccount(false, usrname, pswd).getTid() != -1) {
            return 2;
        }

        sql = "INSERT INTO Customers (tid, username, password, cname, "
              + "state, phonenumber, email, ssn)"
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, taxId);
            prepstmt.setString(2, usrname);
            prepstmt.setString(3, pswd);
            prepstmt.setString(4, cName);
            prepstmt.setString(5, cstate);
            prepstmt.setString(6, phoneNum);
            prepstmt.setString(7, cEmail);
            prepstmt.setString(8, SSN);
            prepstmt.executeUpdate();
            success = 1;
        } catch(SQLException se) {
            se.printStackTrace();
            success = -1;
        } finally {
            close();
        }

        return success;
    }



    public static void listActiveCustomers() {
        List<Integer> tids = Market.getTids();
        List<Integer> activeTids = new ArrayList<>();
        List<Integer> sh = new ArrayList<>();
        Integer shares = 0;

        for (Integer tid : tids) {
            shares = Transactions.getTotalShares(tid);
            if (shares >= 1000){
                activeTids.add(tid);
                sh.add(shares);
            }
        }

        if(activeTids.isEmpty()){
            System.out.println("No Active Accounts this month.");
        }else{
            showActiveAccounts(activeTids, sh);
        }
    }

    private static void showActiveAccounts(List<Integer> activeTids, List<Integer> sh) {
        int i = 0;
        String sql = "SELECT cname FROM Customers WHERE tid=?";

        try {
            connect();

            for (Integer tid : activeTids) {
                PreparedStatement prepstmt = conn.prepareStatement(sql);
                prepstmt.setInt(1, tid);
                ResultSet rs = prepstmt.executeQuery();

                if (rs.next()){
                    System.out.println(tid + " | " + rs.getString("cname") + "     Shares: " + sh.get(i));
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }
    }

    public static void generateDTER() {
        List<Integer> tids = Market.getTids();
        HashMap<Integer, Double> earn = new HashMap<>();
        Double totalProfit = 0.00;
        Double interest = 0.00;
        Double earnings = 0.00;

        for (Integer tid : tids) {
            totalProfit = Transactions.getTotalProfit(tid);
            interest = Transactions.getInterestCharged(tid);

            earnings = totalProfit + interest;
            if (earnings> 10000.00){
                earn.put(tid, earnings);
            }
        }
        
        System.out.println("Listing Customers who earned more than $10000");
        System.out.println("-----------------------------------------------");
        if(earn.isEmpty()){
            System.out.println("No such customers this month.");
        } else{
            listEarners(earn);
        }
    }
    
    private static void listEarners(HashMap<Integer, Double> earn) {
        String st = "";
        String cname = "";

        for (Map.Entry<Integer, Double> set :earn.entrySet()) {
            st = getStateOfResidency(set.getKey());
            cname = getName(set.getKey());
            System.out.println(cname + "   State: " + st + "     Total Earnings: " 
                + String.format("$%.2f", set.getValue()));
        }
    }

    public static String getName(Integer tid) {
        String sql = "SELECT cname FROM Customers WHERE tid=?";
        String name = "";

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                name = rs.getString("cname");
            }
            
        } catch (SQLException se) {
            se.printStackTrace();
        } finally{
            close();
        }

        return name;
    }

    public static String getEmail(Integer tid) {
        String sql = "SELECT email FROM Customers WHERE tid=?";
        String email = "";

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                email = rs.getString("email");
            }
            
        } catch (SQLException se) {
            se.printStackTrace();
        } finally{
            close();
        }

        return email;
    }

    public static String getPhone(Integer tid) {
		String sql = "SELECT phonenumber FROM Customers WHERE tid=?";
        String phonenumber = "";

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                phonenumber = rs.getString("phonenumber");
            }
            
        } catch (SQLException se) {
            se.printStackTrace();
        } finally{
            close();
        }

        return phonenumber;
	}

    public static String getStateOfResidency(int tid){
        String sql = "SELECT state FROM Customers WHERE tid=?";
        String st = "";

        try {
            connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                st = rs.getString("state");
            }
            
        } catch (SQLException se) {
            se.printStackTrace();
        } finally{
            close();
        }

        return st;
    }


    /*          Failed Methods 
    public static void updateDB(String sql){
        Connection conn  = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        Statement statement = null;

        try{
            // find the username and password pair entity
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if(statement != null)
                    statement.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static ResultSet queryDB(String sql) {
        ResultSet rset = null;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            try{
                Statement statement = conn.createStatement();
                rset =  statement.executeQuery(sql);
                return rset;
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rset;
    }

    private final Integer OPEN = 9;
    private final Integer CLOSE = 23;
    private void checkMarketHours(){
        String time = getTime();
        Integer hour = Integer.parseInt(time.substring(0, 2));
        if (hour >= 9 && hour < 23){
            openMarket();
        }else if(hour >= 23 && hour < 9){
            closeMarket();
        }
    }
    private String getTime() {
        return null;
    } 
    */

}


