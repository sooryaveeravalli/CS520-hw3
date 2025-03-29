// package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;


public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }


    public void checkTransaction(double amount, String category, Transaction transaction) {
	assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }


    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
	double amount = 50.0;
	String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
	//                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);
	
	// Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }


    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add and remove a transaction
	double amount = 50.0;
	String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);
    
        // Pre-condition: List of transactions contains only
	//                the added transaction
        assertEquals(1, model.getTransactions().size());
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);

	assertEquals(amount, getTotalCost(), 0.01);
	
	// Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);
    
        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());
    
        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }
    

        
    @Test
    public void testInvalidInputHandling() {
	// This is new test case 2: For the Controller
	//
	// Check pre-conditions
	assertEquals(0, model.getTransactions().size());
	assertEquals(0.00, getTotalCost(), 0.01);
	// Call the unit under test
        boolean didAddTransaction = controller.addTransaction(0.00, "InvalidCategory");
	// Check post-conditions (i.e. nothing changed)
	assertFalse(didAddTransaction);
        assertEquals(0, model.getTransactions().size());
        assertEquals(0.00, getTotalCost(), 0.01);

	// See above for the pre-conditions
	//
	// Call the unit under test
	boolean didAddTransaction2 = controller.addTransaction(50.00, "");
	// Check the post-conditions
	assertFalse(didAddTransaction2);
        assertEquals(0, model.getTransactions().size());
        assertEquals(0.00, getTotalCost(), 0.01);
    }
    //filter by amount
    @Test
    public void testFilterByAmount() {
	// This is new test case 3: For the Model
	//
	// Setup
	double amountToFilterBy = 50.0;
        controller.addTransaction(amountToFilterBy, "food");
        controller.addTransaction(30.00, "entertainment");
        controller.addTransaction(40.00, "food");

	// Check pre-conditions
	assertEquals(3, model.getTransactions().size());

	// Call unit under test
        controller.applyFilter(new AmountFilter(amountToFilterBy));

	// Check the post-conditions
        List<Transaction> displayedTransactions = view.getDisplayedTransactions();
        assertEquals(1, displayedTransactions.size());
        assertEquals(amountToFilterBy, displayedTransactions.get(0).getAmount(), 0.01);
    } 


    //filter by category
    @Test
    public void testFilterByCategory() {
	// This is new test case 4: For the Model
	//
	// Setup
	String categoryToFilterBy = "food";
        controller.addTransaction(50.00, categoryToFilterBy);
        controller.addTransaction(30.00, "entertainment");
        controller.addTransaction(40.00, categoryToFilterBy);

	// Check pre-conditions
	assertEquals(3, model.getTransactions().size());

	// Call the unit under test
        controller.applyFilter(new CategoryFilter(categoryToFilterBy));

	// Check the post-conditions
        List<Transaction> displayedTransactions = view.getDisplayedTransactions();
        assertEquals(2, displayedTransactions.size());
	for (Transaction currDisplayedTransaction : displayedTransactions) {
	    assertEquals(categoryToFilterBy, currDisplayedTransaction.getCategory());
	}
    }
}
