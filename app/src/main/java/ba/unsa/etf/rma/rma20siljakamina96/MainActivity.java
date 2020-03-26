package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements IFinanceView{
    private TextView globalAmount2;
    private TextView limit2;
    private ListView transactionListView;
    private Spinner filterSpinner;
    private Spinner sortSpinner;
    private TextView dateView;
    private ImageButton leftImageButton;
    private ImageButton rightImageButton;
    private Calendar cal;
    private String type;
    private ArrayList<String> filterList;
    private ArrayList<String> sortList;

    private FilterSpinnerAdapter filterSpinnerAdapter;
    private ArrayAdapter<String> sortSpinnerAdapter;

    private IFinancePresenter financePresenter;
    private TransactionListAdapter transactionListAdapter;

    public IFinancePresenter getPresenter() {
        if (financePresenter == null) {
            financePresenter = new FinancePresenter(this, this);
        }
        return financePresenter;
    }
    private String getMonth() {
        int mjesec = cal.get(Calendar.MONTH);
        String month = "wrong";

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (mjesec >= 0 && mjesec <= 11 ) {
            month = months[mjesec];
        }
        return month;
    }

    @Override
    public void sortTransactions(ArrayList<Transaction> transactions, String tip) {
        if(tip.equals("Price - Ascending")) Collections.sort(transactions, Transaction.TranPriceComparatorAsc);
        setTransactions(transactions);
    }

    @Override
    public void notifyTransactionDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionListAdapter = new TransactionListAdapter(getApplicationContext(), R.layout.list_element, new ArrayList<Transaction>());
        transactionListView = (ListView)findViewById(R.id.transactionList);
        transactionListView.setAdapter(transactionListAdapter);

        globalAmount2 = (TextView)findViewById(R.id.globalAmount2);
        limit2 = findViewById(R.id.limit2);


        type = "All";
        filterSpinner = (Spinner)findViewById(R.id.filterSpinner);
        filterList = new ArrayList<>();
        for(Type t: Type.values()) filterList.add(t.toString());
        filterList.add("All");
        filterList.add("Filter by");
        filterSpinnerAdapter = new FilterSpinnerAdapter(getApplicationContext(), R.layout.filter_spinner_item, filterList);
        filterSpinnerAdapter.setDropDownViewResource(R.layout.filter_spinner_dropdown_item);
        filterSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                filterSpinnerAdapter,
                R.layout.filter_spinner_row_nothing_selected,
                this));
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position)!= null) type = parent.getItemAtPosition(position).toString();
                financePresenter.refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        sortList = new ArrayList<>();
        sortList.add("Price - Ascending");
        sortList.add("Price - Descending");
        sortList.add("Title - Ascending");
        sortList.add("Title - Descending");
        sortList.add("Date - Ascending");
        sortList.add("Date - Descending");
        sortSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortList);
        filterSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                sortSpinnerAdapter,
                R.layout.sort_spinner_row_nothing_selected,
                this));

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position)!= null){ financePresenter.sortTransactions(parent.getItemAtPosition(position).toString());}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cal = Calendar.getInstance();
        dateView = (TextView)findViewById(R.id.date);

        leftImageButton = (ImageButton)findViewById(R.id.leftButton);
        rightImageButton = (ImageButton)findViewById(R.id.rightButton);

        leftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,-1);
                financePresenter.refresh();
                setDate();
            }
        });
        rightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,1);
                financePresenter.refresh();
                setDate();
            }
        });
        getPresenter().refresh();
    }


    @Override
    public void setAccountData(String globalAmount, String limit) {
        globalAmount2.setText(globalAmount);
        limit2.setText(limit);
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        boolean correctDate = false;
        transactionListAdapter.clear();

        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            if (t.getType().toString().equals("REGULARPAYMENT") || t.getType().toString().equals("REGULARINCOME")) {

                Calendar startingPoint = Calendar.getInstance();
                startingPoint.setTime(t.getDate());

                Calendar endPoint = Calendar.getInstance();
                endPoint.setTime(t.getEndDate());

                if (cal.compareTo(startingPoint) > 0 && cal.compareTo(startingPoint) < 0) {
                    correctDate = true;
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(t.getDate());

                if (calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                        && calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) correctDate = true;
            }
            if(correctDate) {
                if(type.equals("All")) lista.add(t);
                else if(t.getType().toString().equals(type)) lista.add(t);
            }
        }
        transactionListAdapter.setTransactions(lista);
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDate() {

        int mjesec = cal.get(Calendar.MONTH);
        String month = getMonth();
        String year = "";

        year = String.valueOf(cal.get(Calendar.YEAR));
        dateView.setText(month + ", " + year);
    }

}
