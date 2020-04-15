package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.unsa.etf.rma.rma20siljakamina96.account.AccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.list.ITransactionInteractor;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionInteractor;

public class GraphsPresenter implements IGraphsPresenter{
    private Context context;

    private static ITransactionInteractor financeInteractor;
    private IAccountInteractor accountInteractor;
    private IGraphsView view;
    private static ArrayList<Transaction> transactions;

    public GraphsPresenter(IGraphsView view, Context context) {
        this.context = context;
        this.financeInteractor = new TransactionInteractor();
        this.accountInteractor = new AccountInteractor();
        this.view = view;
    }


    @Override
    public void putDataToBarData(String timeUnit) {
        SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("DD");
        SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MM");
        SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("YYYY");

        List<BarEntry> entries = new ArrayList<BarEntry>();
        Map<Float, Float> mapa = new HashMap<>();

        if(timeUnit.equals("Month")) {
            float month;
            for(float i = 1f; i < 13f; i++) {
                for(Transaction t: financeInteractor.getTransactions()) {
                    if((t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                            || t.getType().toString().equals("REGULARPAYMENT")) && (YEAR_DATE_FORMAT.format(t.getDate())).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {

                        month = Float.valueOf(MONTH_DATE_FORMAT.format(t.getDate()));

                        //ako je regular payment dodaji na vrijednost mjeseca kojeg predstavlja brojac

                        if (month == i) {
                            if(t.getType().toString().equals("REGULARPAYMENT")) {

                                Calendar dateOfPayment = Calendar.getInstance();
                                Calendar endDateOfPayment = Calendar.getInstance();
                                dateOfPayment.setTime(t.getDate());
                                endDateOfPayment.setTime(t.getEndDate());

                                //povecava pocetni datum za interval sve dok ne dodje do krajnjeg,
                                //i onda uzima mjesec pocetnog i na njega stavlja amount
                                while(dateOfPayment.compareTo(endDateOfPayment) <= 0 && dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                                    month = Float.valueOf(MONTH_DATE_FORMAT.format(dateOfPayment.getTime()));
                                    if (mapa.containsKey(month)) {
                                        Float oldValue = mapa.get(month) + (float)t.getAmount();
                                        mapa.put(month, oldValue);
                                    } else mapa.put(month, (float)t.getAmount());

                                    dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
                                }

                            }
                            else {
                                if (mapa.containsKey(i)) {
                                    Float value = mapa.get(i) + (float) t.getAmount();
                                    mapa.put(i, value);
                                } else {
                                    mapa.put(i, (float) t.getAmount());
                                }
                            }
                        }
                    }
                }
            }
            for(float i = 0f; i < 12f; i++) {
                if(mapa.containsKey(i)) entries.add(new BarEntry(i, mapa.get(i)));
                else entries.add(new BarEntry(i, 0f));
            }
        }
        else if(timeUnit.equals("Day")) {
            float day;
            for(float i = 1f; i < 30f; i++) {
                for(Transaction t: financeInteractor.getTransactions()) {
                    Calendar transactionMonth = Calendar.getInstance();
                    transactionMonth.setTime(t.getDate());
                    if((t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                            || t.getType().toString().equals("REGULARPAYMENT")) && (YEAR_DATE_FORMAT.format(t.getDate())).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))) &&
                    transactionMonth.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {

                        day = Float.valueOf(DAY_DATE_FORMAT.format(t.getDate()));

                        //ako je regular payment dodaji na vrijednost mjeseca kojeg predstavlja brojac

                        if (day == i) {
                            if(t.getType().toString().equals("REGULARPAYMENT")) {

                                Calendar dateOfPayment = Calendar.getInstance();
                                Calendar endDateOfPayment = Calendar.getInstance();
                                dateOfPayment.setTime(t.getDate());
                                endDateOfPayment.setTime(t.getEndDate());

                                //povecava pocetni datum za interval sve dok ne dodje do krajnjeg,
                                //i onda uzima dan pocetnog i na njega stavlja amount
                                while(dateOfPayment.compareTo(endDateOfPayment) <= 0 && dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                                    day = Float.valueOf(DAY_DATE_FORMAT.format(dateOfPayment.getTime()));
                                    if (mapa.containsKey(day)) {
                                        Float oldValue = mapa.get(day) + (float)t.getAmount();
                                        mapa.put(day, oldValue);
                                    } else mapa.put(day, (float)t.getAmount());

                                    dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
                                }

                            }
                            else {
                                if (mapa.containsKey(i)) {
                                    Float value = mapa.get(i) + (float) t.getAmount();
                                    mapa.put(i, value);
                                } else {
                                    mapa.put(i, (float) t.getAmount());
                                }
                            }
                        }
                    }
                }
            }
            for(float i = 1f; i < 31f; i++) {
                if(mapa.containsKey(i)) entries.add(new BarEntry(i, mapa.get(i)));
                else entries.add(new BarEntry(i, 0f));
            }
        }
//        else if(timeUnit.equals("Week")) {
//            float week;
//            for(float i = 1f; i <= 4f; i++) {
//                for(Transaction t: financeInteractor.getTransactions()) {
//                    if((t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
//                            || t.getType().toString().equals("REGULARPAYMENT")) && (YEAR_DATE_FORMAT.format(t.getDate())).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
//
//                        day = Float.valueOf(DAY_DATE_FORMAT.format(t.getDate()));
//
//                        if (day == i) {
//                            if(t.getType().toString().equals("REGULARPAYMENT")) {
//
//                                Calendar dateOfPayment = Calendar.getInstance();
//                                Calendar endDateOfPayment = Calendar.getInstance();
//                                dateOfPayment.setTime(t.getDate());
//                                endDateOfPayment.setTime(t.getEndDate());
//
//                                //povecava pocetni datum za interval sve dok ne dodje do krajnjeg,
//                                //i onda uzima dan pocetnog i na njega stavlja amount
//                                while(dateOfPayment.compareTo(endDateOfPayment) <= 0 && dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
//                                    day = Float.valueOf(DAY_DATE_FORMAT.format(dateOfPayment.getTime()));
//                                    if (mapa.containsKey(day)) {
//                                        Float oldValue = mapa.get(day) + (float)t.getAmount();
//                                        mapa.put(day, oldValue);
//                                    } else mapa.put(day, (float)t.getAmount());
//
//                                    dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
//                                }
//
//                            }
//                            else {
//                                if (mapa.containsKey(i)) {
//                                    Float value = mapa.get(i) + (float) t.getAmount();
//                                    mapa.put(i, value);
//                                } else {
//                                    mapa.put(i, (float) t.getAmount());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            for(float i = 1f; i <= 4f; i++) {
//                if(mapa.containsKey(i)) entries.add(new BarEntry(i, mapa.get(i)));
//                else entries.add(new BarEntry(i, 0f));
//            }
//        }

        BarDataSet dataSet = new BarDataSet(entries, "Potrošnja"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        view.setConsumptionBarChart(barData);
    }
}
