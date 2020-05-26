package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public class FinancePresenter implements IFinancePresenter, TransactionListInteractor.OnTransactionGetDone, AccountInteractor.OnAccountGetDone{
    private Context context;
    private IFinanceView view;
    private Account account;
    private String typeOfSort;
    private String typeOfTransaction;
    private Calendar cal;


    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.view = view;
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


//    @Override
//    public void setTransactions() {
//        view.setTransactions(financeInteractor.getTransactions());
//    }

//    @Override
//    public void refresh() {
//        setAccount();
//        view.setTransactions(financeInteractor.getTransactions());
//        view.notifyTransactionListDataSetChanged();
//        view.setDate();
//    }



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

                Calendar startingPoint = Calendar.getInstance();
                startingPoint.setTime(t.getDate());

                Calendar endPoint = Calendar.getInstance();
                if(t.getEndDate()!= null) endPoint.setTime(t.getEndDate());

                //ovo sam dodala da bi se prikazivala transakcija i u mjesecu u kojem je datum
                Calendar temp = (Calendar) cal.clone();
                temp.add(Calendar.MONTH,1);

                if (startingPoint.compareTo(temp) <= 0 && (t.getEndDate()==null || cal.compareTo(endPoint) <= 0)) {
                    lista.add(t);
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
}

