package net.main;
import java.sql.*;

public class Populate {

    public static void createDB()
    {

        Connection connection   = null;

        try {
            String url = "jdbc:sqlite:C:/Users/obenn/Desktop/sqlite/sqlite-tools-win32-x86-3350500/StarsRus_proj/db/starsrus.db";

            connection = DriverManager.getConnection (url);  

            /*          Drop All Tables         */

            try {
                Statement dropTable = connection.createStatement ();
                dropTable.executeUpdate("DROP TABLE Customers");
                dropTable.executeUpdate("DROP TABLE Managers");
                dropTable.executeUpdate("DROP TABLE Stocks");
                dropTable.executeUpdate("DROP TABLE Markets");
                dropTable.executeUpdate("DROP TABLE Actors");
                dropTable.executeUpdate("DROP TABLE StockTransactions");
                dropTable.executeUpdate("DROP TABLE MarketTransactions");
                dropTable.executeUpdate("DROP TABLE AccruedInterest");
                // dropTable.executeUpdate("DROP TABLE StocksHistory");
                dropTable.executeUpdate("DROP TABLE Date");
                }
            catch (SQLException e) {
            }

            
            Statement createTable = connection.createStatement ();

            /*  Create Customer Table and Populate */ 
            
            createTable.executeUpdate ("CREATE TABLE Customers "
                + " (tid INTEGER UNIQUE, username NNVARCHAR(255) PRIMARY KEY, "
                + " password NNVARCHAR(255), cname NVARCHAR(100), state char(2), "
                + " phonenumber NVARCHAR(11), email NVARCHAR(64), ssn NVARCHAR(11)) "); 

            createTable.executeUpdate("INSERT INTO Customers (cname, username, password, state, phonenumber, email, tid, ssn) "
            + "VALUES  ('Alfred Hitchcock','alfred','hi','CA','(805)2574499', 'alfred@hotmail.com',1022,'606-76-7900'), "
            + "('Billy Clinton','billy','cl','CA','(805)5629999','billy@yahoo.com',3045,'606-76-7903'), "
            + "('Cindy Laugher','cindy','la','CA','(805)6930011','cindy@hotmail.com',2034,'606-70-7900'), "
            + "('David Copperfill','david','co','CA','(805)8240011','david@yahoo.com',4093,'506-78-7900'), "
            + "('Elizabeth Sailor','sailor','sa','CA','(805)1234567','sailor@hotmail.com',1234,'436-76-7900'), "
            + "('George Brush','brush','br','CA','(805)1357999','george@hotmail.com',8956,'632-45-7900'), "
            + "('Ivan Stock','ivan','st','NJ','(805)3223243','ivan@yahoo.com',2341,'609-23-7900'), "
            + "('Joe Pepsi','joe','pe','CA','(805)5668123','pepsi@pepsi.com',0456,'646-76-3430'), "
            + "('Magic Jordon','magic','jo','NJ','(805)4535539','jordon@jordon.org',3455,'646-76-8843'), "
            + "('Olive Stoner','olive','st','CA','(805)2574499','olive@yahoo.com',1123,'645-34-7900'), "
            + "('Frank Olson','frank','ol','CA','(805)3456789','frank@gmail.com',3306,'345-23-2134')");

            /*  Create Manager Table and Populate */ 
            
            createTable.executeUpdate("CREATE TABLE Managers "
                + " (tid INTEGER UNIQUE, username NNVARCHAR(255) PRIMARY KEY, "
                + " password NNVARCHAR(255), cname NVARCHAR(100), state char(2), "
                + " phonenumber NVARCHAR(11), email NVARCHAR(64), ssn NVARCHAR(11)) "); 

            createTable.executeUpdate("INSERT INTO Managers (cname, username, password, state, phonenumber, email, tid, ssn) "
            + " VALUES ('John Admin','admin','secret','CA','(805)6374632','admin@stock.com',1000,'606-60-6060')");

            /*  Create Stock Accounts Table and Populate */ 

            createTable.executeUpdate("CREATE TABLE Stocks( "
                + "tid INTEGER NOT NULL, "
                + "sym NVARCHAR(3), "
                + "shares INTEGER, "
                + "price DOUBLE, "
                + "PRIMARY KEY (tid, sym),"
                + "FOREIGN KEY (tid) REFERENCES Customers(tid))");
              
            createTable.executeUpdate("INSERT INTO Stocks(tid, shares, sym, price) "
                + "VALUES ('1022',100,'SKB',40.00), "
                + "('3045',500,'SMD',71.00), "
                + "('3045',100,'STC',32.50), "
                + "('2034',250,'STC',32.50), "
                + "('4093',100,'SKB',40.00), "
                + "('4093',500,'SMD',71.00), "
                + "('4093',50,'STC',32.50), "                
                + "('1234',1000,'SMD',71.00), "
                + "('8956',100,'SKB',40.00), "
                + "('2341',300,'SMD',71.00), "
                + "('0456',500,'SKB',40.00), "
                + "('0456',100,'STC',32.50), "
                + "('0456',200,'SMD',71.00), "
                + "('3455',1000,'SKB',40.00), "
                + "('1123',100,'SKB',40.00), "
                + "('1123',100,'SMD',71.00), "
                + "('1123',100,'STC',32.50), "
                + "('3306',100,'SKB',40.00), "
                + "('3306',200,'STC',32.50), "
                + "('3306',100,'SMD',71.00)");

            /*  Create Actors Table and Populate */ 

            createTable.executeUpdate("CREATE TABLE Actors ( "
                + "sym NVARCHAR(3) PRIMARY KEY, "
                + "curr_price DOUBLE, "
                + "aname NVARCHAR(100), "
                + "dob NVARCHAR(100), "
                + "title NNVARCHAR(255) NOT NULL, "
                + "role NVARCHAR(255), "
                + "year_released INTEGER, "
                + "value REAL NOT NULL )");

            createTable.executeUpdate("INSERT INTO Actors(sym, curr_price, aname, dob, title, role, year_released, value) "
                + "VALUES ('SKB',40.00,'Kim Basinger','12-08-1958', 'L.A. Confidential', 'Actor', 1997, 5000000), "
                + "('SMD',71.00,'Michael Douglas','09-25-1944','Perfect Murder', 'Actor', 1998, 10000000), "
                + "('STC',32.50,'Tom Cruise','07-03-1962','Jerry Maguire', 'Actor', 1996, 5000000)");

            /*  Create Market Accounts Table and Populate */ 

            createTable.executeUpdate("CREATE TABLE Markets( "
                + "tid INTEGER NOT NULL, "
                + "aid NVARCHAR(3) PRIMARY KEY, "
                + "balance DOUBLE, "
                + "FOREIGN KEY (tid) REFERENCES Customers(tid) "
                + "ON UPDATE CASCADE ON DELETE CASCADE)");

               createTable.executeUpdate("INSERT INTO Markets(tid, aid, balance) "
                + "VALUES (1022, '001',10000), "
                + "(3045,'002',100000), "
                + "(2034,'003',50000), "
                + "(4093,'004',45000), "
                + "(1234,'005',200000), "
                + "(8956,'006',5000), "
                + "(2341,'007',2000), "
                + "(0456,'008',10000), "
                + "(3455,'009',130200), "
                + "(1123,'010',35000), "
                + "(3306,'011',30500)");

            /*  Create Tables for Transanctions and Accrued Interest    */ 

            createTable.executeUpdate("CREATE TABLE StockTransactions ("
                + "date NVARCHAR(8), "
                + "ctid INTEGER, "
                + "sym NVARCHAR(3), "
                + "price DOUBLE, "
                + "shares INT, "
                + "profit DOUBLE)");

            createTable.executeUpdate("CREATE TABLE MarketTransactions ("
                + "date NVARCHAR(8), "
                + "ctid INTEGER, " 
                + "amount DOUBLE, "
                + "balance DOUBLE )");

            createTable.executeUpdate("CREATE TABLE AccruedInterest ("
                + "date NVARCHAR(8), "
                + "ctid INTEGER, "
                + "mtid INTEGER, "
                + "interest DOUBLE, "
                + "balance DOUBLE )");
                
            createTable.executeUpdate("CREATE TABLE Date("
                + "date NVARCHAR(10), "
                + "open INTEGER, "
                + "time NVARCHAR(5) )");   
                
            createTable.executeUpdate("INSERT INTO Date (date, open, time) "
                + "VALUES ('06-01-2021', 1, '14:00')");

                 // createTable.executeUpdate("CREATE TABLE StocksHistory ("
            //     + "DATE varchar(8), "
            //     + "ACTORID varchar(3), "
            //     + "PRICE DOUBLE )");


            
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


}
