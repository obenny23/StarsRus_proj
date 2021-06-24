package net.main;

import java.util.Scanner;

public class Trader {

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
                    
                    if (acc.getTid() != -1){    attempts--;     }

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

        System.out.println("no :p ");
        System.out.println("jk (:");
        System.out.println("You must deposit $1,000 into market account.");
        System.out.print("Press enter if you agree, if not enter any key.");
        String promptThousand = scn.nextLine();
        if (!promptThousand.equals("")) {
            System.out.println("We hate to see you leave ): \nThank you for considering Stars R' Us!");
            scn.close();
            return;
        }

        System.out.println("\nPlease enter the following fields to create an account");
        System.out.print("Username: ");
        String username = scn.nextLine();
        System.out.print("Password: ");
        String password = scn.nextLine();
        System.out.print("First and Last Name: ");
        String name = scn.nextLine();
        System.out.print("State you reside in (i.e. CA,NV,etc.): ");
        String state = scn.nextLine();
        System.out.print("Phone number (xxx)xxxxxxx : ");
        String phone = scn.nextLine();
        System.out.print("Email address: ");
        String email = scn.nextLine();
        System.out.print("TaxID: ");
        int taxid = scn.nextInt();
        System.out.print("ssn (xxx-xx-xxxx): ");
        String SSN = scn.nextLine();
        System.out.println("");

        Integer successful  = interfDB.createAccount(taxid, username, password, name, state, phone, email, SSN);

        if (successful == 1) {
            System.out.println("");
            System.out.println("Customer account has been created for username " + username + "\n");
            System.out.println("We'll log you in and take you to the traders interface\n\n");
            Trader.openTradersInt(username, password);
        }else if ( successful == 2){
            System.out.println("");
            System.out.println("Customer account already exists");
            System.out.println("Logging you into existing account");
        } else {
            System.out.println("");
            System.out.println("Customer account creation has failed for username " + username);
            System.out.print("Would you like to try again? (yes/no)  ");

            String s = scn.nextLine();

            if (s.toLowerCase() == "yes"){
                System.out.println("\nOkay, lets try this again\n");;
                createNewAccount();
            } else {
                System.out.println("\nHave a nice day!");
            }
        }

        scn.close();
    }

    static void openTradersInt(String username, String password) {
        String input = "";
        Integer choice = -1;
        Account acc = interfDB.getAccount(false, username, password);
        Scanner scn = new Scanner(System.in);

        System.out.println("\n||         Traders Interface           ||");
        System.out.println("-----------------------------------------");
		System.out.println("Welcome " + acc.getName() + "!\n");

        boolean isMarketOpen = isMarketOpen();

		while((isMarketOpen && choice != 9) || (!isMarketOpen && choice != 7))
		{
			if(isMarketOpen)
			{
				System.out.println("Current date: " + interfDB.getDate());
				System.out.println("What would you like to do today?\n");
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
				System.out.println("\nThe market is closed. No buying or selling of stocks is allowed.");
				System.out.println("What would you like to do today?\n");
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
                    case 1: showDepositOrWithdraw(0);
                            break;
                    case 2: showDepositOrWithdraw(1);
                            break;
                    case 3: showBuy();
                            break;
                    case 4: showSell();
                            break;
                    case 5: showMarketBalance();
                            break;
                    case 6: showStockTransactions();
                            break;
                    case 7: showCurrentStockPrice();
                            break;
                    case 8: showMovieInfo();
                            break;
                    default: System.out.println("\nHave a great day!");
                            System.exit(1);
                }
            } else {
                //switch on choice
                switch(choice) {
                    case 1: showDepositOrWithdraw(0);
                            break;
                    case 2: showDepositOrWithdraw(1);
                            break;
                    case 3: showMarketBalance();
                            break;
                    case 4: showStockTransactions();
                            break;
                    case 5: showCurrentStockPrice();
                            break;
                    case 6: showMovieInfo();
                            break;
                    default:  System.out.println("\nHave a great day!");
                            System.exit(1);
                }
			}
		}
    }

    private static void showDepositOrWithdraw(int i) {
    }

    private static void showBuy() {
    }

    private static void showSell() {
    }
    
    private static void showMarketBalance() {
    }

    private static void showStockTransactions() {
    }
    
    private static void showCurrentStockPrice() {
    }
    
    private static void showMovieInfo() {
    }
    
    
    
    

    private static boolean isMarketOpen() {
        return false;
    }
}