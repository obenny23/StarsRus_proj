package net.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    public static boolean validDate(String date) {
        // String regex = "^(0[1-9]|[12][0-9]|3[01])[- /.]$";
        // Pattern pattern = Pattern.compile(regex);
        // Matcher matcher = pattern.matcher(date);
        // return matcher.matches();
        return true;
    }
}
