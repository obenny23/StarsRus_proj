package net.main;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class interfDB {

    private final Integer OPEN = 9;
    private final Integer CLOSE = 11;

    private static final String DB_URL = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";
    private static Connection conn = null;
    private static Statement stmt;
    private static PreparedStatement prepstmt;

    public static void connect() {
        try {
            // db parameters
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

    public static boolean isMarketOpen()
    {
        String sql = "";
        boolean isMarketOpen = false;

        try
        {
            connect();

            sql = "SELECT Open FROM Date;";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                if(rs.getInt("Open") == 1)
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
    public static String getDate(){
        String QUERY =  "SELECT * " +
                        "FROM Date ";

        // ResultSet resultSet = Utility.sql_query(QUERY);

        String date = "";

        // try{
        //     if(!resultSet.next()){
        //         System.out.println("current date not found!");
        //         System.exit(1);
        //     }

        //     date = resultSet.getString("d");
        // } catch (Exception e){
        //     e.printStackTrace();
        // }

        return date;
    }

    public static void changeDate(String date){
        ResultSet rset = null;

        String sql1 = "DELETE " +
                "FROM Date ";

        String sql2 = "INSERT INTO Date " +
                "VALUES('" + date + "')";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2); 

        } catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
           try {
               if(stmt != null)
                   stmt.close();
           } catch(Exception e){
               e.printStackTrace();
           }
        }
    }

    public static void openMarket(){

    }

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

    public static Integer createAccount(int taxId, String usrname, String pswd, String cName,
                            String cstate, String phoneNum, String cEmail, String SSN){
        Integer success = -1;
        String sql = "";

        if (getAccount(false, usrname, pswd).getTid() != -1) {
            return 2;
        }

        System.out.println("Creating account in database");    
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

	public static void closeMarket() {
	}

    static String getCurrentDate() {
        String date = "06-09-2021";
        return date;
    }

}


