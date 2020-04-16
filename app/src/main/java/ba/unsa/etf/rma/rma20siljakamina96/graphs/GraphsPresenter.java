package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("dd");
    private SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat("MM");

    private static ITransactionInteractor financeInteractor;
    private IGraphsView view;

    public GraphsPresenter(IGraphsView view, Context context) {
        this.context = context;
        this.financeInteractor = new TransactionInteractor();
        this.view = view;
    }

    private float determineWeek(float day) {
        float week;
        if(day>=1f && day<=7f) week = 1f;
        else if(day>=8f && day<=14f) week = 2f;
        else if(day>=15f && day<=21f) week = 3f;
        else  week = 4f;
        return week;
    }
    private List<BarEntry> putMapDataToEntries(Map<Float, Float> mapa, String timeUnit) {
        float limit;
        if(timeUnit.equals("Day")) limit = 31f;
        else if(timeUnit.equals("Week")) limit = 4f;
        else limit = 12f;

        List<BarEntry> entries = new ArrayList<>();

        for(float i = 1f; i <= limit; i++) {
            if (mapa.containsKey(i)) entries.add(new BarEntry(i, mapa.get(i)));
            else entries.add(new BarEntry(i, 0f));

        }
        return entries;
    }
    private Map<Float,Float> putValueToMap(Float key, Float mapValue, Map<Float,Float> mapa) {
        if (mapa.containsKey(key)) {
            Float value = mapa.get(key) + mapValue;
            mapa.put(key, value);
        } else {
            mapa.put(key, mapValue);
        }
        return mapa;
    }
    private Map<Float,Float> monthForRegular(Transaction t, Map<Float, Float> mapa) {
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
            if(dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                float month = Float.valueOf(MONTH_DATE_FORMAT.format(dateOfPayment.getTime()));
                mapa = putValueToMap(month, (float)t.getAmount(), mapa);
            }
            dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
        }
        return mapa;
    }
    private Map<Float,Float> dayOrWeekForRegular(Transaction t, Map<Float, Float> mapa, String timeUnit) {
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
        //i onda uzima dan pocetnog i na njega stavlja amount
        while (dateOfPayment.compareTo(endDateOfPayment) <= 0) {
            if (dateOfPayment.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                    dateOfPayment.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
                float day = Float.valueOf(DAY_DATE_FORMAT.format(dateOfPayment.getTime()));
                if(timeUnit.equals("Week")) day = determineWeek(day);

                mapa = putValueToMap(day, (float)t.getAmount(), mapa);
            }
            dateOfPayment.add(Calendar.DATE, t.getTransactionInterval());
        }
        return mapa;
    }
    @Override
    public Map<Float, Float> putConsumptionDataToBarData(String timeUnit) {
        List<BarEntry> entries;
        Map<Float, Float> mapa = new HashMap<>();

        for(Transaction t: financeInteractor.getTransactions()) {

            Calendar transactionMonth = Calendar.getInstance();
            transactionMonth.setTime(t.getDate());

            if((t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                    || t.getType().toString().equals("REGULARPAYMENT"))) {

                if(timeUnit.equals("Day") || timeUnit.equals("Week")) {
                    if (t.getType().toString().equals("REGULARPAYMENT")) mapa = dayOrWeekForRegular(t, mapa, timeUnit);
                    else if (transactionMonth.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                            transactionMonth.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {

                        float day = Float.valueOf(DAY_DATE_FORMAT.format(t.getDate()));
                        if(timeUnit.equals("Week")) day = determineWeek(day);

                        mapa = putValueToMap(day, (float)t.getAmount(), mapa);
                    }
                }
                else if(timeUnit.equals("Month")) {
                    if(t.getType().toString().equals("REGULARPAYMENT")) mapa = monthForRegular(t, mapa);
                    else if(transactionMonth.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
                        mapa = putValueToMap(Float.valueOf(MONTH_DATE_FORMAT.format(t.getDate())), (float)t.getAmount(), mapa);
                    }
                }
            }
        }

        entries = putMapDataToEntries(mapa, timeUnit);

        BarDataSet dataSet = new BarDataSet(entries, "Potrošnja"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        view.setConsumptionBarChart(barData);

        return mapa;
    }
    @Override
    public Map<Float, Float> putEarningsDataToBarData(String timeUnit) {
        List<BarEntry> entries;
        Map<Float, Float> mapa = new HashMap<>();

        for(Transaction t: financeInteractor.getTransactions()) {

            Calendar transactionMonth = Calendar.getInstance();
            transactionMonth.setTime(t.getDate());

            if(t.getType().toString().equals("INDIVIDUALINCOME") || t.getType().toString().equals("REGULARINCOME")) {

                if(timeUnit.equals("Day") || timeUnit.equals("Week")) {
                    if (t.getType().toString().equals("REGULARINCOME")) mapa = dayOrWeekForRegular(t, mapa, timeUnit);
                    else if (transactionMonth.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                            transactionMonth.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {

                        float day = Float.valueOf(DAY_DATE_FORMAT.format(t.getDate()));
                        if(timeUnit.equals("Week")) day = determineWeek(day);

                        mapa = putValueToMap(day, (float)t.getAmount(), mapa);
                    }
                }
                else if(timeUnit.equals("Month")) {
                    if(t.getType().toString().equals("REGULARINCOME")) mapa = monthForRegular(t, mapa);
                    else if(transactionMonth.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
                        mapa = putValueToMap(Float.valueOf(MONTH_DATE_FORMAT.format(t.getDate())), (float)t.getAmount(), mapa);
                    }
                }
            }
        }

        entries = putMapDataToEntries(mapa, timeUnit);

        BarDataSet dataSet = new BarDataSet(entries, "Zarada"); // add entries to dataset
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        view.setEarningsBarChart(barData);

        return mapa;
    }
    @Override
    public void putTotalDataToBarData(String timeUnit) {
        List<BarEntry> entries;
        Map<Float, Float> mapa = new HashMap<>();

        float limit;
        if(timeUnit.equals("Day")) limit = 31f;
        else if(timeUnit.equals("Week")) limit = 4f;
        else limit = 12f;

        float lastValue = 0f;

        Map<Float, Float> consumptionsMap = putConsumptionDataToBarData(timeUnit);
        Map<Float, Float> earningsMap = putEarningsDataToBarData(timeUnit);

        for(float i = 1f; i < limit; i++) {
            float consumption = 0f;
            float earning = 0f;

            if(consumptionsMap.containsKey(i)) consumption = consumptionsMap.get(i);
            if(earningsMap.containsKey(i)) earning = earningsMap.get(i);
            mapa.put(i, -consumption+earning+lastValue);
            lastValue = -consumption+earning+lastValue;
        }
        entries = putMapDataToEntries(mapa, timeUnit);

        BarDataSet dataSet = new BarDataSet(entries, "Ukupno stanje"); // add entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        view.setTotalBarChart(barData);
    }
}
