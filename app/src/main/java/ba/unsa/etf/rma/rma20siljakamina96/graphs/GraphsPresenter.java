package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.Context;
import android.graphics.Color;

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

    private float getXCoordinate(String month) {
        switch (month) {
            case "01":
                return 0f;
            case "02":
                return 1f;
            case "03":
                return 2f;
            case "04":
                return 3f;
            case "05":
                return 4f;
            case "06":
                return 5f;
            case "07":
                return 6f;
            case "08":
                return 7f;
            case "09":
                return 8f;
            case "10":
                return 9f;
            case "11":
                return 10f;
            case "12":
                return 11f;
        }
        return 0f;
    }
    @Override
    public void putDataToBarData(String timeUnit) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM");
        SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("YYYY");

        List<BarEntry> entries = new ArrayList<BarEntry>();
        float month;
        float endMonth;

        Map<Float, Float> mapa = new HashMap<>();

        if(timeUnit.equals("Month")) {
            for(float i = 0f; i < 12f; i++) {
                for(Transaction t: financeInteractor.getTransactions()) {
                    if((t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("INDIVIDUALPAYMENT")
                            || t.getType().toString().equals("REGULARPAYMENT")) && (YEAR_DATE_FORMAT.format(t.getDate())).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {

                        month = getXCoordinate(DATE_FORMAT.format(t.getDate()));

                        //ako je regular payment dodaji na vrijednost mjeseca kojeg predstavlja brojac
                        if(t.getType().toString().equals("REGULARPAYMENT")) {

                            int transactionInterval = t.getTransactionInterval();
                            float brojac = i;

                            endMonth = getXCoordinate(DATE_FORMAT.format(t.getEndDate()));

                            int pom = 30;
                            while(brojac >= month && brojac <= endMonth) {

                                Float value = 0f;

                                //oduzimam od 30 zbog moguceg ostatak od predhodnog mjeseca
                                pom-= 30;

                                //zbrajanje vrijednosti za mjesec brojac
                                while(pom != 0 && pom <= 30) {
                                    value += (float)t.getAmount();
                                    pom += transactionInterval;
                                }

                                //dodavanje vrijednosti u mjesec brojac za ovu transakciju
                                if (mapa.containsKey(i)) {
                                    Float oldValue = mapa.get(i) + value;
                                    mapa.put(i, oldValue);
                                } else mapa.put(i, value);

                                brojac++;
                            }
                        }
                        else if (month == i) {
                            if (mapa.containsKey(i)) {
                                Float value = mapa.get(i) + (float)t.getAmount();
                                mapa.put(i, value);
                            } else mapa.put(i, (float)t.getAmount());
                        }
                    }
                }
                if(mapa.containsKey(i)) entries.add(new BarEntry(i, mapa.get(i)));
            }
        }
        else if(timeUnit.equals("Day")) {

        }

        BarDataSet dataSet = new BarDataSet(entries, "Potrošnja"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width

        view.setConsumptionBarChart(barData);
    }
}
