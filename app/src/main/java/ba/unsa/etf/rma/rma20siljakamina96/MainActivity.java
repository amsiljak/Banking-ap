package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements IFinanceView {
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
    private Button addTransactionButton;
    private String sort = "Price - Ascending";
    private int pozicija;

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

        addTransactionButton = (Button)findViewById(R.id.addTransaction);
        addTransactionButton.setOnClickListener(addTransactionClickListenr);
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

        transactions = financePresenter.sortTransactions(sort);
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
                sort = parent.getItemAtPosition(position).toString();
                financePresenter.setTransactions();
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
    private AdapterView.OnClickListener addTransactionClickListenr = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
            transactionDetailIntent.putExtra("calling-activity", 2);
            MainActivity.this.startActivityForResult(transactionDetailIntent, 2);
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
            transactionDetailIntent.putExtra("calling-activity", 1);
            pozicija = position;

            MainActivity.this.startActivityForResult(transactionDetailIntent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Transaction t = (Transaction) transactionListView.getItemAtPosition(pozicija); //ovo je transakcija u cije smo detalje usli
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data.getStringExtra("action").equals("delete")) financePresenter.deleteTransaction(t);
                else if(data.getStringExtra("action").equals("save")) {
                    Transaction transaction = new Transaction((Date) data.getSerializableExtra("date"), data.getDoubleExtra("amount", 0),
                            data.getStringExtra("title"), (Type) data.getSerializableExtra("type"), data.getStringExtra("description"),
                            data.getIntExtra("interval", 0),
                            (Date) data.getSerializableExtra("enddate"));
                    financePresenter.modifyTransaction(t, transaction);
                }
                //ako je korisnik samo izasao bez klika na save ne treba nista uraditi
            }
        }
        //dodavanje nove transakcije
        else if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                //ovaj uslov je samo u jednom slucaju zadovoljen, a to je da je korisnik klikuno save pri dodavanju nove transakcije
                if(data.getStringExtra("action").equals("add")) {
                    Transaction transaction = new Transaction((Date)data.getSerializableExtra("date"), data.getDoubleExtra("amount", 0),
                                data.getStringExtra("title"), (Type) data.getSerializableExtra("type"), data.getStringExtra("description"),
                                data.getIntExtra("interval", 0),
                                (Date) data.getSerializableExtra("enddate"));
                    financePresenter.addTransaction(transaction);
                }
            }
            //ako je korisnik izasao na dugme back ne treba nista uraditi
        }
    }
}
