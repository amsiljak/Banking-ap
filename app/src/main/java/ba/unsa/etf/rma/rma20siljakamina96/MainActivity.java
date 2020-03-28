package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        transactionListView.setOnItemClickListener(transactionListItemClickListener);

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
        filterSpinner.setAdapter(new NothingSelectedSpinnerAdapter(filterSpinnerAdapter, R.layout.filter_spinner_row_nothing_selected, this));
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemSelectListener);

        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        sortList = new ArrayList<>();
        sortList.add("Price - Ascending");
        sortList.add("Price - Descending");
        sortList.add("Title - Ascending");
        sortList.add("Title - Descending");
        sortList.add("Date - Ascending");
        sortList.add("Date - Descending");
        sortSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortList);
        sortSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(new NothingSelectedSpinnerAdapter(sortSpinnerAdapter, R.layout.sort_spinner_row_nothing_selected, this));
        sortSpinner.setOnItemSelectedListener(sortSpinnerItemSelectedListener);

        cal = Calendar.getInstance();
        dateView = (TextView)findViewById(R.id.date);

        leftImageButton = (ImageButton)findViewById(R.id.leftButton);
        rightImageButton = (ImageButton)findViewById(R.id.rightButton);

        leftImageButton.setOnClickListener(leftButtonClickListener);
        rightImageButton.setOnClickListener(rightButtonClickListener);
        getPresenter().refresh();
    }


    @Override
    public void setAccountData(String globalAmount, String limit) {
        globalAmount2.setText(globalAmount);
        limit2.setText(limit);
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.clear();

        transactions = financePresenter.filterTransactionsByDate(transactions, cal);
        transactions = financePresenter.filterTransactionsByType(transactions, type);

        transactionListAdapter.setTransactions(transactions);
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDate() {
        String month = getMonth();
        String year = "";

        year = String.valueOf(cal.get(Calendar.YEAR));
        dateView.setText(month + ", " + year);
    }
    private AdapterView.OnItemSelectedListener filterSpinnerItemSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getItemAtPosition(position)!= null) type = parent.getItemAtPosition(position).toString();
            financePresenter.setTransactions();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener sortSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position) != null) {
                financePresenter.sortTransactions(parent.getItemAtPosition(position).toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnClickListener leftButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,-1);
            financePresenter.setTransactions();
            setDate();
        }
    };
    private AdapterView.OnClickListener rightButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,1);
            financePresenter.setTransactions();
            setDate();
        }
    };
    private AdapterView.OnItemClickListener transactionListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
            Transaction transaction = transactionListAdapter.getTransaction(position);

            transactionDetailIntent.putExtra("title", transaction.getTitle());
            transactionDetailIntent.putExtra("amount", transaction.getAmount());
            transactionDetailIntent.putExtra("description", transaction.getItemDescription());
            transactionDetailIntent.putExtra("type", transaction.getType());
            transactionDetailIntent.putExtra("interval", transaction.getTransactionInterval());
            transactionDetailIntent.putExtra("date", transaction.getDate());
            transactionDetailIntent.putExtra("enddate", transaction.getEndDate());

            MainActivity.this.startActivityForResult(transactionDetailIntent, 100);
        }
    };
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

}
