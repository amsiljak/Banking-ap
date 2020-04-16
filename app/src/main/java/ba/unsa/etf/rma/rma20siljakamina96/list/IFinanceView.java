package ba.unsa.etf.rma.rma20siljakamina96.list;

import java.util.ArrayList;

import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;

public interface IFinanceView {
    void notifyTransactionDataSetChanged();
    void setTransactions(ArrayList<Transaction> transactions);
    void setDate();
    void setAccountData();
}
