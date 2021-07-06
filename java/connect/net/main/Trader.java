package net.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trader {

    
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

    public static void CustomerLogin() {
        Scanner scn = new Scanner(System.in);
        boolean loginFail = true;
        int attempts = 4;

        System.out.println("\n         Customer Login           ");
        System.out.println("---------------------------------");

        System.out.print("Are you signed up? (yes/no)  ");

        while(loginFail){
            String resp = scn.nextLine();

            if (resp.toLowerCase().equals("yes")) {
                System.out.println("\nGreat. Enter the following information for your account");
                System.out.print("Username: ");
                String customerUsername = scn.nextLine();
                System.out.print("Password: ");
                String customerPassword = scn.nextLine();

                Account acc = interfDB.getAccount(false, customerUsername, customerPassword);

                while (acc.getTid() == -1){
                    System.out.println("Incorrect username or password.");
                    System.out.println("Try again. Attempts left: " + (attempts) + "\n");
                    System.out.print("username: ");
                    customerUsername = scn.nextLine();
                    System.out.print("password: ");
                    customerPassword = scn.nextLine();

                    acc = interfDB.getAccount(false ,customerUsername, customerPassword);
                    
                    if (acc.getTid() == -1){    attempts--;     }

                    if (attempts == 0){
                        System.out.println("Too many attempts. Closing program.");
                        System.exit(0);
                    }
                }
                Trader.openTradersInt(customerUsername, customerPassword);
            }

            else if (resp.toLowerCase().equals("no")) {
                loginFail = false;

                System.out.print("\nWould you like to sign up as a new customer? (yes/no)  ");
                
                String r = scn.next();

                if (r.toLowerCase().equals("yes")) {
                    createNewAccount();
                }
                else {
                    System.out.println("\nHave a nice day! (: ");
                    System.exit(1);
                }
            }

            else {
                System.out.println("\nPlease enter either yes or no,");
                System.out.print("Are you signed up?  ");
            }
        }
    }

    private static void createNewAccount(){
        Scanner scn = new Scanner(System.in);
        Integer taxid = 0;
        String []names = new String[2];

        System.out.println("\nYou must deposit $1,000 to open a market account.");
        System.out.print("Press enter if you agree, if not enter any key.");
        String promptThousand = scn.nextLine();
        if (!promptThousand.equals("")) {
            System.out.println("\nWe hate to see you leave ): \nThank you for considering Stars R' Us!");
            System.exit(1);
        }

        System.out.println("\nPlease enter the following fields to create an account");
        System.out.print("Username: ");
        String username = scn.nextLine();
        while(usernameExist(username)){
            System.out.print("Account with this username already exists.\nEnter unique username: ");
            username = scn.nextLine();
        }

        System.out.print("Password: ");
        String password = scn.nextLine();
        
        System.out.print("First Name: ");
        names[0] = scn.nextLine();
        while(!isAlpha(names[0])){
            System.out.println("Enter name with only letters.");
            System.out.print("First Name: ");
            names[0] = scn.nextLine();
        }

        System.out.print("Last Name: ");
        names[1] = scn.nextLine();
        while(!isAlpha(names[1])){
            System.out.println("Enter name with only letters.");
            System.out.print("Last Name: ");
            names[1] = scn.nextLine();
        }
        String name = names[0].substring(0, 1).toUpperCase() + names[0].substring(1) + " " 
                    + names[1].substring(0, 1).toUpperCase() + names[1].substring(1);

        System.out.print("State you reside in (i.e. CA,NV,etc.): ");
        String state = scn.nextLine().toUpperCase();
        while(!validState(state)){
            System.out.print("Invalid.\nPlease enter 2-letter State code: ");
            state = scn.nextLine();
        }

        System.out.print("Phone number (xxx)xxxxxxx : ");
        String phone = scn.nextLine();
        while(!validPhoneNum(phone)){
            System.out.print("Invalid.\nEnter number in valid format (xxx)xxxxxxx: ");
            phone = scn.nextLine();
        }

        System.out.print("Email address: ");
        String email = scn.nextLine();
        while(!validEmail(email)){
            System.out.println("Invalid email address. Please enter a valid one.");
            System.out.print("Email address: ");
            email = scn.nextLine();
        }

        System.out.print("ssn (xxx-xx-xxxx): ");
        String SSN = scn.nextLine();
        while(!validSSN(SSN)){
            System.out.println("Please enter a valid ssn.");
            System.out.print("ssn: ");
            SSN = scn.nextLine();
        }
        
        if (ssnExists(SSN)){
            System.out.println("Account with this SSN already exists.");
            System.out.println("Restart program and try logging in to existing account.");
            System.exit(1);
        }

        boolean valid = false;
        boolean exists = false;
        System.out.print("4-digit TaxID: ");

        do {
            String taxId = scn.nextLine();

            try {
                taxid = Integer.parseInt(taxId);
                if(taxid >= 10000){
                    valid = false;
                    System.out.println("Invalid. Only input 4 digits.");
                    System.out.print("TaxID: ");
                }else{  valid = true; }

                if(tidExists(taxid)){
                    exists = true;
                    System.out.println("TaxID exists for other account.");
                    System.out.print("Enter unique TaxID: ");
                }else{
                    exists = false;
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter only numerical inputs.\nEnter valid TaxID.");
            }

        } while (taxid == null || !valid || exists);
        
        System.out.println("");

        Integer successful  = interfDB.createAccount(taxid, username, password, name, state, phone, email, SSN);

        if (successful == 1) {
            System.out.println("");
            System.out.println("Account successfully created for " + username + "!\n");
            System.out.println("Adding $1000 into your market account.");
            System.out.println("Getting banking information...\n");
            String aid = Market.addMarketAcc(taxid);

            System.out.println("Market account created!\nAccount ID is "+ aid + ", balance at $1000.00");
            System.out.println("We'll go ahead and log you in and take you to the traders interface..\n\n");
            Trader.openTradersInt(username, password);
        } else {
            System.out.println("");
            System.out.println("Customer account creation has failed for username: " + username);
            System.out.print("Would you like to try again? (yes/no)  ");
            String s = scn.nextLine();

            if (s.toLowerCase() == "yes"){
                System.out.println("\nOkay, lets try this again\n");;
                createNewAccount();
            } else {
                System.out.println("\nHave a nice day!");
                System.exit(1);
            }
        }
        scn.close();
    }

    /*      Validate Account Inputs     */

    private static boolean validState(String state) {
        if (state.length()!=2 || !isAlpha(state)){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private static boolean validPhoneNum(String phone) {
        Pattern pattern = Pattern.compile("^(\\(\\d{3}\\))?\\d{3}?\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private static boolean validSSN(String ssn) {
        String regex = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ssn);
        return matcher.matches();
    }

    public static boolean validEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
     }

    /*      Check for Existing Fields     */

    private static boolean ssnExists(String ssn) {
        String sql = "SELECT * FROM Customers WHERE ssn =?";
        boolean exists = false;

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, ssn);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()){
                exists = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

        return exists;
    }

    private static boolean tidExists(Integer taxid) {
        String sql = "SELECT * FROM Customers WHERE tid =?";
        boolean exists = false;

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setInt(1, taxid);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()){
                exists = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

        return exists;
    }

    private static boolean usernameExist(String username) {
        String sql = "SELECT * FROM Customers WHERE username =?";
        boolean exists = false;

        try{
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, username);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()){
                exists = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close();
        }

        return exists;
    }


    /*           Main Traders Interface             */
    static void openTradersInt(String username, String password) {
        String input = "";
        Integer choice = -1;
        Account acc = interfDB.getAccount(false, username, password);
        Integer taxID = acc.getTid();
        Scanner scn = new Scanner(System.in);
        Double balance = 0.0;

        System.out.println("\n||         Traders Interface           ||");
        System.out.println("-----------------------------------------");
		System.out.println("Welcome " + acc.getName() + "!");
        System.out.println("Current date: " + interfDB.getCurrentDate());
        boolean isMarketOpen = interfDB.isMarketOpen();

        if (!isMarketOpen){
            System.out.println("The market is closed. No buying or selling of stocks is allowed.\n");
        }

		while((isMarketOpen && choice != 9) || (!isMarketOpen && choice != 7))
		{
            choice = 11;
			if(isMarketOpen)
			{
				System.out.println("\nWhat would you like to do?");
                System.out.println("---------------------------------");
				System.out.println("1. Deposit into Market Account");
				System.out.println("2. Withdraw from Market Account");
				System.out.println("3. Buy Stocks");
				System.out.println("4. Sell Stocks");
				System.out.println("5. Show Market Account Balance");
				System.out.println("6. Show Stock Transaction History");
				System.out.println("7. List Current Stock Price and Actor Profile");
				System.out.println("8. List Movie Information");
				System.out.println("9. Log out");
			}
			else
			{
				//if market is closed
				System.out.println();
				System.out.println("nWhat would you like to do?");
                System.out.println("---------------------------------");
				System.out.println("1. Deposit into Market Account");
				System.out.println("2. Withdraw from Market Account");
				System.out.println("3. Show Market Account Balance");
				System.out.println("4. Show Stock Transaction History");
				System.out.println("5. List Current Stock Price and Actor Profile");
				System.out.println("6. List Movie Information");
				System.out.println("7. Log out");
			}

            do {
                System.out.print("\nEnter number corresponding to the option desired: ");
                input = scn.nextLine();

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    if(isMarketOpen){
                        System.out.println("Please enter a number between 1 and 9\n");
                    }else {
                        System.out.println("Please enter a number between 1 and 7\n");
                    }
                }
    
            } while (input == null || (isMarketOpen && (choice < 1 || choice > 9)) || (!isMarketOpen && (choice < 1 || choice > 7)) );


				
            if(isMarketOpen)
            {
                //switch on choice
                switch(choice)
                {
                    case 1: showDepositOrWithdraw(0,taxID);
                            balance = interfDB.getBalance(taxID);
                            System.out.println("Balance is now: " + balance);
                            break;
                    case 2: showDepositOrWithdraw(1,taxID);
                            balance = interfDB.getBalance(taxID);
                            System.out.println("Balance is now: " + balance);
                            break;
                    case 3: showBuy(taxID);
                            break;
                    case 4: showSell(taxID);
                            break;
                    case 5: showMarketBalance(taxID);
                            break;
                    case 6: showStockTransactions(taxID);
                            break;
                    case 7: showCurrentStockPrice();
                            break;
                    case 8: showMovieInfo();
                            break;
                    case 9: System.out.println("\nHave a great day!");
                            scn.close();
                            System.exit(1);
                    default: return;
                }
            } else {
                //switch on choice
                switch(choice) {
                    case 1: showDepositOrWithdraw(0,taxID);
                            balance = interfDB.getBalance(taxID);
                            System.out.println("Balance is now: $" + String.format("%.2f", balance));
                            break;
                    case 2: showDepositOrWithdraw(1,taxID); 
                            balance = interfDB.getBalance(taxID);
                            System.out.println("Balance is now: $" + String.format("%.2f", balance));
                            break;
                    case 3: showMarketBalance(taxID);
                            break;
                    case 4: showStockTransactions(taxID);
                            break;
                    case 5: showCurrentStockPrice();
                            break;
                    case 6: showMovieInfo();
                            break;
                    case 7:  System.out.println("\nHave a great day!");
                            scn.close();
                            System.exit(1);
                    default: return;
                }
			}
		}
    }

    
    /*          Functions for Users Choices         */ 
    private static void showDepositOrWithdraw(int i, int tid) {
        Scanner scn = new Scanner(System.in);
        String resp = "";
        Double bal = interfDB.getBalance(tid);

        if (i == 0) {
            System.out.print("How much would you like to deposit: ");
            String s = scn.nextLine();
            Double amount = Double.parseDouble(s);
            Market.addToMarketBalance(amount, tid);
        }
        else {
            System.out.print("How much would you like to withdraw: ");
            String s = scn.nextLine();
            Double amount = Double.parseDouble(s);
            if (bal - amount <= 0){
                System.out.println("Withdrawal takes account balance to a negative balance or ZERO");
                System.out.print("Would you like to withdraw remaining account balance of " + String.format("$%.2f", bal) + "? ");
                resp = scn.nextLine();
                if (resp.toLowerCase().equals("yes")) {
                    Market.subToMarketBalance(bal, tid);
                }else{
                    System.out.println("Withdrawal has been canceled.\nBalance remains the same.");
                }
            }else{
                Market.subToMarketBalance(amount, tid);
            }
        }
    }

    private static void showBuy(int tid) {
        Scanner s = new Scanner(System.in);
        if (interfDB.getBalance(tid) == 0.0){
            System.out.println("Current account balance is at $0.00.\nMust deposit into account to purchase any stocks.");
            return;
        }

        System.out.println("Available Stocks and Pricing");
        System.out.println("------------------------------------");
        Stocks.showStocksWPrices();
        System.out.print("\nWhich stock would you like to purchase? ");
        String buySym = s.nextLine().toUpperCase();
        if(Stocks.getStockPrice(buySym) == -1.0){
            System.out.println("Invalid choice.");
            System.out.print("Stock: " + buySym + " does not exist.");
            return;
        }
        System.out.print("How many shares? ");
        int shares = s.nextInt();
        Stocks.buyStock(buySym, tid, shares);
    }

    private static void showSell(int tid) {
        Scanner s = new Scanner(System.in);

        System.out.println("Your current stock holdings");
        System.out.println("-------------------------------");
        boolean isEmpty = Stocks.showStocksOwned(tid);
            if (!isEmpty){
            System.out.print("\nWhich stock would you like to sell? ");
            String sellSym = s.nextLine().toUpperCase();
            while(Stocks.getStockPurchasePrice(sellSym, tid) == -1.0){
                System.out.println("Invalid choice.");
                System.out.print("Enter an available stock option: ");
                sellSym = s.nextLine().toUpperCase();
            }
            System.out.print("How many of your total shares do you want to sell? ");
            int shares = s.nextInt();
            Stocks.sellStock(sellSym, tid, shares);
        }
    }

    private static void showMarketBalance(int tid) {
        double balance = interfDB.getBalance(tid); 
        System.out.println("Your current market balance is $" + String.format("%.2f", balance));
    }

    private static void showStockTransactions(int tid) {
        System.out.println("Transactions for the month of " + Date.getMonth());
        System.out.println("--------------------------------------");
        Transactions.showTransactionHistory(tid);
    }
    
    private static void showCurrentStockPrice() {
        System.out.println("Which stock would you like to learn more about? ");
        Stocks.showStocks();
        Scanner s = new Scanner(System.in);
        String sym = s.nextLine().toUpperCase();
        Double price = Stocks.getStockPrice(sym);
        System.out.println("Price for stock " + sym + " is currently $" + String.format("%.2f", price));
        showActorInfo(sym);
    }
    
    private static void showActorInfo(String sym) {
        String sql = "SELECT * FROM Actors WHERE sym=?";
        try {
            connect();
            PreparedStatement prepstmt = conn.prepareStatement(sql);
            prepstmt.setString(1, sym);
            ResultSet rs = prepstmt.executeQuery();

            if(rs.next()){
                String name = rs.getString("aname");
                String role = rs.getString("role");
                String dob = rs.getString("dob");
                String title = rs.getString("title");
                Integer value = rs.getInt("value");
                Integer year = rs.getInt("year_released");
                System.out.println("This stock is for " + name + ", born on " + dob + ", paid \n$" + value  
                + " as a/an " + role + " in the movie " + title + " (" + year + ")");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            close();
        }
    }

    private static void showMovieInfo() {
        System.out.println("Movies our stock actors have been in:");
        
        String sql = "SELECT title, year_released  FROM Actors";

        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql); 

            while(rs.next()){
                System.out.println(rs.getString("title") + " (" + rs.getString("year_released") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}