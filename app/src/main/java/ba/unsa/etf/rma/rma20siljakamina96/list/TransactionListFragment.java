package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import ba.unsa.etf.rma.rma20siljakamina96.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20siljakamina96.R;
import ba.unsa.etf.rma.rma20siljakamina96.account.AccountPresenter;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountPresenter;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountView;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;

public class TransactionListFragment extends Fragment implements IFinanceView {

    private ListView transactionListView;
    private OnItemClick onItemClick;
    private OnAddButtonClick onAddButtonClick;

    private OnSwipeLeft onSwipeLeft;
    private OnSwipeRight onSwipeRight;

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

    private FilterSpinnerAdapter filterSpinnerAdapter;
    private ArrayAdapter<String> sortSpinnerAdapter;

    private IFinancePresenter financePresenter;

    private TransactionListAdapter transactionListAdapter;

    private int pozi;

    ConstraintLayout listLayout;

    public IFinancePresenter getTransactionPresenter() {
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
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnAddButtonClick {
        void onAddButtonClicked();
    }
    public interface OnItemClick {
        void onItemClicked(Transaction transaction);
    }
    public interface OnSwipeLeft {
        void openBudgetFragmentFromList();
    }
    public interface OnSwipeRight {
        void openGraphsFragmentFromList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        transactionListAdapter = new TransactionListAdapter(getActivity(), R.layout.list_element, new ArrayList<Transaction>());
        transactionListView = (ListView)fragmentView.findViewById(R.id.listView);
        transactionListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        transactionListView.setAdapter(transactionListAdapter);
        transactionListView.setOnItemClickListener(transactionListItemClickListener);

        globalAmount2 = (TextView)fragmentView.findViewById(R.id.globalAmount2);
        limit2 = fragmentView.findViewById(R.id.limit2);

        onSwipeLeft = (OnSwipeLeft) getActivity();
        onSwipeRight = (OnSwipeRight) getActivity();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSwipeRight() {
                    onSwipeRight.openGraphsFragmentFromList();
                }
                @Override
                public void onSwipeLeft() {
                    onSwipeLeft.openBudgetFragmentFromList();
                }
            });
        }

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
        onItemClick = (OnItemClick) getActivity();
        onAddButtonClick = (OnAddButtonClick) getActivity();
//
//        getAccountPresenter();
//        getTransactionPresenter().refresh();
        getTransactionPresenter().getTransactions(null, "title.asc",String.valueOf(cal.get(Calendar.MONTH)+1),String.valueOf(cal.get(Calendar.YEAR)));
        financePresenter.getAccount();
        setDate();
        return fragmentView;
    }

    private AdapterView.OnClickListener leftButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,-1);
            financePresenter.getTransactions(null,sort, String.valueOf(cal.get(Calendar.MONTH)+1),String.valueOf(cal.get(Calendar.YEAR)));
            setDate();

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                transactionListView.setItemChecked(pozi,false);
                addTransactionButton.setEnabled(true);
                onAddButtonClick.onAddButtonClicked();
            }
        }
    };
    private AdapterView.OnClickListener rightButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,1);
            financePresenter.getTransactions(null,sort, String.valueOf(cal.get(Calendar.MONTH)+1),String.valueOf(cal.get(Calendar.YEAR)));
            setDate();

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                transactionListView.setItemChecked(pozi,false);
                addTransactionButton.setEnabled(true);
                onAddButtonClick.onAddButtonClicked();
            }
        }
    };
    private AdapterView.OnItemSelectedListener sortSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position) != null) {
                sort = parent.getItemAtPosition(position).toString();
                financePresenter.getTransactions(null, sort,String.valueOf(cal.get(Calendar.MONTH)+1),String.valueOf(cal.get(Calendar.YEAR)));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener filterSpinnerItemSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getItemAtPosition(position)!= null)
                type = parent.getItemAtPosition(position).toString();
            financePresenter.getTransactions(type, sort,String.valueOf(cal.get(Calendar.MONTH)+1),String.valueOf(cal.get(Calendar.YEAR)));
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public void setAccountData(Account account) {
        globalAmount2.setText(String.valueOf(account.getBudget()));
        limit2.setText(String.valueOf(account.getTotalLimit()));
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.clear();
//
//        transactions = financePresenter.sortTransactions(sort);
//        transactions = financePresenter.filterTransactionsByDate(transactions, cal);
//        transactions = financePresenter.filterTransactionsByType(transactions, type);

        transactionListAdapter.setTransactions(transactions);
//        notifyTransactionListDataSetChanged();
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
            //drugi klik na stavku

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if (pozi == position) {
                    transactionListView.setItemChecked(position, false);
                    addTransactionButton.setEnabled(true);
                    onAddButtonClick.onAddButtonClicked();
                    pozi = -1;
                } else {
                    pozi = position;
                    addTransactionButton.setEnabled(false);
                    onItemClick.onItemClicked(transaction);
                }
            }
            else {
                transactionListView.setItemChecked(position, false);
                addTransactionButton.setEnabled(false);
                onItemClick.onItemClicked(transaction);
            }
        }
    };
    private AdapterView.OnClickListener addTransactionClickListenr = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddButtonClick.onAddButtonClicked();
        }
    };
}
