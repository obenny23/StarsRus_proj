package net.main;

import java.util.Scanner;

public class Manager {
    // PreparedStatement prepstmt = null;
    // ResultSet rs = null;

    private static String nextLine;

	static void ManagerLogin() {
        Scanner scn = new Scanner(System.in);
        int attempts = 3;

		System.out.println("\n         Manager Login           ");
        System.out.println("---------------------------------\n");

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

		while(true){
			// System.out.println("\n" + getCurrentDate());

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
				nextLine = scn.nextLine();

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
					case 9: showCurrentStockPrice();
							break;
					case 10: setNewDate();
							break;
					default: return;
				}
			}
		}

	}

    //Manager Funcions
    private static void setNewDate() {
        Scanner scn = new Scanner(System.in);

        System.out.println("Current date: " + getCurrentDate());

        System.out.println("Please enter desire new date in the form MM-DD-YYYY.");
        System.out.print("desired date: ");
        String date = scn.nextLine();

		interfDB.changeDate(date);
		scn.close();
    }

    private static void goOpenMarket() {
        interfDB.openMarket();
    }

	private static void goCloseMarket() {

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
    }

    private static String getCurrentDate() {
        String date = "06-09-2021";
        return date;
    }
}
