package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public interface IFinancePresenter {
    void refresh();

    ArrayList<Transaction> sortTransactions(String tip);
    ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, String type);
    ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions, Calendar cal);

    void setTransactions();
    void setAccount();

    void deleteTransaction(Transaction t);
    void addTransaction(Transaction t);

    HashMap<String, Double> getMonthlyPayments();
}
