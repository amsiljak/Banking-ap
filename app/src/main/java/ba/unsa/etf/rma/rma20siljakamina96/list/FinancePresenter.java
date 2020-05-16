package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;

public class FinancePresenter implements IFinancePresenter, TransactionListInteractor.OnTransactionGetDone {
    private Context context;
    private IFinanceView view;

    public FinancePresenter(IFinanceView view, Context context) {
        this.context = context;
        this.view = view;
    }
    @Override
    public void onDone(ArrayList<Transaction> results) {

//        ArrayList<Transaction> transactions = new ArrayList<Transaction>() {
//            {
//                try {
//                    add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-15"), 100, "t1",
//                            Type.INDIVIDUALPAYMENT, "opis", null, null));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"), 500, "t2",
//                            Type.REGULARINCOME, null, 30, null));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    add(new Transaction(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-16"), 200, "t3",
//                            Type.INDIVIDUALPAYMENT, "opis",null,null));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        view.setTransactions(transactions);
        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();
    }
    @Override
    public void getTransactions(){
        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone)
                this).execute("transactions");
    }
    @Override
    public void setAccount() {
        view.setAccountData();
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



//    @Override
//    public ArrayList<Transaction> sortTransactions(String tip) {
//        ArrayList<Transaction> transakcije = new ArrayList<>();
//        transakcije.addAll(financeInteractor.getTransactions());
//        if(tip.equals("Price - Ascending")) Collections.sort(transakcije, Transaction.TranPriceComparatorAsc);
//        if(tip.equals("Price - Descending")) Collections.sort(transakcije, Transaction.TranPriceComparatorDesc);
//        if(tip.equals("Title - Ascending")) Collections.sort(transakcije, Transaction.TranTitleComparatorAsc);
//        if(tip.equals("Title - Descending")) Collections.sort(transakcije, Transaction.TranTitleComparatorDesc);
//        if(tip.equals("Date - Ascending")) Collections.sort(transakcije, Transaction.TranDateComparatorAsc);
//        if(tip.equals("Date - Descending")) Collections.sort(transakcije, Transaction.TranDateComparatorDesc);
//
//        return transakcije;
//    }
//
//    @Override
//    public ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, String type) {
//        ArrayList<Transaction> lista = new ArrayList<>();
//        for(Transaction t: transactions) {
//            if (type.equals("All")) lista.add(t);
//            else if (t.getType().toString().equals(type)) lista.add(t);
//        }
//        return lista;
//    }
//    @Override
//    public ArrayList<Transaction> filterTransactionsByDate(ArrayList<Transaction> transactions, Calendar cal) {
//
//        ArrayList<Transaction> lista = new ArrayList<>();
//        for(Transaction t: transactions) {
//            if (t.getType().toString().equals("REGULARPAYMENT") || t.getType().toString().equals("REGULARINCOME")) {
//
//                Calendar startingPoint = Calendar.getInstance();
//                startingPoint.setTime(t.getDate());
//
//                Calendar endPoint = Calendar.getInstance();
//                if(t.getEndDate()!= null) endPoint.setTime(t.getEndDate());
//
//                //ovo sam dodala da bi se prikazivala transakcija i u mjesecu u kojem je datum
//                Calendar temp = (Calendar) cal.clone();
//                temp.add(Calendar.MONTH,1);
//
//                if (startingPoint.compareTo(temp) <= 0 && (t.getEndDate()==null || cal.compareTo(endPoint) <= 0)) {
//                    lista.add(t);
//                }
//            } else {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(t.getDate());
//
//                if (calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
//                        && calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR))  lista.add(t);
//            }
//        }
//        return lista;
//    }
}

