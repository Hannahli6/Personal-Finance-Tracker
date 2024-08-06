package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.Collectors;

import exception.*;
import model.*;
import persistence.JsonViewer;
import persistence.JsonWriter;

// A expense tracker GUI application that displays expense tracker information and 
// allows the user to interact with  in the GUI for 
// adding, deleting, viewing their expense entries
// displaying expense status for the user
// generating a bar graph for the expense amount under each category
public class ExpenseTrackerAppGUI {

    private static final String JSON_STORE = "./data/expensetracker.json";
    private static final Color PINK_COLOR = new Color(255, 173, 173);
    private static final Color ORANGE_COLOR = new Color(255, 214, 165);
    private static final Color PURPLE_COLOR = new Color(222, 218, 244);
    private static final Color BLUE_COLOR = new Color(217, 237, 248);
    private static final Color YELLOW_COLOR = new Color(253, 255, 182);
    private JsonWriter jsonWriter;
    private JsonViewer jsonReader;

    // panels
    JFrame frame;
    JPanel frameWrapper = new JPanel();
    JPanel entryFieldJPanel = new JPanel();
    JPanel buttonJPanel = new JPanel();
    JPanel tableJPanel = new JPanel();
    JPanel statusMessageJPanel = new JPanel();

    // entry field
    JTextField nameTextField = new JTextField(10);
    JTextField categoryTextField = new JTextField(10);
    JTextField amountTextField = new JTextField(10);
    JTextField noteTextField = new JTextField(10);
    JTextField dateTextField = new JTextField(10);
    JComboBox<Categories> entryFieldCategoryDropdown;
    JComboBox<Categories> categoryFilterDropdown;

    // buttons
    JButton addEntryButton = new JButton("add");
    JButton loadButton = new JButton("load");
    JButton saveButton = new JButton("save");
    JButton deleteButton = new JButton("delete");
    JButton editExpenseLimitButton = new JButton("Edit Expense Limit");
    JButton generateGraphButton = new JButton("View Graph");

    // status labels
    JLabel totalExpenseJLabel;
    JLabel expenseLimitJLabel;
    JLabel expenseStatusJLabel;

    // table
    DefaultTableModel tableModel;

    //Expense Tracker
    ExpenseTracker expenseTracker;

    public ExpenseTrackerAppGUI() throws FileNotFoundException {
        //  ================================
        //initalize
        initializeApp();
        // Create the frame (window)
        createJFrame();
        // create jtable
        JScrollPane scrollPaneExpenseTable = createJTable();
        // dropdown filter
        categoryFilterDropdown = new JComboBox<Categories>(Categories.values());
        // Create label
        initializeExpenseStatusLabels();

        // =========================================
        // EVENT LISTENER

        handleCategoryFilter();
        handleGenerateGraph();
        handleEditExpenseLimit();
        handleDeleteEntry();
        handleSaveToFile();
        handleLoadFromFile();
        handleAddEntry();
        handleLogEventsOnCloseWindow();


        // =======================================
        // PANELS & LAYOUTS

        // Create a panel and add the label and button to itz
        createEntryField();
        // BUTTONS LAYOUT
        addButtonsToPanel();
        // STATUS MESSAGE LAYOUT
        addExpenseStatusLabelToPanel();
        // TABLE PANEL LAYOUT
        addExpenseTableToPanel(scrollPaneExpenseTable);
        // ADD ALL PANELS TO FRAME
        addAllPanelsToFrame();
        // Make the frame visible
        frame.setVisible(true);

    }

    

    // EFFECTS: create the jframe with size and title
    private void createJFrame() {
        frame = new JFrame("Expense Tracker");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(750, 750);
    }

    // EFFECTS: add all panels to the frameWrapper panel that will be added to the
    // main frame
    // frameWrapper for padding within the frame
    private void addAllPanelsToFrame() {
        frameWrapper.setLayout(new BorderLayout());
        frameWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        frameWrapper.add(buttonJPanel, BorderLayout.NORTH);
        frameWrapper.add(tableJPanel, BorderLayout.CENTER);
        frameWrapper.add(entryFieldJPanel, BorderLayout.SOUTH);

        // add framewrapper to frame
        // framewrapper has padding
        frame.add(frameWrapper);
    }

    // EFFECTS: add expense table and status message panel to tableJPanel for layout
    private void addExpenseTableToPanel(JScrollPane scrollPaneExpenseTable) {
        tableJPanel.setLayout(new BorderLayout());
        tableJPanel.add(scrollPaneExpenseTable, BorderLayout.NORTH);
        tableJPanel.add(statusMessageJPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: add expense status label to JPanel for layout
    private void addExpenseStatusLabelToPanel() {
        statusMessageJPanel.setLayout(new BorderLayout());
        statusMessageJPanel.add(expenseLimitJLabel, BorderLayout.NORTH);
        statusMessageJPanel.add(totalExpenseJLabel, BorderLayout.CENTER);
        statusMessageJPanel.add(expenseStatusJLabel, BorderLayout.SOUTH);
    }

    // EFFECTS: add functionality buttons to Panel
    private void addButtonsToPanel() {
        buttonJPanel.add(addEntryButton);
        buttonJPanel.add(loadButton);
        buttonJPanel.add(saveButton);
        buttonJPanel.add(deleteButton);
        buttonJPanel.add(editExpenseLimitButton);
        buttonJPanel.add(generateGraphButton);
        buttonJPanel.add(categoryFilterDropdown);
    }

    // EFFECTS: create new Jlabels for expense status
    private void initializeExpenseStatusLabels() {
        expenseLimitJLabel = new JLabel("Expense Limit: $" + expenseTracker.getUser().getExpenseLimit());
        totalExpenseJLabel = new JLabel("Total Expense Amount: $" + expenseTracker.getTotalExpenseAmount());
        expenseStatusJLabel = new JLabel("Expense Status: ");
    }

    // EFFECTS: add each of those entry details to an expense on button click
    // entry
    // if date is invalid, throw date exception
    // if expense Amount is invalid, throw expense amount invalid exception
    // if expense amount is negative, throw negative amount exception
    private void handleAddEntry() {
        addEntryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameTextField.getText();
                    String category = entryFieldCategoryDropdown.getSelectedItem().toString();
                    double expenseAmount = getAmountInDoubleFromTextField();
                    String note = noteTextField.getText();
                    LocalDate date = getDateFromTextField();
                    expenseTracker.addExpenseEntry(new ExpenseEntry(name, category, expenseAmount, note, date));
                    update();
                    clearTextField();
                } catch (DateTimeParseException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter date in yyyy-MM-dd format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid Expense Amount field", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NegativeAmountException msg) {
                    JOptionPane.showMessageDialog(frame, "Cannot enter negative expense amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //EFFECTS: action listener to print event log into console after closing the application
    private void handleLogEventsOnCloseWindow() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                for (model.Event next : EventLog.getInstance()) {
                    System.out.println(next);
                }
            }
        });
    }

    // EFFECTS: action listener to load expense tracker from JSON file on button
    // click
    private void handleLoadFromFile() {
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    expenseTracker = jsonReader.read();
                    update();
                    setDropDownFilterToDefaultValue();
                } catch (IOException msg) {
                    JOptionPane.showMessageDialog(frame, "Unable to read from file: " + JSON_STORE, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: action listener to save expense tracker to JSON file on button click
    private void handleSaveToFile() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    jsonWriter.open();
                    jsonWriter.write(expenseTracker);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(frame, "Expense Tracker Saved Sucessfully!", "Sucess Message!",
                            JOptionPane.DEFAULT_OPTION);
                } catch (FileNotFoundException msg) {
                    JOptionPane.showMessageDialog(frame, "Unable to load to file: " + JSON_STORE, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: action listener to handle delete entries on button click
    private void handleDeleteEntry() {
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idString = JOptionPane.showInputDialog("Please input a value");
                try {
                    int id = Integer.parseInt(idString);
                    if (expenseTracker.findExpenseEntry(id) != null) {
                        expenseTracker.deleteExpenseEntry(id);
                        update();
                        setDropDownFilterToDefaultValue();
                        JOptionPane.showMessageDialog(frame, "Entry Id : " + id + " has been deleted!",
                                "Sucess Message!",
                                JOptionPane.DEFAULT_OPTION);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid ID! No entries deleted!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: event listener for editing expense limit amount on button click
    private void handleEditExpenseLimit() {
        editExpenseLimitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String expenseLimitString = JOptionPane.showInputDialog("Enter a new expense limit: ");
                    double limit = Double.parseDouble(expenseLimitString);
                    if (limit < 0) {
                        throw new NegativeAmountException();
                    }
                    expenseTracker.getUser().setExpenseLimit(limit);
                    update();
                } catch (NumberFormatException msg) {
                    JOptionPane.showMessageDialog(frame, "Invalid Expense Limit", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NegativeAmountException msg) {
                    JOptionPane.showMessageDialog(frame, "Cannot enter negative expense amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: event listener for displaying the graph on button click
    private void handleGenerateGraph() {
        generateGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    generateGraph();
                } catch (EmptyGraphError msg) {
                    JOptionPane.showMessageDialog(frame, "Empty Graph! Make an entry to a category first!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: event listner for filtering entries based on category
    private void handleCategoryFilter() {
        categoryFilterDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<Categories> dropdown = (JComboBox<Categories>) e.getSource();
                Categories category = (Categories) dropdown.getSelectedItem();
                updateExpenseTable(category);
            }
        });
    }

    //EFFECTS: create jTable for expense table
    private JScrollPane createJTable() {
        tableModel = new DefaultTableModel(
                new Object[] { "Name", "Category", "Amount ($) ", "Note", "Date", "Id" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true);
        JScrollPane scrollPaneExpenseTable = new JScrollPane(expenseTable);
        expenseTable.setRowHeight(35);
        return scrollPaneExpenseTable;
    }

    // EFFECTS: create entry fields
    public void createEntryField() {
        entryFieldJPanel.setLayout(new GridLayout(5, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name : ");
        entryFieldJPanel.add(nameLabel);
        entryFieldJPanel.add(nameTextField);

        JLabel categoryLabel = new JLabel("Category : ");
        // filter out the ALL option from ENUM
        List<Categories> filteredCategoriesList = Arrays.stream(Categories.values())
                .filter(category -> category != Categories.All)
                .collect(Collectors.toList());
        // convert the list into an array
        Categories[] filteredCategoriesArray = filteredCategoriesList.toArray(new Categories[0]);
        // use the filtered categories array in JComboBox
        entryFieldCategoryDropdown = new JComboBox<>(filteredCategoriesArray);
        entryFieldJPanel.add(categoryLabel);
        entryFieldJPanel.add(entryFieldCategoryDropdown);

        JLabel amountLabel = new JLabel("Expense Amount : ");
        entryFieldJPanel.add(amountLabel);
        entryFieldJPanel.add(amountTextField);

        JLabel noteLabel = new JLabel("Note : ");
        entryFieldJPanel.add(noteLabel);
        entryFieldJPanel.add(noteTextField);

        JLabel dateLabel = new JLabel("Date : ");
        entryFieldJPanel.add(dateLabel);
        entryFieldJPanel.add(dateTextField);
    }

    // ================================================================================
    // helper methods

    // EFFECTS: set drop down filter to default value of 'All' Category
    public void setDropDownFilterToDefaultValue() {
        categoryFilterDropdown.setSelectedItem(Categories.All);
    }

    // EFFECTS: generate graph by mapping each expense amount to its corresponding
    // category
    public void generateGraph() throws EmptyGraphError {
        JFrame frame = new JFrame("Spending Categories Bar Graph");
        frame.setSize(800, 600);

        if (expenseTracker.getListOfExpenseEntries().size() == 0) {
            throw new EmptyGraphError();
        }

        Map<String, Double> categorySpendings = new HashMap<>();
        for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
            String categoryKey = entry.getCategory();
            double expenseAmount = entry.getExpenseAmount();
            if (!categorySpendings.containsKey(categoryKey)) {
                categorySpendings.put(categoryKey, expenseAmount);
            } else {
                categorySpendings.put(categoryKey, categorySpendings.get(categoryKey) + expenseAmount);
            }
        }
        ArrayList<Double> values = new ArrayList<>(categorySpendings.values());
        ArrayList<String> categoriesLabels = new ArrayList<>(categorySpendings.keySet());
        Color[] colors = { PINK_COLOR, ORANGE_COLOR, YELLOW_COLOR, BLUE_COLOR, PURPLE_COLOR };

        BarGraph barGraph = new BarGraph(values, categoriesLabels, colors);
        frame.add(barGraph);
        frame.setVisible(true);
    }

    // EFFECTS: update the dynamic data that displays on the GUI
    public void update() {
        updateExpenseLimit();
        updateTotalExpense();
        updateExpenseTable(Categories.All);
        updateExpenseStatus();
        setDropDownFilterToDefaultValue();
    }

    // EFFECTS: update the expense status message
    public void updateExpenseStatus() {
        double totalExpense = expenseTracker.getTotalExpenseAmount();
        double limit = expenseTracker.getUser().getExpenseLimit();
        if (expenseTracker.getUser().getOverExpenseLimit(totalExpense)) {
            expenseStatusJLabel
                    .setText("Expense Status: You have reached / reached over your expense limit for the month!");
        } else {
            expenseStatusJLabel
                    .setText("Expense Status: You have remaining $" + (limit - totalExpense) + " to expense!");
        }
    }

    // EFFECTS: update the expense limit message
    public void updateExpenseLimit() {
        expenseLimitJLabel.setText("Expense Limit: $" + expenseTracker.getUser().getExpenseLimit());
    }

    // EFFECTS: update the expense limit message
    public void updateTotalExpense() {
        totalExpenseJLabel.setText("Total Expense Amount: $" + expenseTracker.getTotalExpenseAmount());
    }

    // EFFECTS: update the expense table to display
    public void updateExpenseTableOnCategeory(Categories category) {
        clearTable();
        for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
            if (entry.getCategory().equals(category.toString())) {
                tableModel.addRow(new Object[] { entry.getName(), entry.getCategory(),
                        entry.getExpenseAmount(), entry.getNote(), entry.getDate(), entry.getId() });
            }

        }
    }

    //EFFECTS: display and filter the entries in expense table model based on the given Category
    public void updateExpenseTable(Categories category) {
        clearTable();
        for (ExpenseEntry entry : expenseTracker.getListOfExpenseEntries()) {
            if (category == Categories.All || category.toString().equals(entry.getCategory())) {
                tableModel.addRow(new Object[] { entry.getName(), entry.getCategory(),
                        entry.getExpenseAmount(), entry.getNote(), entry.getDate(), entry.getId() });
            }
        }

    }

    // EFFECTS: clear the expense table
    public void clearTable() {
        tableModel.setRowCount(0);
    }

    // EFFECTS: initialize the expense tracker app
    public void initializeApp() throws FileNotFoundException {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonViewer(JSON_STORE);
        this.expenseTracker = new ExpenseTracker();
    }

    // EFFECTS: convert textfield of String date to LocalDate, throws
    // DateTimeParseException if date is invalid
    public LocalDate getDateFromTextField() throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        String dateStr = dateTextField.getText();
        date = LocalDate.parse(dateStr, formatter);
        return date;
    }

    // EFFECTS: convert textfield of String expense amount to double expense amount
    // throws NegativeAmountException if expense amount is < 0
    // throws NumberFormatException if textfield value converted to double is still
    // not a valid number
    public double getAmountInDoubleFromTextField() throws NumberFormatException, NegativeAmountException {
        try {
            double expenseAmount;
            expenseAmount = Double.parseDouble(amountTextField.getText());
            if (expenseAmount < 0) {
                throw new NegativeAmountException();
            }
            return expenseAmount;
        } catch (NumberFormatException msg) {
            throw new NumberFormatException();
        }
    }

    // EFFECTS: clear all text field after adding
    public void clearTextField() {
        nameTextField.setText("");
        dateTextField.setText("");
        amountTextField.setText("");
        noteTextField.setText("");
    }

    

}
