package net.main;

// import java.util.ArrayList;

public class Date {

    public static String getMonth() {
        String date = interfDB.getCurrentDate();
        int month = Integer.parseInt(date.substring(0, 2));
        String[] months = new String[]{"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
        return months[month-1];
    }
    
    // public static ArrayList<Integer> stripDate(String date){
        // ArrayList<Integer> dArrayList = new ArrayList<Integer>();
        // return date;

    // }
}
