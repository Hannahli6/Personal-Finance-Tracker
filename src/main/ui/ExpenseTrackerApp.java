package ui;

import java.time.format.DateTimeParseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import model.ExpenseEntry;
import model.ExpenseTracker;

import persistence.JsonViewer;
import persistence.JsonWriter;

// CODE ATTRIBUTE: Lab 3
// A expense tracker console ui application that displays expense tracker information and 
// allows the user to interact with  in the console for 
// adding, editing, deleting, viewing their expense entries
public class ExpenseTrackerApp {

    private static final String JSON_STORE = "./data/expensetracker.json";
    private ExpenseTracker expenseTracker;
    private Scanner scanner;
    private boolean activeProgram;
    private JsonWriter jsonWriter;
    private JsonViewer jsonReader;

    // EFFECTS: creates an instance of the ExpenseTrackerApp console ui application
    public ExpenseTrackerApp() throws FileNotFoundException {
        initializeApp();
        displayLongDivider();
        System.out.println("Welcome to your Personal Expense Tracker app!");
        displayLongDivider();
        handlePromptExpenseLimit(); // get expense limit
        while (this.activeProgram) {
            displayMainMenu();
            String userInput = this.scanner.nextLine();
            handleMainMenu(userInput);
        }
    }

    // EFFECTS: initialize the expense tracker app
    public void initializeApp() throws FileNotFoundException {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonViewer(JSON_STORE);
        this.expenseTracker = new ExpenseTracker();
        this.scanner = new Scanner(System.in);
        this.activeProgram = true;
    }

    // EFFECTS: handle main menu user input
    @SuppressWarnings("methodlength")
    public void handleMainMenu(String userInput) {
        switch (userInput) {
            case "w":
                handlePromptAddExpenseEntry();
                break;
            case "e":
                handlePromptEditExpenseEntry();
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
                handleEditExpenseLimit();
                break;
            case "o":
                handleSaveExpenseTracker();
                break;
            case "p":
                handleLoadExpenseTracker();
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

    // EFFECTS: saving the expense tracker to json file
    private void handleSaveExpenseTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseTracker);
            jsonWriter.close();
            System.out.println("SAVED!");
            System.out.println("Saved expense tracker to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loading the expense tracker from json file
    private void handleLoadExpenseTracker() {
        try {
            expenseTracker = jsonReader.read();
            System.out.println("Loaded expense tracker from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: get a list of expense entry details and adding each of those details
    // to an expense entry
    public void handlePromptAddExpenseEntry() {

        displayLongDivider();
        System.out.println("\nADD EXPENSE ENTRY");

        ArrayList<Object> entryInfoList = getEntryInfoList("");

        String name = (String) entryInfoList.get(0);
        String category = (String) entryInfoList.get(1);
        double expenseAmount = (double) entryInfoList.get(2);
        String note = (String) entryInfoList.get(3);
        LocalDate date = (LocalDate) entryInfoList.get(4);

        ExpenseEntry newEntry = new ExpenseEntry(name, category, expenseAmount, note, date);
        expenseTracker.addExpenseEntry(newEntry);

        displayLongDivider();
        System.out.println("\nNew Expense Entry has been created!");
        displayLongDivider();

    }

    // EFFECTS: ask user for entry details and returning a list of those properties
    public ArrayList<Object> getEntryInfoList(String functionalityName) {
        ArrayList<Object> entryInfoList = new ArrayList<>();
        // name
        System.out.println("\n" + functionalityName + "Enter Expense Entry Name:");
        String name = this.scanner.nextLine();
        // category
        System.out.println("\n" + functionalityName + "Enter Expense Category ");
        System.out.println("[Transportation , Bills, Food, Education, Entertainment, Sport]");
        String category = this.scanner.nextLine();
        // expense amount
        System.out.println("\n" + functionalityName + "Enter Expense amount");
        double expenseAmount = this.scanner.nextDouble();
        this.scanner.nextLine();
        // abstract this
        while (expenseAmount < 0) {
            System.out.println("No Negative expense amount! \n Enter Expense amount again!");
            expenseAmount = this.scanner.nextDouble();
            this.scanner.nextLine();
        }
        // note
        System.out.println("\n" + functionalityName + "Enter additional notes for this entry");
        String note = this.scanner.nextLine();
        // date
        LocalDate date = getDateFromPrompt();
        entryInfoList.add(name);
        entryInfoList.add(category);
        entryInfoList.add(expenseAmount);
        entryInfoList.add(note);
        entryInfoList.add(date);
        return entryInfoList;
    }

    // EFFECTS: get a new list of expense entry details that the user as edited and
    // replacing each of those details to a corresponding expense entry
    public void handlePromptEditExpenseEntry() {

        int id;
        displayLongDivider();
        System.out.println("EDIT EXPENSE ENTRY");
        displayLongDivider();
        System.out.println("Please enter the Id of the expense entry that you want to edit!");
        id = this.scanner.nextInt();
        this.scanner.nextLine();

        if (expenseTracker.findExpenseEntry(id) != null) {

            ArrayList<Object> entryInfoList = getEntryInfoList("Edit: ");
            String name = (String) entryInfoList.get(0);
            String category = (String) entryInfoList.get(1);
            double expenseAmount = (double) entryInfoList.get(2);
            String note = (String) entryInfoList.get(3);
            LocalDate date = (LocalDate) entryInfoList.get(4);

            expenseTracker.editExpenseEntry(name, category, expenseAmount, note, date, id);
            System.out.println("\nYour expense entry id:" + id + " has been edited!");

        } else {
            System.out.println("invalid ID! No entries edited!");
        }
    }

    // EFFECTS: ask user to input yyy-mm-dd format for the date,
    // if its a valid date, return date
    // otherwise ask the user again to input another date until it's valid
    public LocalDate getDateFromPrompt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validTimeFormat = false;
        LocalDate date = null;

        while (!validTimeFormat) {
            try {
                System.out.println("\nEnter the date yyyy-mm-dd");
                String dateStr = this.scanner.nextLine();
                date = LocalDate.parse(dateStr, formatter);
                validTimeFormat = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format!!");
            }
        }
        return date;
    }

    // EFFECTS: ask the user to input a valid entry id and delete the entry from the
    // list
    public void handlePromptDeleteExpenseEntry() {
        int id;
        displayLongDivider();
        System.out.println("DELETE EXPENSE ENTRY: ");
        displayLongDivider();
        System.out.println("Enter the expense entry id that you want to delete from the list: ");
        id = this.scanner.nextInt();
        this.scanner.nextLine();
        if (expenseTracker.findExpenseEntry(id) != null) {
            expenseTracker.deleteExpenseEntry(id);
        } else {
            System.out.println("invalid ID! No entries deleted!");
        }

    }

    // EFFECTS: ask the user to input the expense limit amount
    public void handlePromptExpenseLimit() {
        System.out.println("Please enter your expense limit\n");
        double limit = this.scanner.nextDouble();
        while (limit < 0) {
            System.out.println("No Negative expense limit! Please enter your expense limit\n");
            limit = this.scanner.nextDouble();
        }
        this.scanner.nextLine();
        expenseTracker.getUser().setExpenseLimit(limit);
        System.out.println("\nYou have set your expense limit to: $" + expenseTracker.getUser().getExpenseLimit());
    }

    // EFFECTS: prompt the user to edit their expense limit amount
    public void handleEditExpenseLimit() {
        displayLongDivider();
        System.out.println("EDIT EXPENSE LIMIT");
        System.out.println("Please enter a new expense limit\n");
        double limit = this.scanner.nextDouble();
        while (limit < 0) {
            System.out.println("No Negative expense limit! Please enter your expense limit\n");
            limit = this.scanner.nextDouble();
        }
        this.scanner.nextLine();
        expenseTracker.getUser().setExpenseLimit(limit);
        System.out.println("\nYou have set your new expense limit to: $" + expenseTracker.getUser().getExpenseLimit());
    }

    // EFFECTS: ask the user to input the expense category to display the list of
    // expenses from that category
    public void displayExpenseEntriesFromCategory() {
        if (!expenseTracker.getListOfExpenseEntries().isEmpty()) {
            displayLongDivider();
            System.out.println("\nEnter the following Categories:");
            System.out.println("Transportation , Bills, Food, Education, Entertainment, Sport");
            displayLongDivider();
            String category = this.scanner.nextLine();

            displayLongDivider();
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

    // EFFECTS: display all expense entries
    public void displayExpenseEntriesAll() {
        displayLongDivider();
        System.out.println("VIEW ALL EXPENSE ENTRIES");
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

    // EFFECTS: display the user's expense limit amount and total expense amount and
    // overspending warning message
    public void displayUserExpenseAmountInformation() {
        double limit = expenseTracker.getUser().getExpenseLimit();
        double totalExpense = expenseTracker.getTotalExpenseAmount();
        displayLongDivider();
        System.out.println("User expense limit / amount details:");
        displayLongDivider();
        System.out.println("Your expense limit per month : $" + limit);
        if (expenseTracker.getUser().getOverExpenseLimit(totalExpense)) {
            System.out.println("You have reached / reached over your expense limit for the month!");
        }
        System.out.println("You have remaining $ " + (limit - totalExpense) + " to expense!");
        System.out.println("Your total expense amount : $ " + totalExpense);
        displayLongDivider();
    }

    // EFFECTS: stop running the application
    public void exitProgram() {
        displayLongDivider();
        System.out.println("You have exited out the app!");
        System.out.println("Come back later to keep track of your expenses!");
        displayLongDivider();
        this.activeProgram = false;
    }

    // EFFECTS: print out a divider using symbols
    public void displayLongDivider() {
        System.out.println("===================================================");
    }

    // EFFECTS: print out the format of each expense entry
    public void displayExpenseEntry(ExpenseEntry entry) {
        displayLongDivider();
        System.out.println("Expense Entry #" + entry.getId());
        displayLongDivider();

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
        displayLongDivider();

        System.out.println("w: Add an expense entry");
        System.out.println("e: Edit an expense entry");
        System.out.println("r: Delete an expense entry");
        System.out.println("t: View all expense entries");
        System.out.println("y: View expense entry from specific category");
        System.out.println("u: View user expense limit/amount details");
        System.out.println("i: Edit expense limit ");
        System.out.println("o: Save expense tracker to file");
        System.out.println("p: Load saved expense tracker");
        System.out.println("q: Quit application");
        displayLongDivider();
    }

}
