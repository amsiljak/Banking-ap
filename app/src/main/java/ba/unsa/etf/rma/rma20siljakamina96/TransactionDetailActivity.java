package ba.unsa.etf.rma.rma20siljakamina96;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class TransactionDetailActivity extends AppCompatActivity implements ITransactionDetailActivity {
    private ITransactionDetailPresenter presenter;
    private EditText titleEditText;

    public ITransactionDetailPresenter getPresenter() {
        if (presenter == null) {
            presenter = new TransactionDetailPresenter(this);
        }
        return presenter;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        getPresenter().create(getIntent().getStringExtra("title"),getIntent().getDoubleExtra("amount", 0),
                (Type)getIntent().getSerializableExtra("type"),getIntent().getStringExtra("description"),
                getIntent().getIntExtra("interval", 0),(Date)getIntent().getSerializableExtra("date"),
                (Date)getIntent().getSerializableExtra("enddate"));
        titleEditText = (EditText)findViewById(R.id.transactionTitle);
        Transaction transaction = getPresenter().getTransaction();
        titleEditText.setText(transaction.getTitle());

    }

}
