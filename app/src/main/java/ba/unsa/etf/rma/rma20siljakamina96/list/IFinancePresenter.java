package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;
import java.util.Calendar;

import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public interface IFinancePresenter {
//    void refresh();

//    ArrayList<Transaction> sortTransactions(String tip);
//    ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, String type);
//    ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions, Calendar cal);

    void getTransactions(String typeId, String sort, String month, String year);

    void getAccount();

    //    void setTransactions();
//    void setAccount();

}
