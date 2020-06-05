package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;

public interface ITransactionInteractor {
    ArrayList<Transaction> getTransactions();
//
//    void delete(Transaction transaction);
//
//    void save(Transaction transaction, String title, double amount, Type type, String itemDescription, Date date);
//
//    void save(Transaction transaction, String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate);
//
//    void add(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate);
//
//    void add(String title, double amount, Type type, String itemDescription, Date date);
}
