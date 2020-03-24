package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IFinanceView{
    private TextView globalAmount2;
    private TextView limit2;
    private ListView transactionListView;
    private IFinancePresenter financePresenter;
    private TransactionListAdapter transactionListAdapter;

    public IFinancePresenter getPresenter() {
        if (financePresenter == null) {
            financePresenter = new FinancePresenter(this, this);
        }
        return financePresenter;
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
        transactionListView = findViewById(R.id.transactionList);
        transactionListView.setAdapter(transactionListAdapter);
        getPresenter().refresh();
    }

    @Override
    public void setAccountData(String globalAmount, String limit) {
        globalAmount2.setText(globalAmount);
        limit2.setText(limit);
    }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
    }
}
