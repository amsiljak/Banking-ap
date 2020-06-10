package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionListChange;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionListDelete;

import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;

public class FinancePresenter implements IFinancePresenter, TransactionListInteractor.OnTransactionsGetDone,
        AccountInteractor.OnAccountGetDone, TransactionListDelete.OnTransactionDeleteDone, TransactionListChange.OnTransactionModifyDone {
    private Context context;
    private IFinanceView view;
    private static Account account;
    private String typeOfSort;
    private String typeOfTransaction;
    private Calendar cal;
    private TransactionListInteractor transactionListInteractor;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.view = view;
        transactionListInteractor = new TransactionListInteractor();
    }
    public FinancePresenter(Context context) {
        this.context = context;
        transactionListInteractor = new TransactionListInteractor();
    }

    @Override
    public void getTransactions(String type, String sort, Calendar cal){
        this.typeOfSort = sort;
        this.typeOfTransaction = type;
        this.cal = cal;

        if(!connected) {
            ArrayList<Transaction> lista = new ArrayList<>();
            lista.addAll(transactionListInteractor.getAddedTransactions(context.getApplicationContext()));
            lista.addAll(transactionListInteractor.getDeletedTransactions(context.getApplicationContext()));
            lista.addAll(transactionListInteractor.getModifiedTransactions(context.getApplicationContext()));
            lista.addAll(TransactionListInteractor.getTransactions());
            lista =sortTransactions(lista);
            lista =filterTransactionsByType(lista);
            lista = filterTransactionsByDate(lista);
            view.setTransactions(lista);
            view.notifyTransactionListDataSetChanged();
        }
        //pokupi sve transakcije i onda ih starim metodama filtrira
        else new TransactionListInteractor((TransactionListInteractor.OnTransactionsGetDone)
                this).execute(null,null,null,null);
    }
    @Override
    public void setAccount(){
        new AccountInteractor((AccountInteractor.OnAccountGetDone)
                this).execute("account");
    }
    @Override
    public Account getAccount(){
        return account;
    }

    @Override
    public ArrayList<Transaction> sortTransactions(ArrayList<Transaction> transakcije) {
        if(typeOfSort.equals("Price - Ascending")) Collections.sort(transakcije, Transaction.TranPriceComparatorAsc);
        if(typeOfSort.equals("Price - Descending")) Collections.sort(transakcije, Transaction.TranPriceComparatorDesc);
        if(typeOfSort.equals("Title - Ascending")) Collections.sort(transakcije, Transaction.TranTitleComparatorAsc);
        if(typeOfSort.equals("Title - Descending")) Collections.sort(transakcije, Transaction.TranTitleComparatorDesc);
        if(typeOfSort.equals("Date - Ascending")) Collections.sort(transakcije, Transaction.TranDateComparatorAsc);
        if(typeOfSort.equals("Date - Descending")) Collections.sort(transakcije, Transaction.TranDateComparatorDesc);

        return transakcije;
    }

    @Override
    public ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            if (typeOfTransaction.equals("All")) lista.add(t);
            else if (t.getType().toString().equals(typeOfTransaction)) lista.add(t);
        }
        return lista;
    }
    @Override
    public ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions) {

        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            if (t.getType().toString().equals("REGULARPAYMENT") || t.getType().toString().equals("REGULARINCOME")) {

                SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MM");
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                Date endOfYear = null;
                try {
                    endOfYear = format.parse("31.12."+Calendar.getInstance().get(Calendar.YEAR));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar dateOfPayment = Calendar.getInstance();
                Calendar endDateOfPayment = Calendar.getInstance();
                dateOfPayment.setTime(t.getDate());
                if(t.getEndDate() != null) endDateOfPayment.setTime(t.getEndDate());
                else endDateOfPayment.setTime(endOfYear);

                //povecava pocetni datum za interval sve dok ne dodje do krajnjeg,
                //i onda uzima mjesec pocetnog i na njega stavlja amount

                while(dateOfPayment.compareTo(endDateOfPayment) <= 0) {
                    if(dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) && dateOfPayment.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
                        float month = Float.valueOf(MONTH_DATE_FORMAT.format(dateOfPayment.getTime()));
                        lista.add(t);
                    }
                    if(t.getTransactionInterval() == 0) break;
                    else dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(t.getDate());

                if (calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                        && calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR))  lista.add(t);
            }
        }
        return lista;
    }

    @Override
    public void onTransactionDeleted(int id) {
        getTransactions(typeOfTransaction,typeOfSort,cal);
    }

    @Override
    public void undoAction(Transaction transaction) {
        transactionListInteractor.deleteFromDB(transaction.getId(),context.getApplicationContext(), true);
        TransactionListInteractor.addToListOfTransactions(transaction);
    }

    @Override
    public void onTransactionModified(int id) { }

    @Override
    public void onTransactionsGetDone(ArrayList<Transaction> results) {
        results.addAll(transactionListInteractor.getAddedTransactions(context.getApplicationContext()));
        results.addAll(transactionListInteractor.getDeletedTransactions(context.getApplicationContext()));
        results.addAll(transactionListInteractor.getModifiedTransactions(context.getApplicationContext()));
        results =sortTransactions(results);
        results =filterTransactionsByType(results);
        results = filterTransactionsByDate(results);

        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();

    }

    @Override
    public void onAccountGetDone(Account account) {
        if(account != null) this.account = account;
        //nece se ni postavljati ako je i stari i novi account null
        if(this.account != null) view.setAccountData(this.account);
    }
    @Override
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}

