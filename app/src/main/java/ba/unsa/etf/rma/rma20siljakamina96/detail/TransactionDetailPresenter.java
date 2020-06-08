package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountChange;
import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver;

import static ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor.transactions;
import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;

public class TransactionDetailPresenter implements ITransactionDetailPresenter, TransactionListChange.OnTransactionPostDone, TransactionListDelete.OnTransactionDeleteDone, AccountChange.OnAccountChange, AccountInteractor.OnAccountGetDone, TransactionDetailResultReceiver.Receiver{
    private Transaction transaction;
    private static Account account;
    private Context context;
    private ITransactionDetailView view;
    private IAccountInteractor accountInteractor;
    private ITransactionListChange transactionListChange;
    private ITransactionListDelete transactionListDelete;
    public static TransactionDetailResultReceiver transactionDetailResultReceiver;

    public TransactionDetailPresenter(ITransactionDetailView view, Context context) {
        this.view = view;
        this.context = context;
        this.accountInteractor = new AccountInteractor();
        transactionDetailResultReceiver = new TransactionDetailResultReceiver(new Handler());
        transactionDetailResultReceiver.setReceiver(TransactionDetailPresenter.this);
        transactionListDelete = new TransactionListDelete();
        transactionListChange = new TransactionListChange();
    }
    private String formatDate(String date) {
        SimpleDateFormat DATE_FORMAT_SET = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat DATE_FORMAT_GET = new SimpleDateFormat("dd-MM-yyyy");
        Date dateTemp = null;
        try {
            dateTemp = DATE_FORMAT_GET.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = DATE_FORMAT_SET.format(dateTemp);
        return dateString;
    }
    @Override
    public void update(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate) {
        date = formatDate(date);
        if(!endDate.equals("")) endDate = formatDate(endDate);
        String id = this.transaction.getId().toString();

        Integer transactionInt = null;
        if(!transactionInterval.equals("")) transactionInt = Integer.valueOf(transactionInterval);

        if(!connected) {
            transactionListChange.update(date, Double.parseDouble(amount), title, type, itemDescription, transactionInt, type, Integer.valueOf(id), context.getApplicationContext());
            TransactionListInteractor.removeFromListOfTransactions(this.transaction.getId());
        }
        else new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(date, title, amount, endDate, itemDescription, transactionInterval, type, id);
    }

    @Override
    public void delete(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate) {
        if (!connected) {
            date = formatDate(date);
            if(!endDate.equals("")) endDate = formatDate(endDate);
            String id = this.transaction.getId().toString();

            Integer transactionInt = null;
            if(!transactionInterval.equals("")) transactionInt = Integer.valueOf(transactionInterval);

            transactionListDelete.delete(date, Double.parseDouble(amount), title, type, itemDescription, transactionInt, type, Integer.valueOf(id), context.getApplicationContext());
            TransactionListInteractor.removeFromListOfTransactions(this.transaction.getId());
        } else {
            new TransactionListDelete((TransactionListDelete.OnTransactionDeleteDone) this).execute(transaction.getId().toString());
        }
    }

    @Override
    public void add(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate) {
        date = formatDate(date);
        if(!endDate.equals("")) endDate = formatDate(endDate);

        Integer transactionInt = null;
        if(!transactionInterval.equals("")) transactionInt = Integer.valueOf(transactionInterval);

        if(!connected) {
            transactionListChange.save(date, Double.parseDouble(amount), title, type, itemDescription, transactionInt, type, context.getApplicationContext());
        }
        else new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(date, title, amount, endDate, itemDescription, transactionInterval, type, null);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction) transaction;
    }


    public HashMap<String, Double> getMonthlyPayments() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-yyyy");
        HashMap<String, Double> iznosi = new HashMap<>();
        for(Transaction t: transactions) {
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
                if(el.getValue() + amount > account.getMonthLimit()) return true;
                else return false;
            }
        }
        //ako je ovo prva potrosnja za taj mjesec provjerava samo nju
        if(amount > account.getMonthLimit()) return true;
        else return false;
    }

    @Override
    public void onTransactionPosted() {

    }

    @Override
    public void onTransactionDeleted() {

    }
    @Override
    public void setAccount() {
        new AccountInteractor((AccountInteractor.OnAccountGetDone)
                this).execute("account");
    }
    @Override
    public void updateBudget(String action, String amount, String type) {
        double amountValue = Double.parseDouble(amount);
        double budget = account.getBudget();


        if (action.equals("delete")) {
            if(type.equals("PURCHASE") || type.equals("INDIVIDUALPAYMENT")
                    || type.equals("REGULARPAYMENT")) budget += amountValue;
            else budget -= amountValue;
        }
        else if(action.equals("add")) {
            if(type.equals("PURCHASE") || type.equals("INDIVIDUALPAYMENT")
                    || type.equals("REGULARPAYMENT")) budget -= amountValue;
            else budget += amountValue;
        }
        else {
            double oldAmount = transaction.getAmount();
            double difference = amountValue - oldAmount;
            if(type.equals("PURCHASE") || type.equals("INDIVIDUALPAYMENT")
                    || type.equals("REGULARPAYMENT")) {
                budget -= difference;
            }
            else {
                budget += amountValue;
            }
        }
        if(connected) new AccountChange((AccountChange.OnAccountChange)
                this).execute(String.valueOf(budget),String.valueOf(account.getTotalLimit()),String.valueOf(account.getMonthLimit()));
    }

    @Override
    public void onAccountChanged() {

    }

    @Override
    public void onAccountGetDone(Account account) {
        this.account = account;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case TransactionListChange.STATUS_ERROR:
                break;
        }
    }


}
