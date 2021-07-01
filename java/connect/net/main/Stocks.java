package net.main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stocks {
        
        public static double getStockPrice(String sym) throws SQLException{
        String sql =  "SELECT A.currentPrice " +
                        "FROM ActorsStockInfo A " +
                        "WHERE A.sym = " + "'" + sym + "'";

        ResultSet resultSet = interfDB.queryDB(sql);
        double res = -1.0;

        try{
            if(!resultSet.next()){
                return -1.0;
            }

            res = resultSet.getDouble("curr_Price");
        } catch(Exception e){
            e.printStackTrace();
        }

        return res;
    }
    
    public static void changeStockPrice(String sym, double price) throws SQLException{
        String UPDATE = "UPDATE ActorsStockInfo "
                        + "SET CURRENTPRICE = " + price + " "
                        + "WHERE ACTORID = " + "'" + sym + "'";
        interfDB.updateDB(UPDATE);
    }
}