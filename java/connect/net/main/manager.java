package net.main;

import java.util.Scanner;

public class Manager {

	static void ManagerLogin() {
        Scanner scn = new Scanner(System.in);
        int attempts = 3;

		System.out.println("\n         Manager Login           ");
        System.out.println("---------------------------------");
        System.out.println("Enter your manager credentials\n");

        System.out.print("Username: ");
		String managerUsername = scn.nextLine();
		System.out.print("Password: ");
		String managerPassword = scn.nextLine();

        Account acc = interfDB.getAccount(true, managerUsername, managerPassword);

        while (acc.getTid() == -1) {
            System.out.println("Incorrect Manager credentials.");
            System.out.println("Try again. Attempts left: " + (attempts) + "\n");
            System.out.print("Username: ");
            managerUsername = scn.nextLine();
            System.out.print("Password: ");
            managerPassword = scn.nextLine();

			acc = interfDB.getAccount(true, managerUsername, managerPassword);

            if (acc.getTid() == -1) {   attempts--;    } 

            if (attempts == 0) {
                System.out.println("Too many attempts. Closing program.");
                System.exit(0);
            }
        }

		openManagerInt(managerUsername, managerPassword);
	}

    private static void openManagerInt(String username, String password) {
        Scanner scn = new Scanner(System.in);

        Account account = interfDB.getAccount(true, username, password);

        System.out.println("\n||         Manager Interface           ||");
        System.out.println("-----------------------------------------");
        System.out.println("Welcome " + account.getName() + "!\n");
		System.out.println("\n" + interfDB.getCurrentDate());

		while(true){
			System.out.println("\nWhat would you like to do today?\n");
			System.out.println("1. Add interest");
			System.out.println("2. Generate Monthly Statement");
			System.out.println("3. List Active Customers");
			System.out.println("4. Generate Government Drug & Tax Evasion Report(DTER)");
			System.out.println("5. Generate Customer Report");
			System.out.println("6. Delete Transactions");
			System.out.println("7. Open the Market");
			System.out.println("8. Close the Market");
			System.out.println("9. Set New Stock Price");
			System.out.println("10. Set date");
			System.out.println("11. Log out");
			System.out.print("\nEnter number corresponding to the option desired: ");

            //check for non-int input
			if(!scn.hasNextInt())
			{
				System.out.println();
				System.out.println("Error! Invalid Input!");
				scn.nextLine();
				continue;
			}

			else
			{
				int choice = scn.nextInt();

                if(choice < 1 || choice > 11)
				{
					System.out.println("Invalid input. Please choose one of the 7 options below.");
					continue;
				}

				//switch on choice
				switch(choice)
				{
					case 1: showAddInterest();
							break;
					case 2: showMonthlyStatement();
							break;
					case 3: showListActiveCustomers();
							break;
					case 4: showGovTaxReport();
							break;
					case 5: showCustomerReport();
							break;
					case 6: showStockTransactions();
							break;
					case 7: goOpenMarket();
							break;
					case 8: goCloseMarket();
							break;
					case 9: showChangeStockPrice();
							break;
					case 10: setNewDate();
							break;
					default: return;
				}
			}
		}

	}

    private static void showChangeStockPrice() {
		Scanner s = new Scanner(System.in);
		System.out.println("Available Stocks and Pricing");
        System.out.println("------------------------------------");
        Stocks.showStocksWPrices();
        System.out.print("\nWhich stock would you like to change? ");
        String sym = s.nextLine().toUpperCase();
		System.out.print("What would you like the new price to be? ");
		Double newprice = s.nextDouble();
		int success = Stocks.changeStockPrice(sym, newprice);
		Double p = Stocks.getStockPrice(sym);
		if (success == 1){
			System.out.println("Stock " + sym + " successfully changed to " + p);
		}
	}

	//Manager Funcions
    private static void setNewDate() {
        Scanner scn = new Scanner(System.in);

        System.out.println("Current date: " + interfDB.getCurrentDate());

        System.out.println("Please enter desired new date,");
		System.out.print("Month: ");
		String month = scn.nextLine();
		System.out.print("Day: " );
		String day = scn.nextLine();
		System.out.print("Year: ");
        String year = scn.nextLine();
		
		String date = month + "-" + day + "-" + year;
		interfDB.changeDate(date);
		scn.close();
    }

    private static void goOpenMarket() {
		if(interfDB.isMarketOpen()){
			System.out.println("Market is already open.");
		}
		else {
			interfDB.openMarket();
			System.out.println("Market has been open by request.");

		}
    }

	private static void goCloseMarket() {
		if(!interfDB.isMarketOpen()){
			System.out.println("Market is already closed.");
		}
		else{
			interfDB.closeMarket();
			System.out.println("Market has been closed by request.");
		}
	}

	private static void showStockTransactions(){

	}

	private static void showCurrentStockPrice(){

	}

    private static void showCustomerReport() {
    }

    private static void showGovTaxReport() {
    }

    private static void showListActiveCustomers() {
    }

    private static void showMonthlyStatement() {
    }
    
    private static void showAddInterest() {
		System.out.println("Adding monthly interest to all account balances...");
		addInterest();
		System.out.println("Done!\n");
    }

    private static void addInterest() {
	}
}
