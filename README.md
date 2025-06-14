# My Personal Project

## Personal Finance & Expense Tracker (CPSC210 Java Project)
<img width="800" alt="image" src="https://github.com/user-attachments/assets/22858298-172a-4942-ac12-1cf91eaf8dfa" />
<img width="800" alt="image" src="https://github.com/user-attachments/assets/514a1199-a782-4aab-b9e9-97476eae6282" />





### Who will use it? 
This application is intended for anyone who wants to keep track of their weekly/monthly expenses! **It could be a great tool for budgeting and avoiding the habit of overspending!**

### What will the application do? 
This app will record and categorize expenses into fields like: food, bills, transportation, tution fees etc...
The user is able to add and delete expense entries.  The tracker will finally provide charts / summaries to display the users spending patterns, alerting the user for overspending when exceeding a certain limit.

### Why is this project of interest to you?
This project is of interest to me because I find myself buying a lot of unneeded items and having no accurate awareness of how much I was spending each month, which leads me to saving less money. I wanted to make this app so I can learn Java in the process and see how the final developed application can help my piggy bank! 


## **User Stories**
- As a user, I want to be able to add an expense entry to my list of expenses specifying the category , date , amount spent and extra note/detail.
- As a user, I want to be able to edit an expense entry 
- As a user, I want to be able to delete an expense entry from the list
- As a user, I want to be able to view the list of expenses (all expenses, expenses from specific category)
- As a user, I want to arrange my list of expenses based on the date (latest to earliest)
- As a user, I want to see a visualization / graphs of my spending based on the categories
- As a user, I want to see a warning indication for overspending after a set limit for the month

- As a user, when I select the quit option or from the save option from the application menu , I want to have an option that asks whether I want to save my expense tracker
- As a user, when I start the application, I want to be given the *option* to load my expense tracker from file

# Instructions for Grader

- You can generate the first required action related to the user story "adding multiple expense entries to a list" by filling in the entry details in the entry text fields at the bottom section of the application and pressing the 'add' button at the top section.
- You can generate the second required action related to the user story "adding multiple expense entries to a list" by deleting an entry when pressing the delete button and entering the ID of the entry you want to delete
- You can filter the list of expense entries based on categories by selecting the category in the dropdown menu at the top section
- You can locate my visual component by generating a bar graph of the user's expense amount categorized under the corresponding categories in the dropdown menu at the top section
- You can save the state of my application by pressing the save button at the top section
- You can reload the state of my application by pressing the load button at the top section

## **Phase 4: Task 2**
- Added a new expense entryID: 0
- Added a new expense entryID: 1
- Set expense limit200.0
- Deleted Expense EntryID: 0
- Added a new expense entryID: 2
- Set expense limit500.0

## **Phase 4: Task 3**
Looking at the UML diagram, I would have Categories Enum be associated with the ExpenseEntry class instead of having a dependency relationship with the ExpenseTrackerAppGUI.  This way the ExpenseEntry class can directly manages its category, making it easier to maintain and extend category-related features in the future. I would also like to change the association relationship with the User and ExpenseTracker to a bidirectional relationship. So that it makes more sense for a User to have an Expense Tracker and for an Expense Tracker to be aware of its associated User! If I had more time, I think it would best to separate and extract some UI componenets into its own classes oppose to having all of them under one class. For example, extracting the Jtable that displays the list of entries, and extracting the entry fields component. If an edit feature is implmeneted in the future, I could reduce code duplication by reusing the extracted entry fields component with minimal changes! This way we can improve code readability, better cohesiveness and reduce coupling.

