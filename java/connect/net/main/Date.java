package net.main;

// import java.util.ArrayList;

public class Date {

    public static String getMonth() {
        String date = interfDB.getCurrentDate();
        int month = Integer.parseInt(date.substring(0, 2));
        String[] months = new String[]{"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
        return months[month-1];
    }

    public static int getMonth(String date){
        int month = Integer.parseInt(date.substring(0, 2));
        return month;
    }

    public static int getDay(String date){
        int day = Integer.parseInt(date.substring(3, 5));
        return day;
    }

    public static int daysInMonth(int month){
        int []days = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        int am = days[month-1];
        return am;
    }
    
    // public static ArrayList<Integer> stripDate(String date){
        // ArrayList<Integer> dArrayList = new ArrayList<Integer>();
        // return date;

    // }
}
