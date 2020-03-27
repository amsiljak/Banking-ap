package ba.unsa.etf.rma.rma20siljakamina96;

import java.util.ArrayList;
import java.util.Calendar;

public interface IFinancePresenter {
    void refresh();

    void sortTransactions(String tip);
    ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, String type);
    ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions, Calendar cal);

    void setTransactions();
}
