package ui;

import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import model.ExpenseEntry;
import model.ExpenseTracker;
import model.User;

// A expense tracker application
// - display ALL expense entries or display specific category expense entries
public class ExpenseTrackerApp {

    private User user;
    private ExpenseTracker expenseTracker;
    private Scanner scanner;
    private boolean activeProgram;

    // EFFECTS: creates an instance of the ExpenseTrackerApp console ui application
    public ExpenseTrackerApp() {
        initializeApp();
        displayLongDivider();
        System.out.println("Welcome to your Personal Expense Tracker app!");
        displayShortDivider();
        handlePromptExpenseLimit();
        while (this.activeProgram) {
            displayMainMenu();
            String userInput = this.scanner.nextLine();
            handleMainMenu(userInput);
        }
    }

    // EFFECTS: initialize the expense tracker app
    public void initializeApp() {
        this.user = new User();
        this.expenseTracker = new ExpenseTracker();
        this.scanner = new Scanner(System.in);
        this.activeProgram = true;
    }

    public void handleMainMenu(String userInput) {
        switch (userInput) {
            case "w":
                handlePromptAddExpenseEntry();
                break;
            case "e":
                // edit
                break;
            case "r":
                handlePromptDeleteExpenseEntry();
                break;
            case "t":
                displayExpenseEntriesAll();
                break;
            case "y":
                displayExpenseEntriesFromCategory();
                break;
            case "u":
                displayUserExpenseAmountInformation();
                break;
            case "i":
                break;
            case "q":
                exitProgram();
                break;
            default:
                displayLongDivider();
                System.out.println("Invalid choice! Please choose from the options menu!");
                displayLongDivider();
        }
    }

    public void handlePromptAddExpenseEntry() {

        displayLongDivider();
        System.out.println("\nADD EXPENSE ENTRY");
        displayShortDivider();

        // name
        System.out.println("\nEnter Expense Entry Name:");
        String name = this.scanner.nextLine();

        // category
        System.out.println("\nEnter Expense Category ");
        System.out.println("\n[Transportation , Bills, Food, Education, Entertainment, Sport]");
        String category = this.scanner.nextLine();

        // expense amount
        System.out.println("\nEnter Expense amount");
        double expenseAmount = this.scanner.nextDouble();
        this.scanner.nextLine();
        while (expenseAmount < 0){
            System.out.println("No Negative expense amount!");
            System.out.println("\nEnter Expense amount again!");
            expenseAmount = this.scanner.nextDouble();
            this.scanner.nextLine();
        }

        // note
        System.out.println("\nEnter additional notes for this entry");
        String note = this.scanner.nextLine();

        // date 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validTimeFormat = false;
        LocalDate date = null;

        while(!validTimeFormat){
            try {
                System.out.println("\nEnter the date yyyy-mm-dd");
                String dateStr = this.scanner.nextLine();  
                date = LocalDate.parse(dateStr, formatter);  
                validTimeFormat = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format!!");
            }
        }

        ExpenseEntry newEntry = new ExpenseEntry(name, category, expenseAmount, note, date);
    
        expenseTracker.addExpenseEntry(newEntry);
        user.addToTotalExpenseAmount(expenseAmount);
        displayLongDivider();
        System.out.println("\nNew Expense Entry has been created!");
        displayLongDivider();
    }

    public void handlePromptDeleteExpenseEntry() {
        int id;
        displayLongDivider();
        System.out.println("Delete Expense Entry: ");
        displayShortDivider();
        System.out.println("Enter the expense entry id that you want to delete from the list: ");
        id = this.scanner.nextInt();
        this.scanner.nextLine();
        if (expenseTracker.findExpenseEntry(id) != null) {
            expenseTracker.deleteExpenseEntry(id);
        } else {
            System.out.println("invalid ID! No entries deleted!");
        } 

    }

    public void handlePromptExpenseLimit() {
        System.out.println("Please enter your expense limit per month\n");
        double limit = this.scanner.nextDouble();
        while(limit < 0) {
            System.out.println("No Negative expense limit! Please enter your expense limit per month\n");
            limit = this.scanner.nextDouble();
        }
        user.setExpenseLimitPerMonth(limit);
        this.scanner.nextLine();
        System.out.println("\nYou have set your expense limit per month to: $" + user.getExpenseLimitPerMonth());
    }

    public void displayExpenseEntriesFromCategory() {
        if (!expenseTracker.getListOfExpenseEntries().isEmpty()) {
            displayLongDivider();
            System.out.println("\nEnter the following Categories:");
            System.out.println("Transportation , Bills, Food, Education, Entertainment, Sport");
            displayLongDivider();
            String category = this.scanner.nextLine();

            System.out.println("\nView " + category + " category");
            displayLongDivider();
            for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
                if (entry.getCategory().equals(category)) {
                    displayExpenseEntry(entry);
                }
            }
        } else {
            System.out.println("There are no expense entries!");
        }
    }

    public void displayExpenseEntriesAll() {
        if (!expenseTracker.getListOfExpenseEntries().isEmpty()) {
            for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
                displayExpenseEntry(entry);
            }
        } else {
            displayLongDivider();
            System.out.println("There are no expense entries!");
            displayLongDivider();
        }

    }

    public void displayUserExpenseAmountInformation() {
        double limit = user.getExpenseLimitPerMonth();
        double totalExpense = user.getTotalExpenseAmount(); 
        displayLongDivider();
        System.out.println("User expense limit / amunt details:");
        displayShortDivider();
        System.out.println("Your expense limit per month : $" + limit);
        if (user.getOverExpenseLimit()) {
            System.out.println("You have reached / reached over your expense limit for the month!");
        }
        System.out.println("You have remaining $ " +  (limit - totalExpense) + " to expense!");
        System.out.println("Your total expense amount : $ " + totalExpense);
        displayLongDivider();
    }

    public void exitProgram() {
        displayLongDivider();
        System.out.println("You have exited out the app!");
        System.out.println("Come back later to keep track of your expenses!");
        displayLongDivider();
        this.activeProgram = false;
    }

    public void displayLongDivider() {
        System.out.println("===================================================");
    }

    public void displayShortDivider() {
        System.out.println("---------------------------");
    }

    public void displayExpenseEntry(ExpenseEntry entry) {
        displayLongDivider();
        System.out.println("Expense Entry Details:");
        displayShortDivider();

        System.out.println("Name: " + entry.getName());
        System.out.println("Id: " + entry.getId());
        System.out.println("Expense Amount: " + "$" + entry.getExpenseAmount());
        System.out.println("Category: " + entry.getCategory());
        System.out.println("Additional Note: " + entry.getNote());
        System.out.println("Date: " + entry.getDate());
        displayLongDivider();
    }

    // EFFECTS: displays a list of commands that can be used in the main menu
    public void displayMainMenu() {
        displayLongDivider();
        System.out.println("Main Menu");
        displayShortDivider();

        System.out.println("w: Add an expense entry");
        System.out.println("e: Edit an expense entry");
        System.out.println("r: Delete an expense entry");
        System.out.println("t: View all expense entries");
        System.out.println("y: View expense entry from specific category");
        System.out.println("u: View user expense limit/amount details");
        System.out.println("i: Change expense limit per month");
        System.out.println("q: quit application");
        displayLongDivider();
    }

}
