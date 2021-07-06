package net.main;

import java.util.List;
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
			System.out.println("\nWhat would you like to do?\n");
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
					System.out.println("Invalid input. Please choose one of the 11 options.");
					continue;
				}

				//switch on choice
				switch(choice)
				{
					case 1: showAddInterest(account.getTid());
							break;
					case 2: showMonthlyStatement();
							break;
					case 3: showListActiveCustomers();
							break;
					case 4: showDTER();
							break;
					case 5: showCustomerReport();
							break;
					case 6: deleteTransactions();
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
	
	/*			Manager Functional Options		*/
	
	private static void showAddInterest(int mtid) {
		System.out.println("Adding monthly interest to all account balances...");
		Market.addInterest(mtid);
		System.out.println("Done!\n");
    }

	private static void showMonthlyStatement() {
		Scanner s = new Scanner(System.in);
		List<Integer> tids = Market.getTids();
		String name = "";

		System.out.println(" Customers of Stars R' Us");
		System.out.println("--------------------------");
		for (Integer tid : tids) {
			name = interfDB.getName(tid);
			System.out.println(tid + "   " + name);
		}

		System.out.print("\nWhich account would you like to generate statement for?\nEnter tid: ");
		Integer t = s.nextInt();
		System.out.println("");

		name = interfDB.getName(t);
		String email = interfDB.getEmail(t);
		String phone = interfDB.getPhone(t);
		String st = interfDB.getStateOfResidency(t);

		if(name.equals("")){
			System.out.println("No account with tid " + t);

		} else{
			System.out.println(name + "        " + st);
			System.out.println(email + "   " + phone);
			System.out.println("");
			Transactions.showTransactionHistory(t);
			Double totalEarnings = Transactions.getTotalProfit(t) + Transactions.getInterestCharged(t);
			Double init = Market.getInitialBalance(t);
			System.out.println("\nInitial balance: " + String.format("$%.2f", init));
			System.out.println("End balance: " + String.format("$%.2f", interfDB.getBalance(t)));
			System.out.println("Total Earnings: " + String.format("$%.2f", totalEarnings));
			System.out.println("Commisions paid out: " + Transactions.getCommissions(t));
		}
    }

	private static void showListActiveCustomers() {
		System.out.println("Listing Active Customers who have bought/sold 1000+ shares");
		System.out.println("---------------------------------------------------------------");
		interfDB.listActiveCustomers();
    }

	private static void showDTER() {
		interfDB.generateDTER();
    }

	private static void showCustomerReport() {
		Scanner s = new Scanner(System.in);
		List<Integer> tids = Market.getTids();
		String name = "";

		System.out.println(" Customers of Stars R' Us");
		System.out.println("--------------------------");
		for (Integer tid : tids) {
			name = interfDB.getName(tid);
			System.out.println(tid + "   " + name);
		}

		System.out.print("\nWhich account would you like a report for?\nEnter tid: ");
		Integer t = s.nextInt();
		System.out.println("\n      Current Stock Holdings");
        System.out.println("-----------------------------------");
		Stocks.showStocksOwned(t);
		System.out.println("\nAccount Market Balance:  "
				+ String.format("$%.2f", interfDB.getBalance(t)));

    }

	private static void deleteTransactions() {
		System.out.println("Deleting all transactions...");
		Transactions.deleteTransactions();
		System.out.println("Done.");
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

	private static void showChangeStockPrice() {
		Scanner s = new Scanner(System.in);
		System.out.println("Available Stocks and Pricing");
        System.out.println("------------------------------------");
        Stocks.showStocksWPrices();
        System.out.print("\nWhich stock would you like to change? ");
        String sym = s.nextLine().toUpperCase();
		if(Stocks.getStockPrice(sym) == -1.00){
			System.out.println("Stock " + sym + " does not exist.\nCould not change price.");
			return;
		}

		System.out.print("What would you like the new price to be? ");
		Double newprice = s.nextDouble();
		int success = Stocks.changeStockPrice(sym, newprice);
		Double newp = Stocks.getStockPrice(sym);
		if (success == 1){
			System.out.println("Stock " + sym + " successfully changed to " + String.format("$%.2f", newp));
		}
	}

    private static void setNewDate() {
        Scanner scn = new Scanner(System.in);

        System.out.println("Current date: " + interfDB.getCurrentDate());

        System.out.println("Please enter desired new date,");
		System.out.print("Month: ");
		String month = scn.nextLine();
		if (month.length() == 1){	month = "0"+month;	}
		System.out.print("Day: " );
		String day = scn.nextLine();
		if (day.length() == 1){	day = "0"+day;	}
		System.out.print("Year: ");
        String year = scn.nextLine();
		
		String date = month + "-" + day + "-" + year;
		if(!Date.validDate(date)){
			System.out.println("Invalid date. Could not apply changes.");
		}else{
			interfDB.changeDate(date);
			System.out.println("Date changed to " + interfDB.getCurrentDate() + ".");
		}
	}

}
