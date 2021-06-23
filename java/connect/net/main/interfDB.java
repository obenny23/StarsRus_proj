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
        // } finally {
        //     try {
        //         if (conn != null) {
        //             conn.close();
        //         }
        //     } catch (SQLException ex) {
        //         System.out.println(ex.getMessage());
        //     }
        // }
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


    public boolean changeStockPrice(String stocksymbol, float newprice)
    {
      String sql = "";
      try
      {
        connect();
        sql = "UPDATE Stock SET current_price=? WHERE ssym=?;";
        PreparedStatement prepstmt = conn.prepareStatement(sql);
        prepstmt.setFloat(1, newprice);
        prepstmt.setString(2, stocksymbol);
        prepstmt.executeUpdate();
      }
      
      catch(SQLException se)
      {
        se.printStackTrace();
        return false;
      }
    //   finally
    //   {
    //     close();
    //   }
      return true;
    }


    public boolean isMarketOpen()
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


    public static void createDB()
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

            /*  Create Customer Table and Populate */ 
            
            // createTable.executeUpdate ("CREATE TABLE Customers "
            //     + " (tid INTEGER UNIQUE, username NNVARCHAR(255) PRIMARY KEY, "
            //     + " password NNVARCHAR(255), cname NVARCHAR(100), state char(2), "
            //     + " phonenumber NVARCHAR(11), email NVARCHAR(64), ssn NVARCHAR(11)) "); 

            // createTable.executeUpdate("INSERT INTO Customers (cname, username, password, state, phonenumber, email, tid, ssn) "
            // + "VALUES  ('John Admin','admin','secret','CA','(805)6374632','admin@stock.com',1000,'606-60-6060'), "
            // + "('Alfred Hitchcock','alfred','hi','CA','(805)2574499', 'alfred@hotmail.com',1022,'606-76-7900'), "
            // + "('Billy Clinton','billy','cl','CA','(805)5629999','billy@yahoo.com',3045,'606-76-7903'), "
            // + "('Cindy Laugher','cindy','la','CA','(805)6930011','cindy@hotmail.com',2034,'606-70-7900'), "
            // + "('David Copperfill','david','co','CA','(805)8240011','david@yahoo.com',4093,'506-78-7900'), "
            // + "('Elizabeth Sailor','sailor','sa','CA','(805)1234567','sailor@hotmail.com',1234,'436-76-7900'), "
            // + "('George Brush','brush','br','CA','(805)1357999','george@hotmail.com',8956,'632-45-7900'), "
            // + "('Ivan Stock','ivan','st','NJ','(805)3223243','ivan@yahoo.com',2341,'609-23-7900'), "
            // + "('Joe Pepsi','joe','pe','CA','(805)5668123','pepsi@pepsi.com',0456,'646-76-3430'), "
            // + "('Magic Jordon','magic','jo','NJ','(805)4535539','jordon@jordon.org',3455,'646-76-8843'), "
            // + "('Olive Stoner','olive','st','CA','(805)2574499','olive@yahoo.com',1123,'645-34-7900'), "
            // + "('Frank Olson','frank','ol','CA','(805)3456789','frank@gmail.com',3306,'345-23-2134')");

            /*  Create Manager Table and Populate */ 
            
            // createTable.executeUpdate("CREATE TABLE Managers "
            //     + " (tid INTEGER UNIQUE, username NNVARCHAR(255) PRIMARY KEY, "
            //     + " password NNVARCHAR(255), cname NVARCHAR(100), state char(2), "
            //     + " phonenumber NVARCHAR(11), email NVARCHAR(64), ssn NVARCHAR(11)) "); 

            // createTable.executeUpdate("INSERT INTO Managers (cname, username, password, state, phonenumber, email, tid, ssn) "
            // + " VALUES ('John Admin','admin','secret','CA','(805)6374632','admin@stock.com',1000,'606-60-6060')");


            // createTable.executeUpdate("CREATE TABLE Date("
                //+ " Open INTEGER, "
                //+ " )");

            // createTable.executeUpdate("CREATE TABLE Account("
            //     + " aid INTEGER , "
            //     + " uid NVARCHAR(64) NOT NULL, "
            //     + " PRIMARY KEY(aid, uid), "
            //     + " FOREIGN KEY(uid) REFERENCES Customers(username)"
            //     + " ON UPDATE CASCADE ON DELETE CASCADE )");

            // createTable.executeUpdate("CREATE TABLE Market_Account( "
            //     + "aid INTEGER NOT NULL PRIMARY KEY, "
            //     + "balance REAL NOT NULL, "
            //     + "avg_daily REAL, "
            //     + "FOREIGN KEY (aid) REFERENCES Account(aid) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE)");

            // createTable.executeUpdate("CREATE TABLE Stock_Account( "
            //     + "aid INTEGER NOT NULL PRIMARY KEY , "
            //     + "uid INTEGER NOT NULL, "
            //     + "sid INTEGER NOT NULL, "
            //     + "num_shares INTEGER, "
            //     + "FOREIGN KEY (uid) REFERENCES Account(aid) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE, "
            //     + "FOREIGN KEY (sid) REFERENCES Stock(sid)  "
            //     + "ON DELETE CASCADE )");

            // createTable.executeUpdate("CREATE TABLE Transactions( "
            //     + "transaction_id INTEGER  PRIMARY KEY, "
            //     + "aid INTEGER NOT NULL, "
            //     + "sys_date NVARCHAR(100), "
            //     + "date bigint(255) NOT NULL, "
            //     + "FOREIGN KEY (aid) REFERENCES Account(aid) "
            //     + "ON DELETE CASCADE )");

            // createTable.executeUpdate("CREATE TABLE deposit( "
            //     + "tr_id INTEGER NOT NULL PRIMARY KEY, "
            //     + "amount REAL NOT NULL, "
            //     + "FOREIGN KEY(tr_id) REFERENCES Transactions(transaction_id) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE )");

            // createTable.executeUpdate("CREATE TABLE Withdrawal( "
            //     + "tr_id INTEGER NOT NULL PRIMARY KEY, "
            //     + "amount REAL NOT NULL, "
            //     + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE )" );
            
            // createTable.executeUpdate("CREATE TABLE buy( "
            //     + "tr_id INTEGER NOT NULL PRIMARY KEY, "
            //     + "sid INTEGER NOT NULL, "
            //     + "amount REAL NOT NULL, "
            //     + "price REAL NOT NULL, "
            //     + "FOREIGN KEY (tr_id)	REFERENCES Transactions(transaction_id) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE, "
            //     + "FOREIGN KEY (sid) REFERENCES Stock(sid)  "
            //     + "ON DELETE CASCADE )");
            
            // createTable.executeUpdate("CREATE TABLE sell ( "
            //     + "tr_id INTEGER NOT NULL PRIMARY KEY, "
            //     + "sid INTEGER NOT NULL, "
            //     + "amount REAL NOT NULL, "
            //     + "price REAL NOT NULL, "
            //     + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE, "
            //     + "FOREIGN KEY (sid) REFERENCES Stock(sid) " 
            //     + "ON DELETE CASCADE )");
            
            // createTable.executeUpdate("CREATE TABLE accrue_interest ( "
            //     + "tr_id INTEGER NOT NULL PRIMARY KEY, "
            //     + "amount REAL, "
            //     + "FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id) "
            //     + "ON UPDATE CASCADE ON DELETE CASCADE )");
              
            // createTable.executeUpdate("CREATE TABLE Stock ( "
            //     + "sid INTEGER  PRIMARY KEY, "
            //     + "aid INTEGER NOT NULL, "
            //     + "ssym NVARCHAR(3) NOT NULL, "
            //     + "closing_price REAL, "
            //     + "current_price REAL, "
            //     + "FOREIGN KEY (aid) REFERENCES Actor(aid) " 
            //     + "ON UPDATE CASCADE )");
              
            // createTable.executeUpdate("CREATE TABLE Actor ( "
            //     + "aid INTEGER PRIMARY KEY, "
            //     + "aname NVARCHAR(100), "
            //     + "sid INTEGER, "
            //     + "dob NVARCHAR(100), "
            //     + "FOREIGN KEY (sid) REFERENCES Stock(sid) "
            //     + "ON UPDATE CASCADE )");
              
            // createTable.executeUpdate("CREATE TABLE Contracts ( "
            //     + "mid INTEGER NOT NULL,  "
            //     + "aid INTEGER NOT NULL, "
            //     + "title NNVARCHAR(255) NOT NULL, "
            //     + "role enum('Actor', 'Director', 'Both'), "
            //     + "year_released INTEGER, "
            //     + "value INTEGER NOT NULL, "
            //     + "PRIMARY KEY(mid, aid), "
            //     + "FOREIGN KEY (aid) "
            //     + "REFERENCES Actor(aid) )");

            // createTable.executeUpdate("CREATE TABLE Date ( "
            //     + "open INTEGER, "
            //     + "date NVARCHAR (10))");

            
            // // PreparedStatement insert = connection.prepareStatement ("INSERT INTO "
            // //     + tableName + " (I, WORD, SQUARE, SQUAREROOT) "
            // //     + " VALUES (?, ?, ?, ?)");  

            // // Data Insertions
           
            // createTable.executeUpdate("INSERT INTO Account(uid) "
            //     + "VALUES ('admin'), "
            //     + "('alfred'), "
            //     + "('billy'), "
            //     + "('cindy'), "
            //     + "('david'), "
            //     + "('sailor'), "
            //     + "('brush'), "
            //     + "('ivan'), "
            //     + "('joe'), "
            //     + "('magic'), "
            //     + "('olive'), "
            //     + "('frank') ");
    
            // createTable.executeUpdate("INSERT INTO Actor(ssym, aname, dob) "
            //     + "VALUES ('SKB','Kim Basinger', '12-08-1958'), "
            //     + "('SMD','Michael Douglas','09-25-1944'), "
            //     + "('STC','Tom Cruise','07-03-1962')");

            // createTable.executeUpdate("INSERT INTO Contracts(mid, aid, title, role, year_released, value) "
            //     + "VALUES (1, 1, 'L.A. Confidential', 'Actor', 1997, 5000000), "
            //     + "(2, 2, 'Perfect Murder', 'Actor', 1998, 10000000), "
            //     + "(3, 3, 'Jerry Maguire', 'Actor', 1996, 5000000)");

            // createTable.executeUpdate("INSERT INTO Stock(aid, symbol, closing_price, current_price, active) "
            //     + "VALUES (1, 'SKB', 40.00, 40.00, '1'), "
            //     + "(2, 'SMD', 71.00, 71.00, '1'), "
            //     + "(3, 'STC', 32.50, 32.50, '1')");

            // createTable.executeUpdate("INSERT INTO Market_Account(tid, aid, balance) "
            //     + "VALUES (1022, 001,10000), "
            //     + "(3045,002,100000), "
            //     + "(2034,003,50000), "
            //     + "(4093,004,45000), "
            //     + "(1234,005,200000), "
            //     + "(8956,006,5000), "
            //     + "(2341,007,2000), "
            //     + "(0456,008,10000), "
            //     + "(3455,009,130200), "
            //     + "(1123,010,35000), "
            //     + "(3306,011,30500)");

            // createTable.executeUpdate("INSERT INTO Stock_Account(uid, num_shares, sid) "
            //     + "VALUES (2, 100, 1), "
            //     + "(3, 500, 2), "
            //     + "(3, 100, 3), "
            //     + "(4, 250, 3), "
            //     + "(5, 100, 1), "
            //     + "(5, 500, 2), "
            //     + "(5, 50, 3), "
            //     + "(6, 1000, 2), "
            //     + "(7, 100, 1), "
            //     + "(8, 300, 2), "
            //     + "(9, 500, 1), "
            //     + "(9, 100, 3), "
            //     + "(9, 200, 2), "
            //     + "(10, 1000, 1), "
            //     + "(11, 100, 1), "
            //     + "(11, 100, 2), "
            //     + "(11, 100, 3), "
            //     + "(12, 100, 1), "
            //     + "(12, 200, 3), "
            //     + "(12, 100, 2)");

            
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

    public static ResultSet updateDB(String sql) {
        ResultSet rset = null;

        return rset;
    }

    public static ResultSet queryDB(String sql) {
        ResultSet rset = null;

        return rset;
    }

    public static Integer createAccount(int taxId, String usrname, String pswd, String cName,
                            String cstate, String phoneNum, String cEmail, String SSN){
        Integer success = -1;
        String sql = "";

        System.out.println("Creating account in database");    
        sql = "INSERT INTO Customer (tid, username, password, cname, "
              + "state, phonenumber, email, ssn)"
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

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


        if (success==1) {
            String ownsStockSql = "";
            String ownsMarketSql = "";
            ownsStockSql = "INSERT INTO Stock_Account (username)"
                + "VALUES (?);";
            ownsMarketSql = "INSERT INTO Market_Account (username, mbalance)"
                + "VALUES (?, ?);";


            try {
                connect();

                // Create new entry in ownsStock, creating a new stock account
                PreparedStatement prepstmt = conn.prepareStatement(ownsStockSql);
                prepstmt.setString(1, usrname);
                prepstmt.executeUpdate();

                // Create new entry in ownsMarket with $1,000 balance
                prepstmt = conn.prepareStatement(ownsMarketSql);
                prepstmt.setString(1, usrname);
                prepstmt.setFloat(2, 1000);
                prepstmt.executeUpdate();

            } catch(SQLException se) {
                se.printStackTrace();
            } finally {
                close();
            }

        }
        return success;
    }
}


