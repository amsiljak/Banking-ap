package ba.unsa.etf.rma.rma20siljakamina96.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ba.unsa.etf.rma.rma20siljakamina96.R;
import ba.unsa.etf.rma.rma20siljakamina96.account.AccountPresenter;
import ba.unsa.etf.rma.rma20siljakamina96.account.IAccountPresenter;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.data.Type;
import ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver;

import static ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver.connected;

public class TransactionDetailFragment extends Fragment implements ITransactionDetailView, ConnectivityBroadcastReceiver.onConnectionChanged {
    private ITransactionDetailPresenter presenter;
    private IAccountPresenter accountPresenter;

    private EditText titleEditText;
    private EditText amountEditText;
    private EditText dateEditText;
    private EditText endDateEditText;
    private EditText descriptionEditText;
    private EditText intervalEditText;
    private EditText typeEditText;
    private TextView offlineText;

    private Button saveButton;
    private Button deleteButton;

    private SimpleDateFormat DATE_FORMAT;

    private OnTransactionModify onTransactionModify;
    private OnTransactionAddOrDelete onTransactionAddOrDelete;
    private OnAddButtonClick onAddButtonClick;

    private boolean validTitle, validAmount, validDate, validEndDate, validDescription, validInterval, validType;

    private boolean saving;

    private Transaction transaction;

    @Override
    public void onConnectionChanged() {
        if(saving) offlineText.setText("Offline izmjena");
        else offlineText.setText("Offline dodavanje");
    }

    public interface OnTransactionModify {
        void onTransactionModified();
    }
    public interface OnTransactionAddOrDelete {
        void onTransactionAddedOrDeleted();
    }
    public interface OnAddButtonClick {
        void onAddButtonClicked(Account account);
    }
    public ITransactionDetailPresenter getPresenter() {
        if (presenter == null) {
            presenter = new TransactionDetailPresenter(this, getActivity());
        }
        return presenter;
    }
    public IAccountPresenter getAccountPresenter() {
        if (accountPresenter == null) {
            accountPresenter = new AccountPresenter(getActivity(),null);
        }
        return accountPresenter;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        titleEditText = (EditText) view.findViewById(R.id.transactionTitle);
        amountEditText = (EditText) view.findViewById(R.id.transactionAmount);
        typeEditText = (EditText) view.findViewById(R.id.transactionType);
        descriptionEditText = (EditText) view.findViewById(R.id.transactionDescription);
        intervalEditText = (EditText) view.findViewById(R.id.transactionInterval);
        dateEditText = (EditText) view.findViewById(R.id.transactionDate);
        endDateEditText = (EditText) view.findViewById(R.id.transactionEndDate);

        offlineText = (TextView) view.findViewById(R.id.offlineTextView);

        saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClickListener);

        deleteButton = (Button) view.findViewById(R.id.deleteButton);

        onTransactionModify = (OnTransactionModify) getActivity();
        onTransactionAddOrDelete = (OnTransactionAddOrDelete) getActivity();
        onAddButtonClick = (OnAddButtonClick) getActivity();

        getPresenter();
        DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

        if (getArguments() != null && getArguments().containsKey("transaction")) {
            saving = true;
            presenter.setTransaction(getArguments().getParcelable("transaction"));
            transaction = getPresenter().getTransaction();

            deleteButton.setOnClickListener(deleteClickListener);
            if(transaction.isDeleted()) deleteButton.setText("undo");

            titleEditText.setText(transaction.getTitle());
            amountEditText.setText(String.valueOf(transaction.getAmount()));
            typeEditText.setText(transaction.getType().toString());

            if (!(transaction.getType().toString().equals("INDIVIDUALINCOME") || transaction.getType().toString().equals("REGULARINCOME")) && transaction.getItemDescription() != null)
                descriptionEditText.setText(transaction.getItemDescription());
            else descriptionEditText.setText("");

            if (transaction.getType().toString().equals("REGULARINCOME") || transaction.getType().toString().equals("REGULARPAYMENT"))
                intervalEditText.setText(String.valueOf(transaction.getTransactionInterval()));
            else intervalEditText.setText("");
            if (transaction.getEndDate() != null)
                endDateEditText.setText(DATE_FORMAT.format(transaction.getEndDate()));
            else endDateEditText.setText("");

            dateEditText.setText(DATE_FORMAT.format(transaction.getDate()));

            validTitle = true; validAmount = true; validDate = true; validEndDate = true; validDescription = true; validInterval = true; validType = true;
        }
        else {
            saving = false;
            validDate = false; validAmount = false; validType = false; validTitle = false;
        }
        titleEditText.addTextChangedListener(titleTextWatcher);
        typeEditText.addTextChangedListener(typeTextWatcher);
        amountEditText.addTextChangedListener(amountTextWatcher);
        descriptionEditText.addTextChangedListener(descriptionTextWatcher);
        intervalEditText.addTextChangedListener(intervalTextWatcher);
        dateEditText.addTextChangedListener(dateTextWatcher);
        endDateEditText.addTextChangedListener(endDateTextWatcher);

        presenter.setAccount();
        getAccountPresenter();

        return view;
    }
    void validateInterval() {
        if((typeEditText.getText().toString().toUpperCase().equals("REGULARINCOME") || typeEditText.getText().toString().toUpperCase().equals("REGULARPAYMENT"))) {
            if(intervalEditText.getText().toString().matches("[0-9]+")) validInterval = true;
            else validInterval = false;
        }
        else {
            if (TextUtils.isEmpty(intervalEditText.getText().toString())) validInterval = true;
            else validInterval = false;
        }
    }
    void validateEndDate() {
        validEndDate = true;
    }
    void validateDescription() {
        if((typeEditText.getText().toString().toUpperCase().equals("REGULARINCOME") || typeEditText.getText().toString().toUpperCase().equals("INDIVIDUALINCOME"))) {
            if(TextUtils.isEmpty(descriptionEditText.getText().toString())) validDescription = true;
            else validDescription = false;
        }
        else {
            if(TextUtils.isEmpty(descriptionEditText.getText().toString())) validDescription = false;
            else validDescription = true;
        }
    }
    private TextWatcher titleTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (titleEditText.getText().toString().length() <= 3 || titleEditText.getText().toString().length() >= 15) {
                titleEditText.setBackgroundColor(Color.RED);
                validTitle = false;
            } else {
                titleEditText.setBackgroundColor(Color.GREEN);
                validTitle = true;
            }
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
            if (valid) {
                typeEditText.setBackgroundColor(Color.GREEN);
                validType = true;
                validateInterval();
                validateDescription();
                validateEndDate();
            }
            else {
                typeEditText.setBackgroundColor(Color.RED);
                validType = false;
            }
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

            if (valid) {
                validAmount = true;
                amountEditText.setBackgroundColor(Color.GREEN);
            }
            else {
                amountEditText.setBackgroundColor(Color.RED);
                validAmount = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private TextWatcher descriptionTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            validateDescription();
            if(validDescription) descriptionEditText.setBackgroundColor(Color.GREEN);
            else descriptionEditText.setBackgroundColor(Color.RED);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private TextWatcher intervalTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            validateInterval();
            if(validInterval) intervalEditText.setBackgroundColor(Color.GREEN);
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
            validDate = true;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private TextWatcher endDateTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            validateEndDate();
            if(validEndDate)endDateEditText.setBackgroundColor(Color.GREEN);
            else endDateEditText.setBackgroundColor(Color.RED);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void saveAction() {
        String type = typeEditText.getText().toString().toUpperCase();

        Date date = null;
        if(!endDateEditText.getText().equals("")) {
            try {
                date = DATE_FORMAT.parse(String.valueOf(dateEditText.getText()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //izmjena transakcije
        if (saving) {
            titleEditText.setBackgroundColor(android.R.attr.editTextColor);
            amountEditText.setBackgroundColor(android.R.attr.editTextColor);
            descriptionEditText.setBackgroundColor(android.R.attr.editTextColor);
            dateEditText.setBackgroundColor(android.R.attr.editTextColor);
            endDateEditText.setBackgroundColor(android.R.attr.editTextColor);
            typeEditText.setBackgroundColor(android.R.attr.editTextColor);
            intervalEditText.setBackgroundColor(android.R.attr.editTextColor);

            //salje se stari iznos transakcije zabiljezen u presenteru
            presenter.updateBudget("update", amountEditText.getText().toString(), typeEditText.getText().toString().toUpperCase());
            presenter.update(dateEditText.getText().toString(), amountEditText.getText().toString(),titleEditText.getText().toString(),
                    typeEditText.getText().toString().toUpperCase(), descriptionEditText.getText().toString(),
                    intervalEditText.getText().toString(), endDateEditText.getText().toString());
            onTransactionModify.onTransactionModified();


        //dodavanje transakcije
        } else {
            presenter.updateBudget("add", amountEditText.getText().toString(), typeEditText.getText().toString().toUpperCase());
            presenter.add(dateEditText.getText().toString(), amountEditText.getText().toString(),titleEditText.getText().toString(),
                    typeEditText.getText().toString().toUpperCase(), descriptionEditText.getText().toString(),
                    intervalEditText.getText().toString(), endDateEditText.getText().toString());


            onTransactionAddOrDelete.onTransactionAddedOrDeleted();
        }
    }

    private AdapterView.OnClickListener saveClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            String type = typeEditText.getText().toString().toUpperCase();

            if (!(validTitle && validAmount && validDate && validDescription && validEndDate && validInterval && validType)) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Nevalidan unos!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            //ako su unosi validni idi na spasavanje
            //ako je placanje treba provjeriti da li ce se sa njim premasiti limit
            else if (type.equals("PURCHASE") || type.equals("INDIVIDUALPAYMENT")
                    || type.equals("REGULARPAYMENT")){
                double totalPayments = presenter.getTotalPayments();
                if (presenter.isOverLimit(Double.parseDouble(amountEditText.getText().toString()), String.valueOf(dateEditText.getText()).substring(3)) ||
                        ((totalPayments + Double.parseDouble(amountEditText.getText().toString())) > presenter.getAccount().getTotalLimit())) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setTitle("Save transaction");
                    builder1.setMessage("Iznos transakcije prelazi budžet ili limit. Da li ste sigurni da želite nastaviti?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveAction();
                                    dialog.cancel();
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
                } else saveAction();
            }else saveAction();
        }
    };
    private AdapterView.OnClickListener deleteClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(transaction.isDeleted()) {
                transaction.setDeleted(false);
                onTransactionModify.onTransactionModified();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    onTransactionAddOrDelete.onTransactionAddedOrDeleted();
                } else {
                    onTransactionModify.onTransactionModified();
                    onAddButtonClick.onAddButtonClicked(presenter.getAccount());
                }
            }
            else {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete transaction")
                        .setMessage("Da li ste sigurni da želite obrisati ovu transakciju?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                presenter.updateBudget("delete", amountEditText.getText().toString(), typeEditText.getText().toString().toUpperCase());
                                presenter.delete();

                                onTransactionModify.onTransactionModified();
                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    onTransactionAddOrDelete.onTransactionAddedOrDeleted();
                                } else {
                                    onTransactionModify.onTransactionModified();
                                    onAddButtonClick.onAddButtonClicked(presenter.getAccount());
                                }
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    };

}
