package ba.unsa.etf.rma.rma20siljakamina96.list;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
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
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;
import ba.unsa.etf.rma.rma20siljakamina96.detail.ITransactionDetailPresenter;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionDetailPresenter;

import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;

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
    private static Calendar cal;
    private static String type;
    private static String sort = "Price - Ascending";
    private ArrayList<String> filterList;
    private ArrayList<String> sortList;
    private Button addTransactionButton;

    private FilterSpinnerAdapter filterSpinnerAdapter;
    private ArrayAdapter<String> sortSpinnerAdapter;
    private TransactionListAdapter transactionListAdapter;

    private IFinancePresenter financePresenter;
    private ITransactionDetailPresenter detailPresenter;

    private int pozi;

    public IFinancePresenter getTransactionPresenter() {
        if (financePresenter == null) {
            financePresenter = new FinancePresenter(this, getActivity());
        }
        return financePresenter;
    }
    public ITransactionDetailPresenter getDetailPresenter() {
        if (detailPresenter == null) {
            detailPresenter = new TransactionDetailPresenter(getActivity());
        }
        return detailPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface OnAddButtonClick {
        void onAddButtonClicked(Account account);
    }
    public interface OnItemClick {
        void onItemClicked(Transaction transaction, String action);
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

        if(cal == null) cal = Calendar.getInstance();
        dateView = (TextView)fragmentView.findViewById(R.id.date);

        leftImageButton = (ImageButton)fragmentView.findViewById(R.id.leftButton);
        rightImageButton = (ImageButton)fragmentView.findViewById(R.id.rightButton);

        leftImageButton.setOnClickListener(leftButtonClickListener);
        rightImageButton.setOnClickListener(rightButtonClickListener);

        addTransactionButton = (Button)fragmentView.findViewById(R.id.addTransaction);
        addTransactionButton.setOnClickListener(addTransactionClickListenr);
        onItemClick = (OnItemClick) getActivity();
        onAddButtonClick = (OnAddButtonClick) getActivity();

        getTransactionPresenter().getTransactions(type, "title.asc",cal);
        getDetailPresenter();
        financePresenter.setAccount();
        setDate();

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());

        if(connected) detailPresenter.uploadToServis();

        return fragmentView;
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
    private AdapterView.OnClickListener leftButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,-1);
            financePresenter.getTransactions(type,sort, cal);
            setDate();

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                transactionListView.setItemChecked(pozi,false);
                addTransactionButton.setEnabled(true);
                onAddButtonClick.onAddButtonClicked(financePresenter.getAccount());
            }
        }
    };
    private AdapterView.OnClickListener rightButtonClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            cal.add(Calendar.MONTH,1);
            financePresenter.getTransactions(type,sort,cal);
            setDate();

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                transactionListView.setItemChecked(pozi,false);
                addTransactionButton.setEnabled(true);
                onAddButtonClick.onAddButtonClicked(financePresenter.getAccount());
            }
        }
    };
    private AdapterView.OnItemSelectedListener sortSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position) != null) {
                sort = parent.getItemAtPosition(position).toString();
                financePresenter.getTransactions(type, sort,cal);
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
            financePresenter.getTransactions(type, sort,cal);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public void setAccountData(Account account) {
        if(account != null) {
            globalAmount2.setText(String.valueOf(account.getBudget()));
            limit2.setText(String.valueOf(account.getTotalLimit()));
        }
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.clear();
        transactionListAdapter.setTransactions(transactions);
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
            String action = detailPresenter.getAction(transaction);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if (pozi == position) {
                    transactionListView.setItemChecked(position, false);
                    addTransactionButton.setEnabled(true);
                    onAddButtonClick.onAddButtonClicked(financePresenter.getAccount());
                    pozi = -1;
                } else {
                    pozi = position;
                    addTransactionButton.setEnabled(false);

                    onItemClick.onItemClicked(transaction, action);
                }
            }
            else {
                transactionListView.setItemChecked(position, false);
                addTransactionButton.setEnabled(false);
                onItemClick.onItemClicked(transaction, action);
            }
        }
    };
    private AdapterView.OnClickListener addTransactionClickListenr = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddButtonClick.onAddButtonClicked(financePresenter.getAccount());
        }
    };
    @Override
    public void uploadToServis() {
        detailPresenter.uploadToServis();
        financePresenter.getTransactions(type,sort,cal);
    }
}
