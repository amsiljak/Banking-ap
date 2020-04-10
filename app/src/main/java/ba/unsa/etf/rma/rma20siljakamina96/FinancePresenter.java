package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class FinancePresenter implements IFinancePresenter {
    private Context context;

    private static IFinanceInteractor financeInteractor;
    private IFinanceView view;
    private static ArrayList<Transaction> transactions;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.financeInteractor = new FinanceInteractor();
        this.view = view;
        transactions = financeInteractor.getTransactions();
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }


    @Override
    public void setTransactions() {
        view.setTransactions(transactions);
    }

    @Override
    public void setAccount() {
        DecimalFormat df = new DecimalFormat("#.##");
        double iznos = 0;
        for(Transaction t : transactions) {
            iznos += t.getAmount();
        }
        iznos = financeInteractor.getAccount().getBudget() + iznos;
        view.setAccountData(df.format(iznos), String.valueOf(financeInteractor.getAccount().getTotalLimit()));
    }

    @Override
    public void refresh() {
        setAccount();
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
                startingPoint.setTime(t.getDate());

                Calendar endPoint = Calendar.getInstance();
                endPoint.setTime(t.getEndDate());

                //ovo sam dodala da bi se prikazivala transakcija i u mjesecu u kojem je datum
                Calendar temp = (Calendar) cal.clone();
                temp.add(Calendar.MONTH,1);

                if (startingPoint.compareTo(temp) <= 0 && cal.compareTo(endPoint) <= 0) {
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

//    @Override
//    public void deleteTransaction(Transaction t) {
//        Iterator itr = transactions.iterator();
//        while (itr.hasNext())
//        {
//            Transaction transaction = (Transaction)itr.next();
//            if (t.equals(transaction))
//                itr.remove();
//        }
//        setAccount();
//        view.setTransactions(transactions);
//    }

//    @Override
//    public void addTransaction(Transaction t) {
//        transactions.add(t);
//        setAccount();
//        view.setTransactions(transactions);
//    }

    public HashMap<String, Double> getMonthlyPayments() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-yyyy");
        HashMap<String, Double> iznosi = new HashMap<>();
        for(Transaction t: transactions) {
            if (iznosi.containsKey(DATE_FORMAT.format(t.getDate()))) {
                Double vrijednost = iznosi.get(DATE_FORMAT.format(t.getDate())) + t.getAmount();
                iznosi.put(DATE_FORMAT.format(t.getDate()), vrijednost);
            } else iznosi.put(t.getDate().toString(), t.getAmount());
        }
        return iznosi;
    }

}

