package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.content.Context;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListChange;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListDelete;

import static ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListInteractor.transactions;

public class TransactionDetailPresenter implements ITransactionDetailPresenter, TransactionListChange.OnTransactionPostDone, TransactionListDelete.OnTransactionDeleteDone {
    private Transaction transaction;
    private Context context;
    private IAccountInteractor accountInteractor;


    public TransactionDetailPresenter(Context context) {
        this.context = context;
        this.accountInteractor = new AccountInteractor();
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
    public void save(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate) {
        date = formatDate(date);

        if(!endDate.equals("")) endDate = formatDate(endDate);
        String id = this.transaction.getId().toString();
        new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(date, title, amount, endDate, itemDescription, transactionInterval, type, id);
    }

    @Override
    public void delete() {
        new TransactionListDelete((TransactionListDelete.OnTransactionDeleteDone) this).execute(transaction.getId().toString());
    }

    @Override
    public void add(String date, String amount, String title, String type, String itemDescription, String transactionInterval, String endDate) {
        date = formatDate(date);

        if(!endDate.equals("")) endDate = formatDate(endDate);
        new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(date, title, amount, endDate, itemDescription, transactionInterval, type, null);
    }

//    @Override
//    public void create(String title, double amount, Type type, String itemDescription, int transactionInterval, Date date, Date endDate) {
//        this.transaction = new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate);
//    }
//
//    @Override
//    public void create(String title, double amount, Type type, String itemDescription, Date date) {
//        this.transaction = new Transaction(date, amount, title, type, itemDescription,null,null);
//    }



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
                if(el.getValue() + amount > getAccount().getMonthLimit()) return true;
                else return false;
            }
        }
        //ako je ovo prva potrosnja za taj mjesec provjerava samo nju
        if(amount > getAccount().getMonthLimit()) return true;
        else return false;
    }

    @Override
    public void onTransactionPosted() {

    }

    @Override
    public void onTransactionDeleted() {

    }
}
