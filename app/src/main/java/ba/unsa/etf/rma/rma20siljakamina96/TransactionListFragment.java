package ba.unsa.etf.rma.rma20siljakamina96;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class
TransactionListFragment extends Fragment implements IFinanceView{

    private ListView transactionListView;
    private OnItemClick onItemClick;
    private OnAddButtonClick onAddButtonClick;
    private TextView globalAmount2;
    private TextView limit2;
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
            financePresenter = new FinancePresenter(this, getActivity());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnAddButtonClick {
        void onAddButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        transactionListAdapter = new TransactionListAdapter(getActivity(), R.layout.list_element, new ArrayList<Transaction>());
        transactionListView = (ListView)fragmentView.findViewById(R.id.listView);
        transactionListView.setAdapter(transactionListAdapter);
        transactionListView.setOnItemClickListener(transactionListItemClickListener);

        globalAmount2 = (TextView)fragmentView.findViewById(R.id.globalAmount2);
        limit2 = fragmentView.findViewById(R.id.limit2);


        type = "All";
        filterSpinner = (Spinner)fragmentView.findViewById(R.id.filterSpinner);
        filterList = new ArrayList<>();
        for(Type t: Type.values()) filterList.add(t.toString());
        filterList.add("All");
        filterList.add("Filter by");
        filterSpinnerAdapter = new FilterSpinnerAdapter(getActivity(), R.layout.filter_spinner_item, filterList);
        filterSpinnerAdapter.setDropDownViewResource(R.layout.filter_spinner_dropdown_item);
        filterSpinner.setAdapter(new NothingSelectedSpinnerAdapter(filterSpinnerAdapter, R.layout.filter_spinner_row_nothing_selected, getActivity()));
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemSelectListener);

        sortSpinner = (Spinner) fragmentView.findViewById(R.id.sortSpinner);
        sortList = new ArrayList<>();
        sortList.add("Price - Ascending");
        sortList.add("Price - Descending");
        sortList.add("Title - Ascending");
        sortList.add("Title - Descending");
        sortList.add("Date - Ascending");
        sortList.add("Date - Descending");
        sortSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortList);
        sortSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(new NothingSelectedSpinnerAdapter(sortSpinnerAdapter, R.layout.sort_spinner_row_nothing_selected, getActivity()));
        sortSpinner.setOnItemSelectedListener(sortSpinnerItemSelectedListener);

        cal = Calendar.getInstance();
        dateView = (TextView)fragmentView.findViewById(R.id.date);

        leftImageButton = (ImageButton)fragmentView.findViewById(R.id.leftButton);
        rightImageButton = (ImageButton)fragmentView.findViewById(R.id.rightButton);

        leftImageButton.setOnClickListener(leftButtonClickListener);
        rightImageButton.setOnClickListener(rightButtonClickListener);

        addTransactionButton = (Button)fragmentView.findViewById(R.id.addTransaction);
        addTransactionButton.setOnClickListener(addTransactionClickListenr);
        transactionListView.setOnItemClickListener(listItemClickListener);
        onItemClick = (OnItemClick) getActivity();
        onAddButtonClick = (OnAddButtonClick) getActivity();

        getPresenter().refresh();
        return fragmentView;
    }
    public interface OnItemClick {
        void onItemClicked(Transaction transaction);
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Transaction transaction = transactionListAdapter.getTransaction(position);
            onItemClick.onItemClicked(transaction);
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
    private AdapterView.OnItemClickListener transactionListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Transaction transaction = transactionListAdapter.getTransaction(position);
            onItemClick.onItemClicked(transaction);

//            transactionDetailIntent.putExtra("calling-activity", 1);
//            double totalPayments = 0;
//            for(Map.Entry <String,Double> el : financePresenter.getMonthlyPayments().entrySet()) {
//                //zbrajam sve potrosnje u toku svih mjeseci radi total limita
//                totalPayments += el.getValue();
//                //saljem podatke o potrosnjama u svim mjesecima u kojima ima potrosnje ne ukljucujuci transakciju koja se modifikuje
//                transactionDetailIntent.putExtra(el.getKey(), el.getValue() - transaction.getAmount());
//            }
//            transactionDetailIntent.putExtra("totalPayments", totalPayments);
//            pozicija = position;
//
//            MainActivity.this.startActivityForResult(transactionDetailIntent, 1);

        }
    };
    private AdapterView.OnClickListener addTransactionClickListenr = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
//            transactionDetailIntent.putExtra("calling-activity", 2);
//            double totalPayments = 0;
//            for(Map.Entry <String,Double> el : financePresenter.getMonthlyPayments().entrySet()) {
//                //zbrajam sve potrosnje u toku svih mjeseci radi total limita
//                totalPayments += el.getValue();
//                //saljem podatke o potrosnjama u svim mjesecima u kojima ima potrosnje ne ukljucujuci transakciju koja se modifikuje
//                transactionDetailIntent.putExtra(el.getKey(), el.getValue());
//            }
//            transactionDetailIntent.putExtra("totalPayments", totalPayments);
//            MainActivity.this.startActivityForResult(transactionDetailIntent, 2);
            onAddButtonClick.onAddButtonClicked();
        }
    };
}
