package ba.unsa.etf.rma.rma20siljakamina96;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText endDateEditText;
    private EditText descriptionEditText;
    private EditText intervalEditText;
    private EditText typeEditText;

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

        Transaction transaction = getPresenter().getTransaction();

        titleEditText = (EditText) findViewById(R.id.transactionTitle);
        titleEditText.setText(transaction.getTitle());

        amountEditText = (EditText) findViewById(R.id.transactionAmount);
        amountEditText.setText(String.valueOf(transaction.getAmount()));

        typeEditText = (EditText) findViewById(R.id.transactionType);
        typeEditText.setText(transaction.getType().toString());

        descriptionEditText = (EditText) findViewById(R.id.transactionDescription);
        descriptionEditText.setText(transaction.getItemDescription());

        intervalEditText = (EditText) findViewById(R.id.transactionInterval);
        intervalEditText.setText(String.valueOf(transaction.getTransactionInterval()));

        dateEditText = (EditText) findViewById(R.id.transactionDate);
        dateEditText.setText(transaction.getDate().toString());

        endDateEditText = (EditText) findViewById(R.id.transactionEndDate);
        endDateEditText.setText(transaction.getEndDate().toString());

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

//            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//            builder1.setMessage("Write your message here.");
//            builder1.setCancelable(true);
//
//            builder1.setPositiveButton(
//                    "Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert11 = builder1.create();
//            alert11.show();

//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("some_key", "String data");
//            setResult(Activity.RESULT_OK, resultIntent);
//            finish();
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
