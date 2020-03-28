package ba.unsa.etf.rma.rma20siljakamina96;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        titleEditText = (EditText) findViewById(R.id.transactionTitle);

        amountEditText = (EditText) findViewById(R.id.transactionAmount);

        typeEditText = (EditText) findViewById(R.id.transactionType);

        descriptionEditText = (EditText) findViewById(R.id.transactionDescription);

        intervalEditText = (EditText) findViewById(R.id.transactionInterval);

        dateEditText = (EditText) findViewById(R.id.transactionDate);

        endDateEditText = (EditText) findViewById(R.id.transactionEndDate);

        titleEditText.addTextChangedListener(titleTextWatcher);
        typeEditText.addTextChangedListener(typeTextWatcher);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClickListener);

        deleteButton = (Button) findViewById(R.id.deleteButton);


        if(getIntent().getIntExtra("calling-activity", 0) == 1) {
            getPresenter().create(getIntent().getStringExtra("title"), getIntent().getDoubleExtra("amount", 0),
                    (Type) getIntent().getSerializableExtra("type"), getIntent().getStringExtra("description"),
                    getIntent().getIntExtra("interval", 0), (Date) getIntent().getSerializableExtra("date"),
                    (Date) getIntent().getSerializableExtra("enddate"));

            Transaction transaction = getPresenter().getTransaction();

            if(getIntent().getIntExtra("calling-activity", 0) == 1) deleteButton.setOnClickListener(deleteClickListener);
            titleEditText.setText(transaction.getTitle());
            amountEditText.setText(String.valueOf(transaction.getAmount()));
            typeEditText.setText(transaction.getType().toString());
            descriptionEditText.setText(transaction.getItemDescription());
            intervalEditText.setText(String.valueOf(transaction.getTransactionInterval()));
            dateEditText.setText(transaction.getDate().toString());
            endDateEditText.setText(transaction.getEndDate().toString());
        }
    }

    private AdapterView.OnClickListener saveClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            //treba vratit boju polja na pocetnu onih polja koja su se promijenila

            ColorDrawable drawable = (ColorDrawable)titleEditText.getBackground();
            if(drawable.getColor()==(int)Color.GREEN) titleEditText.setBackgroundColor(0x00000000);
//            amountEditText.setBackgroundColor(0x00000000);
//            descriptionEditText.setBackgroundColor(0x00000000);
//            dateEditText.setBackgroundColor(0x00000000);
//            endDateEditText.setBackgroundColor(0x00000000);
//            typeEditText.setBackgroundColor(0x00000000);
//            intervalEditText.setBackgroundColor(0x00000000);

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

            Intent resultIntent = new Intent();
            resultIntent.putExtra("some_key", "String data");
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    };
    private TextWatcher titleTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (titleEditText.getText().toString().length() <= 3 || titleEditText.getText().toString().length() >= 15) {
                titleEditText.setBackgroundColor(Color.RED);
            }
            else titleEditText.setBackgroundColor(Color.GREEN);
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };
    private TextWatcher typeTextWatcher = new TextWatcher() {
        boolean valid = false;
        public void afterTextChanged(Editable s) {
            for(Type t: Type.values()) {
                if(t.toString().equals(typeEditText.getText().toString().toUpperCase())) valid = true;
            }
            if(valid) typeEditText.setBackgroundColor(Color.GREEN);
            else typeEditText.setBackgroundColor(Color.RED);
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    };
}
