package ba.unsa.etf.rma.rma20siljakamina96;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

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

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClickListener);

        deleteButton = (Button) findViewById(R.id.deleteButton);

getPresenter();
        if (getIntent().getIntExtra("calling-activity", 0) == 1) {

            getPresenter().create(getIntent().getStringExtra("title"), getIntent().getDoubleExtra("amount", 0),
                    (Type) getIntent().getSerializableExtra("type"), getIntent().getStringExtra("description"),
                    getIntent().getIntExtra("interval", 0), (Date) getIntent().getSerializableExtra("date"),
                    (Date) getIntent().getSerializableExtra("enddate"));

            Transaction transaction = getPresenter().getTransaction();

            if (getIntent().getIntExtra("calling-activity", 0) == 1)
                deleteButton.setOnClickListener(deleteClickListener);

            titleEditText.setText(transaction.getTitle());
            amountEditText.setText(String.valueOf(transaction.getAmount()));
            typeEditText.setText(transaction.getType().toString());

            if (!(transaction.getType().toString().equals("INDIVIDUALINCOME") || transaction.getType().toString().equals("REGULARINCOME")))
                descriptionEditText.setText(transaction.getItemDescription());
            else descriptionEditText.setText("");
            if (transaction.getType().toString().equals("REGULARINCOME") || transaction.getType().toString().equals("REGULARPAYMENT"))
                intervalEditText.setText(String.valueOf(transaction.getTransactionInterval()));
            else intervalEditText.setText("");
            if (transaction.getType().toString().equals("REGULARINCOME") || transaction.getType().toString().equals("REGULARPAYMENT"))
                endDateEditText.setText(DATE_FORMAT.format(transaction.getEndDate()));
            else endDateEditText.setText("");

            dateEditText.setText(DATE_FORMAT.format(transaction.getDate()));
        }
        titleEditText.addTextChangedListener(titleTextWatcher);
        typeEditText.addTextChangedListener(typeTextWatcher);
        amountEditText.addTextChangedListener(amountTextWatcher);
        descriptionEditText.addTextChangedListener(descriptionTextWatcher);
        intervalEditText.addTextChangedListener(intervalTextWatcher);
        dateEditText.addTextChangedListener(dateTextWatcher);
        endDateEditText.addTextChangedListener(endDateTextWatcher);
    }

    private AdapterView.OnClickListener saveClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
                if (presenter.checkBudget(Double.parseDouble(amountEditText.getText().toString()))) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TransactionDetailActivity.this);
                    builder1.setTitle("Save transaction");
                    builder1.setMessage("Iznos transakcije prelazi budžet. Da li ste sigurni da želite nastaviti?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (getIntent().getIntExtra("calling-activity", 0) == 1) {
                                        titleEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        amountEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        descriptionEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        dateEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        endDateEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        typeEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        intervalEditText.setBackgroundColor(android.R.attr.editTextColor);
                                        dialog.cancel();
                                    }
                                    else if(getIntent().getIntExtra("calling-activity", 0) == 2) {
                                        Intent resultIntent = new Intent();

                                        resultIntent.putExtra("action", "add");
                                        resultIntent.putExtra("title", presenter.getTransaction().getTitle());
                                        resultIntent.putExtra("amount", presenter.getTransaction().getAmount());
                                        resultIntent.putExtra("date", presenter.getTransaction().getDate());
                                        resultIntent.putExtra("enddate", presenter.getTransaction().getEndDate());
                                        resultIntent.putExtra("interval", presenter.getTransaction().getTransactionInterval());
                                        resultIntent.putExtra("description", presenter.getTransaction().getItemDescription());
                                        resultIntent.putExtra("type", presenter.getTransaction().getType());

                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else  {
                    if (getIntent().getIntExtra("calling-activity", 0) == 1) {
                        titleEditText.setBackgroundColor(android.R.attr.editTextColor);
                        amountEditText.setBackgroundColor(android.R.attr.editTextColor);
                        descriptionEditText.setBackgroundColor(android.R.attr.editTextColor);
                        dateEditText.setBackgroundColor(android.R.attr.editTextColor);
                        endDateEditText.setBackgroundColor(android.R.attr.editTextColor);
                        typeEditText.setBackgroundColor(android.R.attr.editTextColor);
                        intervalEditText.setBackgroundColor(android.R.attr.editTextColor);
                    }
                    else if(getIntent().getIntExtra("calling-activity", 0) == 2) {
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("action", "add");
                        resultIntent.putExtra("title", String.valueOf(titleEditText.getText()));
                        resultIntent.putExtra("amount", Double.parseDouble(String.valueOf(amountEditText.getText())));
                        try {
                            resultIntent.putExtra("date",  DATE_FORMAT.parse(String.valueOf(dateEditText.getText())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            resultIntent.putExtra("enddate", DATE_FORMAT.parse(String.valueOf(endDateEditText.getText())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        resultIntent.putExtra("interval", Integer.valueOf(String.valueOf(intervalEditText.getText())));
                        resultIntent.putExtra("description", String.valueOf(descriptionEditText.getText()));
                        resultIntent.putExtra("type", Type.valueOf(typeEditText.getText().toString().toUpperCase()));

                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }
        }
    }

        ;
        private AdapterView.OnClickListener deleteClickListener = new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TransactionDetailActivity.this)
                        .setTitle("Delete transaction")
                        .setMessage("Da li ste sigurni da želite obrisati ovu transakciju?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("action", "delete");
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        };
        private TextWatcher titleTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (titleEditText.getText().toString().length() <= 3 || titleEditText.getText().toString().length() >= 15) {
                    titleEditText.setBackgroundColor(Color.RED);
                } else titleEditText.setBackgroundColor(Color.GREEN);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };
        private TextWatcher typeTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                boolean valid = false;
                for (Type t : Type.values()) {
                    if (t.toString().equals(typeEditText.getText().toString().toUpperCase()))
                        valid = true;
                }
                if (valid) typeEditText.setBackgroundColor(Color.GREEN);
                else typeEditText.setBackgroundColor(Color.RED);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        private TextWatcher amountTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                boolean valid;
                try {
                    Double num = Double.parseDouble(amountEditText.getText().toString());
                    valid = true;
                } catch (NumberFormatException e) {
                    valid = false;
                }

                if (valid && !amountEditText.equals(""))
                    amountEditText.setBackgroundColor(Color.GREEN);
                else amountEditText.setBackgroundColor(Color.RED);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        private TextWatcher descriptionTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!descriptionEditText.equals("") ||
                        ((descriptionEditText.getText().toString().equals("REGULARINCOME") || typeEditText.getText().toString().equals("INDIVIDUALINCOME")) &&
                                descriptionEditText.equals("")))
                    descriptionEditText.setBackgroundColor(Color.GREEN);
                else descriptionEditText.setBackgroundColor(Color.RED);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        private TextWatcher intervalTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (intervalEditText.getText().toString().matches("[0-9]+") &&
                        (descriptionEditText.getText().toString().equals("REGULARINCOME") || typeEditText.getText().toString().equals("REGULARPAYMENT")))
                    intervalEditText.setBackgroundColor(Color.GREEN);
                else intervalEditText.setBackgroundColor(Color.RED);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        private TextWatcher dateTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                dateEditText.setBackgroundColor(Color.GREEN);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        private TextWatcher endDateTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                endDateEditText.setBackgroundColor(Color.GREEN);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
//ovdje ce ici sve vezano za save
        switch(keyCode) {
            case(KeyEvent.KEYCODE_BACK):
                Intent resultIntent = new Intent();

                    resultIntent.putExtra("action", "save");
                    resultIntent.putExtra("title", presenter.getTransaction().getTitle());
                    resultIntent.putExtra("amount", presenter.getTransaction().getAmount());
                    resultIntent.putExtra("date", presenter.getTransaction().getDate());
                    resultIntent.putExtra("enddate", presenter.getTransaction().getEndDate());
                    resultIntent.putExtra("interval", presenter.getTransaction().getTransactionInterval());
                    resultIntent.putExtra("description", presenter.getTransaction().getItemDescription());
                    resultIntent.putExtra("type", presenter.getTransaction().getType());

                setResult(RESULT_OK, resultIntent);
                finish();
        }
        return false;
    }
}


