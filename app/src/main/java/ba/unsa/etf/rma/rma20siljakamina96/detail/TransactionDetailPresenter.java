package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.FinancePresenter;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private Transaction transaction;
    private Context context;
    private ITransactionInteractor transactionInteractor;
    private IAccountInteractor accountInteractor;


    public TransactionDetailPresenter(Context context) {
        this.context = context;
        this.transactionInteractor = new TransactionInteractor();
        this.accountInteractor = new AccountInteractor();
    }
    @Override
    public void save(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
        transactionInteractor.save(transaction, title, amount, type, itemDescription, transactionInterval, date, endDate);
    }
    @Override
    public void save(String title, double amount, Type type, String itemDescription, Date date) {
        transactionInteractor.save(transaction, title, amount, type, itemDescription, date);
    }

    @Override
    public void delete() {
        transactionInteractor.delete(transaction);
    }

    @Override
    public void add(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
       transactionInteractor.add(title, amount, type, itemDescription, transactionInterval, date, endDate);
    }
    @Override
    public void add(String title, double amount, Type type, String itemDescription, Date date) {
        transactionInteractor.add(title, amount, type, itemDescription, date);
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
        return accountInteractor.getAccount();
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction) transaction;
    }


    public HashMap<String, Double> getMonthlyPayments() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-yyyy");
        HashMap<String, Double> iznosi = new HashMap<>();
        for(Transaction t: transactionInteractor.getTransactions()) {
            if(t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                    || t.getType().toString().equals("REGULARPAYMENT")) {
                if (iznosi.containsKey(DATE_FORMAT.format(t.getDate()))) {
                    Double vrijednost = iznosi.get(DATE_FORMAT.format(t.getDate())) + t.getAmount();
                    iznosi.put(DATE_FORMAT.format(t.getDate()), vrijednost);
                } else iznosi.put(t.getDate().toString(), t.getAmount());
            }
        }
        return iznosi;
    }
    @Override
    public double getTotalPayments() {
        double totalPayments = 0;
        for(Map.Entry <String,Double> el : getMonthlyPayments().entrySet()) {
            totalPayments += el.getValue();
        }
        return totalPayments;
    }
    @Override
    public boolean isOverLimit(double amount, String date) {
        //trazi zbir postrosnji u određenom mjesecu i vraca true ako je proslo mjesecni limit
        for(Map.Entry <String,Double> el : getMonthlyPayments().entrySet()) {
            if(el.getKey().equals(date)) {
                if(el.getValue() + amount > getAccount().getMonthLimit()) return true;
                else return false;
            }
        }
        //ako je ovo prva potrosnja za taj mjesec provjerava samo nju
        if(amount > getAccount().getMonthLimit()) return true;
        else return false;
    }
}
