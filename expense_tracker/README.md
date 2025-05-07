# hw2

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTrackerApp
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## Features
- Add a new transaction: First specify the amount and category. Then click on the Add transaction button. Adds the new transaction to the list and updates the total cost.
- Remove a transaction (Undo): Select a transaction from the list and click the Undo transaction button to undo/remove it. The transaction will be removed from the list and the total cost will be updated. If no transaction is selected or the total of transactions row is selected, an error message will be shown.
- Filter the transaction list by either amount or category: First specify the amount or category to be matched. Then click the corresponding Filter button. Highlights the matching transactions in the list.

## Undo Functionality
- The application supports undoing a transaction removal by allowing users to remove any transaction from the list.
- The UI disables removal if no transaction is selected, providing clear feedback.
- All updates are reflected in the Transactions List and the Total Cost.

## Testing
- Unit tests are provided for adding, removing, and filtering transactions.
- The test suite includes a test case for attempting to remove a transaction not contained in the transaction list, ensuring robust error handling.