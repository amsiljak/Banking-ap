package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class FinancePresenter implements IFinancePresenter {
    private Context context;
    private IFinanceInteractor financeInteractor;
    private IFinanceView view;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.financeInteractor = new FinanceInteractor();
        this.view = view;
    }

    @Override
    public void setTransactions() {
        view.setTransactions(financeInteractor.getTransactions());
    }

    @Override
    public void refresh() {
        view.setAccountData(String.valueOf(financeInteractor.getAccount().getBudget()),String.valueOf(financeInteractor.getAccount().getTotalLimit()));
        view.setTransactions(financeInteractor.getTransactions());
        view.notifyTransactionDataSetChanged();
        view.setDate();
    }

    @Override
    public void sortTransactions(String tip) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.addAll(financeInteractor.getTransactions());
        if(tip.equals("Price - Ascending")) Collections.sort(transactions, Transaction.TranPriceComparatorAsc);
        if(tip.equals("Price - Descending")) Collections.sort(transactions, Transaction.TranPriceComparatorDesc);
        if(tip.equals("Title - Ascending")) Collections.sort(transactions, Transaction.TranTitleComparatorAsc);
        if(tip.equals("Title - Descending")) Collections.sort(transactions, Transaction.TranTitleComparatorDesc);
        if(tip.equals("Date - Ascending")) Collections.sort(transactions, Transaction.TranDateComparatorAsc);
        if(tip.equals("Date - Descending")) Collections.sort(transactions, Transaction.TranDateComparatorDesc);

        view.setTransactions(transactions);
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
                startingPoint.setTime(t.getDate());

                Calendar endPoint = Calendar.getInstance();
                endPoint.setTime(t.getEndDate());

                if (cal.compareTo(startingPoint) >= 0 && cal.compareTo(endPoint) <= 0) {
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
