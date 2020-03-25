package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements IFinanceView{
    private TextView globalAmount2;
    private TextView limit2;
    private ListView transactionListView;
    private Spinner filterSpinner;
    private TextView dateView;
    private ImageButton leftImageButton;
    private ImageButton rightImageButton;
    private Calendar cal = Calendar.getInstance();

    private ArrayAdapter<Type> filterSpinnerAdapter;

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
        globalAmount2 = (TextView)findViewById(R.id.globalAmount2);
        limit2 = findViewById(R.id.limit2);
        transactionListView = (ListView)findViewById(R.id.transactionList);
        transactionListView.setAdapter(transactionListAdapter);

        filterSpinner = (Spinner)findViewById(R.id.filterSpinner);
        ArrayList<Type> list = new ArrayList<>();
        list.addAll(Arrays.asList(Type.values()));
        filterSpinnerAdapter = new FilterSpinnerAdapter(getApplicationContext(), R.layout.filter_spinner_item, list);
        filterSpinner.setAdapter(filterSpinnerAdapter);

        dateView = (TextView)findViewById(R.id.date);

        leftImageButton = (ImageButton)findViewById(R.id.leftButton);
        rightImageButton = (ImageButton)findViewById(R.id.rightButton);

        leftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,-1);
                financePresenter.refresh();
                setDate();
                //fazon je da svaki put kad se klike dugme postavljaju se drugacije transakcije,
                //kao i mjesec u textviewu
                //trebam negdje sacuvati
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


        transactionListAdapter.clear();
        ArrayList<Transaction> lista = new ArrayList<>();
        for(Transaction t: transactions) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(t.getDatum());

            if(calendar.get(Calendar.MONTH) ==  cal.get(Calendar.MONTH)
                    && calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) lista.add(t);
        }
        transactionListAdapter.setTransactions(lista);
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
