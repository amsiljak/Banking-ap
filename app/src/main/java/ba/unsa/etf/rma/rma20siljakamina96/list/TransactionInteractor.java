package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import ba.unsa.etf.rma.rma20siljakamina96.data.FinanceModel;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;

public class TransactionInteractor implements ITransactionInteractor {

    @Override
    public ArrayList<Transaction> getTransactions() {
        return FinanceModel.transactions;
    }
    @Override
    public void delete(Transaction transaction) {
        Iterator itr = FinanceModel.transactions.iterator();
        while (itr.hasNext())
        {
            Transaction t = (Transaction)itr.next();
            if (t.equals(transaction))
                itr.remove();
        }
    }
    @Override
    public void save(Transaction transaction, String title, double amount, Type type, String itemDescription, Date date) {
        for(Transaction t: FinanceModel.transactions) {
            if (t.getTitle().equals(transaction.getTitle())) {
                t.setTitle(title);
                t.setAmount(amount);
                t.setItemDescription(itemDescription);
                t.setType(type);
                t.setDate(date);
            }
        }
    }
    @Override
    public void save(Transaction transaction, String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        for(Transaction t: FinanceModel.transactions) {
            if (t.getTitle().equals(transaction.getTitle())) {
                t.setTitle(title);
                t.setAmount(amount);
                t.setItemDescription(itemDescription);
                t.setTransactionInterval(transactionInterval);
                t.setType(type);
                t.setDate(date);
                t.setEndDate(endDate);
            }
        }
    }
    @Override
    public void add(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        FinanceModel.transactions.add(new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate));
    }
    @Override
    public void add(String title, double amount, Type type, String itemDescription, Date date) {
        FinanceModel.transactions.add(new Transaction(date, amount, title, type, itemDescription));
    }
}
