package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private Transaction transaction;
    private Context context;
    private IFinanceInteractor interactor;
    private ArrayList<Transaction> transactions = FinancePresenter.getTransactions();

    public TransactionDetailPresenter(Context context) {
        this.context = context;
        this.interactor = new FinanceInteractor();
    }
    @Override
    public void save(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        for(Transaction t: transactions) {
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
    public void save(String title, double amount, Type type, String itemDescription, Date date) {
        for(Transaction t: transactions) {
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
    public void add(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        transactions.add(new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate));
    }
    @Override
    public void add(String title, double amount, Type type, String itemDescription, Date date) {
        transactions.add(new Transaction(date, amount, title, type, itemDescription));
    }

    @Override
    public void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        this.transaction = new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate);
    }

    @Override
    public void create(String title, double amount, Type type, String itemDescription, Date date) {
        this.transaction = new Transaction(date, amount, title, type, itemDescription);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public Account getAccount() {
        return interactor.getAccount();
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction) transaction;
    }
}
