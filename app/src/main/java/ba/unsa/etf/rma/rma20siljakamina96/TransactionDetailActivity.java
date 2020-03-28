package ba.unsa.etf.rma.rma20siljakamina96;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class TransactionDetailActivity extends AppCompatActivity implements ITransactionDetailActivity {
    private ITransactionDetailPresenter presenter;
    private EditText titleEditText;
    private Button saveButton;
    private Button deleteButton;


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

        getPresenter().create(getIntent().getStringExtra("title"), getIntent().getDoubleExtra("amount", 0),
                (Type) getIntent().getSerializableExtra("type"), getIntent().getStringExtra("description"),
                getIntent().getIntExtra("interval", 0), (Date) getIntent().getSerializableExtra("date"),
                (Date) getIntent().getSerializableExtra("enddate"));

        titleEditText = (EditText) findViewById(R.id.transactionTitle);
        Transaction transaction = getPresenter().getTransaction();
        titleEditText.setText(transaction.getTitle());

        titleEditText.setOnFocusChangeListener(titleFocusChangeListener);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClickListener);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(deleteClickListener);
    }

    private AdapterView.OnClickListener saveClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            titleEditText.setBackgroundColor(0x00000000);
        }
    };
    private AdapterView.OnClickListener deleteClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("some_key", "String data");
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    };
    private EditText.OnFocusChangeListener titleFocusChangeListener = new EditText.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                {
                    titleEditText.setBackgroundColor(Color.GREEN);
                }
            }
        }
    };
}
