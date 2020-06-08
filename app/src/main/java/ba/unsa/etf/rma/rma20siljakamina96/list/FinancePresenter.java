package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.detail.ITransactionDetailView;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionListChange;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionListDelete;
import ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver;

import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;

public class FinancePresenter implements IFinancePresenter, TransactionListInteractor.OnTransactionGetDone,
        AccountInteractor.OnAccountGetDone, TransactionListDelete.OnTransactionDeleteDone, TransactionListChange.OnTransactionPostDone {
    private Context context;
    private IFinanceView view;
    private Account account;
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
    public void onTransactionGetDone(ArrayList<Transaction> results) {
        results =sortTransactions(results);
        results =filterTransactionsByType(results);
        results = filterTransactionsByDate(results);

        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void onAccountGetDone(Account account) {
        this.account = account;
        view.setAccountData(account);
    }

    @Override
    public void getTransactions(String type, String sort, Calendar cal){

        this.typeOfSort = sort;
        this.typeOfTransaction = type;
        this.cal = cal;

        if(!connected) {
            ArrayList<Transaction> lista = new ArrayList<>();
            lista.addAll(transactionListInteractor.getTransactions());
            lista =sortTransactions(lista);
            lista =filterTransactionsByType(lista);
            lista = filterTransactionsByDate(lista);
            view.setTransactions(lista);
        }
        //pokupi sve transakcije i onda ih starim metodama filtrira
        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone)
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
    public void uploadToServis() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        for(Transaction t: transactionListInteractor.getDeletedTransactions(context.getApplicationContext())) {
            new TransactionListDelete((TransactionListDelete.OnTransactionDeleteDone) this).execute(t.getId().toString());
        }
        for(Transaction t: transactionListInteractor.getModifiedTransactions(context.getApplicationContext())) {
            new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(DATE_FORMAT.format(t.getDate()), t.getTitle(), String.valueOf(t.getAmount()), DATE_FORMAT.format(t.getEndDate()), t.getItemDescription(), String.valueOf(t.getTransactionInterval()), t.getType().toString(), String.valueOf(t.getId()));
        }
        for(Transaction t: transactionListInteractor.getAddedTransactions(context.getApplicationContext())) {
            new TransactionListChange((TransactionListChange.OnTransactionPostDone) this).execute(DATE_FORMAT.format(t.getDate()), t.getTitle(), String.valueOf(t.getAmount()), DATE_FORMAT.format(t.getEndDate()), t.getItemDescription(), String.valueOf(t.getTransactionInterval()), t.getType().toString(), null);
        }
    }

    @Override
    public void onTransactionDeleted() {
        getTransactions(typeOfTransaction,typeOfSort,cal);
    }

    @Override
    public String getAction(Transaction transaction) {
        for(Transaction t: transactionListInteractor.getDeletedTransactions(context.getApplicationContext())) {
            if(t.getId().equals(transaction.getId())) {
                return "delete";
            }
        }
        for(Transaction t: transactionListInteractor.getAddedTransactions(context.getApplicationContext())) {
            if(t.getId().equals(transaction.getId())) {
                return "add";
            }
        }
        for(Transaction t: transactionListInteractor.getModifiedTransactions(context.getApplicationContext())) {
            if(t.getId().equals(transaction.getId())) {
                return "modify";
            }
        }
        return null;
    }

    @Override
    public void undoAction(Transaction transaction) {
//        TransactionListInteractor.undo(transaction.getId());
    }

    @Override
    public void onTransactionPosted() {

    }
}

