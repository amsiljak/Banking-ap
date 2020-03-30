package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class FinancePresenter implements IFinancePresenter {
    private Context context;
    private IFinanceInteractor financeInteractor;
    private IFinanceView view;
    private ArrayList<Transaction> transactions;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.financeInteractor = new FinanceInteractor();
        this.view = view;
        transactions = financeInteractor.getTransactions();
    }

    @Override
    public void setTransactions() {
        view.setTransactions(transactions);
    }

    @Override
    public void refresh() {
        view.setAccountData(String.valueOf(financeInteractor.getAccount().getBudget()),String.valueOf(financeInteractor.getAccount().getTotalLimit()));
        view.setTransactions(transactions);
        view.notifyTransactionDataSetChanged();
        view.setDate();
    }

    @Override
    public ArrayList<Transaction> sortTransactions(String tip) {
        ArrayList<Transaction> transakcije = new ArrayList<>();
        transakcije.addAll(transactions);
        if(tip.equals("Price - Ascending")) Collections.sort(transakcije, Transaction.TranPriceComparatorAsc);
        if(tip.equals("Price - Descending")) Collections.sort(transakcije, Transaction.TranPriceComparatorDesc);
        if(tip.equals("Title - Ascending")) Collections.sort(transakcije, Transaction.TranTitleComparatorAsc);
        if(tip.equals("Title - Descending")) Collections.sort(transakcije, Transaction.TranTitleComparatorDesc);
        if(tip.equals("Date - Ascending")) Collections.sort(transakcije, Transaction.TranDateComparatorAsc);
        if(tip.equals("Date - Descending")) Collections.sort(transakcije, Transaction.TranDateComparatorDesc);

        return transakcije;
    }

    @Override
    public ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, String type) {
        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            if (type.equals("All")) lista.add(t);
            else if (t.getType().toString().equals(type)) lista.add(t);
        }
        return lista;
    }
    @Override
    public ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions, Calendar cal) {

        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            if (t.getType().toString().equals("REGULARPAYMENT") || t.getType().toString().equals("REGULARINCOME")) {

                Calendar startingPoint = Calendar.getInstance();
                startingPoint.set(t.getDate().getYear(),t.getDate().getMonth(),t.getDate().getDay());
                Calendar endPoint = Calendar.getInstance();
                startingPoint.set(t.getEndDate().getYear(),t.getEndDate().getMonth(),t.getEndDate().getDay());

                if (cal.compareTo(startingPoint) >= 0 && cal.compareTo(endPoint) <= 0) {
                    lista.add(t);
                }
                lista.add(t);

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
    public void deleteTransaction(Transaction t) {
        Iterator itr = transactions.iterator();
        while (itr.hasNext())
        {
            Transaction transaction = (Transaction)itr.next();
            if (t.equals(transaction))
                itr.remove();
        }
        view.setTransactions(transactions);
    }

    @Override
    public void addTransaction(Transaction t) {
        transactions.add(t);
        view.setTransactions(transactions);
    }

    @Override
    public void modifyTransaction(Transaction oldTransaction, Transaction newTransaction) {
        int brojac = 0;
        for(Transaction t: transactions) {
            if (t.equals(oldTransaction)) {
                transactions.set(brojac, newTransaction);
                break;
            }
            brojac++;
        }
        view.setTransactions(transactions);
    }

}

