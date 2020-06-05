package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public interface IFinanceView {
    void notifyTransactionListDataSetChanged();

    //    private AdapterView.OnClickListener leftButtonClickListener = new AdapterView.OnClickListener() {
    //        @Override
    //        public void onClick(View v) {
    //            cal.add(Calendar.MONTH,-1);
    //            financePresenter.setTransactions();
    //            setDate();
    //
    //            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
    //                transactionListView.setItemChecked(pozi,false);
    //                addTransactionButton.setEnabled(true);
    //                onAddButtonClick.onAddButtonClicked();
    //            }
    //        }
    //    };
    //    private AdapterView.OnClickListener rightButtonClickListener = new AdapterView.OnClickListener() {
    //        @Override
    //        public void onClick(View v) {
    //            cal.add(Calendar.MONTH,1);
    //            financePresenter.setTransactions();
    //            setDate();
    //
    //            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
    //                transactionListView.setItemChecked(pozi,false);
    //                addTransactionButton.setEnabled(true);
    //                onAddButtonClick.onAddButtonClicked();
    //            }
    //        }
    //    };
    //    private AdapterView.OnItemSelectedListener sortSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
    //        @Override
    //        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //            if (parent.getItemAtPosition(position) != null) {
    //                sort = parent.getItemAtPosition(position).toString();
    //                financePresenter.setTransactions();
    //            }
    //        }
    //
    //        @Override
    //        public void onNothingSelected(AdapterView<?> parent) {
    //
    //        }
    //    };
    //    private AdapterView.OnItemSelectedListener filterSpinnerItemSelectListener = new AdapterView.OnItemSelectedListener() {
    //        @Override
    //        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //            if(parent.getItemAtPosition(position)!= null) type = parent.getItemAtPosition(position).toString();
    //            financePresenter.setTransactions();
    //        }
    //        @Override
    //        public void onNothingSelected(AdapterView<?> parent) {
    //
    //        }
    //    };
    void setAccountData(Account account);

    void setTransactions(ArrayList<Transaction> transactions);
    void setDate();
}
